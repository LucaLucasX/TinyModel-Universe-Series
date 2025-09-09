"""
Attention U-Net (2D) in PyTorch

Paper: Oktay et al., "Attention U-Net: Learning Where to Look for the Pancreas", 2018.
This implementation provides a clean, dependency-free (only PyTorch) model that
works as a drop-in replacement for a standard U-Net, adding Attention Gates on
skip connections.

Features
- Configurable number of channels and depth
- Bilinear upsampling or transposed-conv upsampling
- Optional BatchNorm and Dropout
- Weight initialization (Kaiming)
- Tiny smoke test in __main__

Usage
-----
from attention_unet_pytorch import AttUNet
model = AttUNet(in_channels=3, out_channels=1, base_channels=64,
                depth=4, bilinear=True, use_bn=True, dropout=0.0)

x = torch.randn(2, 3, 256, 256)
y = model(x)
print(y.shape)  # (2, 1, 256, 256)
"""
from typing import List
import torch
from torch import nn
import torch.nn.functional as F


class DoubleConv(nn.Module):
    """(Conv -> BN? -> ReLU) * 2"""
    def __init__(self, in_ch: int, out_ch: int, use_bn: bool = True, dropout: float = 0.0):
        super().__init__()
        bias = not use_bn
        layers = [
            nn.Conv2d(in_ch, out_ch, kernel_size=3, padding=1, bias=bias),
        ]
        if use_bn:
            layers.append(nn.BatchNorm2d(out_ch))
        layers.append(nn.ReLU(inplace=True))
        if dropout > 0:
            layers.append(nn.Dropout2d(dropout))
        layers += [
            nn.Conv2d(out_ch, out_ch, kernel_size=3, padding=1, bias=bias),
        ]
        if use_bn:
            layers.append(nn.BatchNorm2d(out_ch))
        layers.append(nn.ReLU(inplace=True))
        self.block = nn.Sequential(*layers)

    def forward(self, x: torch.Tensor) -> torch.Tensor:
        return self.block(x)


class AttentionGate(nn.Module):
    """Attention Gate to filter skip connections based on decoder gating signal.

    Given a skip feature x (from encoder) and a gating feature g (from decoder),
    produce attention coefficients alpha in [0,1] with shape like x, and return
    x * alpha.

    Follows the additive attention described in the paper:
      q = W_x * x + W_g * g + b  -> ReLU -> psi -> sigmoid -> alpha
    """
    def __init__(self, in_ch: int, gating_ch: int, inter_ch: int):
        super().__init__()
        # Project both x and g to an intermediate number of channels
        self.theta_x = nn.Conv2d(in_ch, inter_ch, kernel_size=1, stride=1, padding=0, bias=True)
        self.phi_g   = nn.Conv2d(gating_ch, inter_ch, kernel_size=1, stride=1, padding=0, bias=True)
        self.relu    = nn.ReLU(inplace=True)
        self.psi     = nn.Conv2d(inter_ch, 1, kernel_size=1, stride=1, padding=0, bias=True)
        self.sigmoid = nn.Sigmoid()

    def forward(self, x: torch.Tensor, g: torch.Tensor) -> torch.Tensor:
        # Align spatial shapes by interpolation if needed
        if x.shape[2:] != g.shape[2:]:
            g = F.interpolate(g, size=x.shape[2:], mode="bilinear", align_corners=False)
        q = self.theta_x(x) + self.phi_g(g)
        q = self.relu(q)
        psi = self.sigmoid(self.psi(q))  # (B,1,H,W)
        return x * psi  # broadcast along channel dim


class UpBlock(nn.Module):
    """Upsample + Attention-gated skip + DoubleConv."""
    def __init__(self, in_ch: int, skip_ch: int, out_ch: int, bilinear: bool, use_bn: bool, dropout: float):
        super().__init__()
        if bilinear:
            self.up = nn.Upsample(scale_factor=2, mode="bilinear", align_corners=False)
            up_ch = in_ch
        else:
            self.up = nn.ConvTranspose2d(in_ch, in_ch // 2, kernel_size=2, stride=2)
            up_ch = in_ch // 2

        # Attention gate filters the skip features
        inter_ch = max(out_ch // 2, 1)
        self.att = AttentionGate(in_ch=skip_ch, gating_ch=up_ch, inter_ch=inter_ch)

        # After concatenation: (up_ch + skip_ch) -> out_ch via DoubleConv
        self.conv = DoubleConv(up_ch + skip_ch, out_ch, use_bn=use_bn, dropout=dropout)

    def forward(self, x: torch.Tensor, skip: torch.Tensor) -> torch.Tensor:
        x = self.up(x)
        # pad if needed due to odd dims to ensure same H,W
        diffY = skip.size()[2] - x.size()[2]
        diffX = skip.size()[3] - x.size()[3]
        if diffY != 0 or diffX != 0:
            x = F.pad(x, [diffX // 2, diffX - diffX // 2,
                          diffY // 2, diffY - diffY // 2])
        # attention on skip
        skip_att = self.att(skip, x)
        x = torch.cat([skip_att, x], dim=1)
        return self.conv(x)


class AttUNet(nn.Module):
    def __init__(
        self,
        in_channels: int = 3,
        out_channels: int = 1,
        base_channels: int = 64,
        depth: int = 4,
        bilinear: bool = True,
        use_bn: bool = True,
        dropout: float = 0.0,
    ):
        """
        Args:
            in_channels: number of input channels
            out_channels: number of output channels (e.g., classes for segmentation)
            base_channels: number of channels at the first encoder stage
            depth: number of down/up stages (typical U-Net uses 4)
            bilinear: if True use bilinear upsampling; else use transposed conv
            use_bn: if True, add BatchNorm after convs
            dropout: Dropout2d probability inside DoubleConv blocks
        """
        super().__init__()
        assert depth >= 1
        chs: List[int] = [base_channels * (2 ** i) for i in range(depth)]

        # Encoder
        self.inc = DoubleConv(in_channels, chs[0], use_bn=use_bn, dropout=dropout)
        self.downs = nn.ModuleList()
        self.pools = nn.ModuleList([nn.MaxPool2d(2) for _ in range(depth - 1)])
        for i in range(1, depth):
            self.downs.append(DoubleConv(chs[i - 1], chs[i], use_bn=use_bn, dropout=dropout))

        # Bottleneck
        self.bot = DoubleConv(chs[-1], chs[-1] * 2, use_bn=use_bn, dropout=dropout)

        # Decoder
        dec_channels = [chs[-1] * 2] + chs[::-1]  # bottleneck out then skips order
        self.ups = nn.ModuleList()
        out_chs: List[int] = chs[::-1]  # output channel per up stage
        for i in range(depth):
            in_ch = dec_channels[i]
            if i == 0:
                # first up uses skip = chs[-1]
                skip_ch = chs[-1]
            else:
                skip_ch = chs[depth - 1 - i]
            out_ch = out_chs[i]
            self.ups.append(UpBlock(in_ch, skip_ch, out_ch, bilinear, use_bn, dropout))

        # Final conv
        self.outc = nn.Conv2d(out_chs[-1], out_channels, kernel_size=1)

        self._init_weights()

    def _init_weights(self):
        for m in self.modules():
            if isinstance(m, (nn.Conv2d, nn.ConvTranspose2d)):
                nn.init.kaiming_normal_(m.weight, nonlinearity="relu")
                if m.bias is not None:
                    nn.init.constant_(m.bias, 0.0)
            elif isinstance(m, nn.BatchNorm2d):
                nn.init.constant_(m.weight, 1.0)
                nn.init.constant_(m.bias, 0.0)

    def forward(self, x: torch.Tensor) -> torch.Tensor:
        # Encoder path (store skips)
        skips = []
        x = self.inc(x)              # C
        skips.append(x)
        for pool, down in zip(self.pools, self.downs):
            x = pool(x)
            x = down(x)
            skips.append(x)

        # Bottleneck
        x = F.max_pool2d(x, 2)
        x = self.bot(x)

        # Decoder path with attention-gated skips (reverse order)
        for i, up in enumerate(self.ups):
            skip = skips[-(i + 1)]
            x = up(x, skip)

        return self.outc(x)


if __name__ == "__main__":
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model = AttUNet(in_channels=3, out_channels=3, base_channels=32, depth=4,
                    bilinear=True, use_bn=True, dropout=0.1).to(device)
    x = torch.randn(2, 3, 256, 256, device=device)
    with torch.no_grad():
        y = model(x)
    print("Input:", x.shape, "Output:", y.shape)
