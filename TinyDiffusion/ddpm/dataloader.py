import os

import numpy as np
import torch
import torchvision
from torchvision import transforms
from torch.utils.data import DataLoader
import matplotlib.pyplot as plt

os.environ["KMP_DUPLICATE_LIB_OK"] = "TRUE"

def load_transformed_dataset(data_dir ="./datasets/Animals-10-split", is_Animal = True,img_size=128, batch_size=128) -> DataLoader:
    """加载数据集"""
    train_data_transform = transforms.Compose([
        transforms.Resize((img_size, img_size)),
        transforms.RandomHorizontalFlip(),  # 随机水平翻转
        transforms.ToTensor(),  # 将数据缩放到[0, 1]范围
        transforms.Normalize((0.5, 0.5, 0.5), (0.5, 0.5, 0.5)),  # 将数据缩放到[-1, 1]范围
    ])
    test_data_transform = transforms.Compose([
        transforms.Resize((img_size, img_size)),
        transforms.ToTensor(),
        transforms.Normalize((0.5, 0.5, 0.5), (0.5, 0.5, 0.5)),
    ])
    if not is_Animal:
        data_dir = "./datasets"
        # 加载训练集和测试集
        train_dataset = torchvision.datasets.CIFAR10(root=data_dir,
                                                    train=True,
                                                    download=True,
                                                    transform=train_data_transform)

        test_dataset = torchvision.datasets.CIFAR10(root=data_dir,
                                                   train=False,
                                                   download=True,
                                                   transform=test_data_transform)
    else:
        # 使用 ImageFolder 按目录结构加载数据
        train_dataset = torchvision.datasets.ImageFolder(
            root=os.path.join(data_dir, "train"),
            transform=train_data_transform
        )
        test_dataset = torchvision.datasets.ImageFolder(
            root=os.path.join(data_dir, "val"),
            transform=test_data_transform
        )

    # 创建 DataLoader
    train_loader = DataLoader(train_dataset,
                            batch_size=batch_size,
                            shuffle=True,
                            drop_last=True)

    test_loader = DataLoader(test_dataset,
                           batch_size=batch_size,
                           shuffle=False,
                           drop_last=True)

    return train_loader, test_loader


def show_tensor_image(image):
    reverse_transforms = transforms.Compose([
        transforms.Lambda(lambda t: (t + 1) / 2),  # 将数据从[-1, 1]缩放到[0, 1]范围
        transforms.Lambda(lambda t: t.permute(1, 2, 0)),  # 将通道顺序从CHW改为HWC
        transforms.Lambda(lambda t: t * 255.),  # 将数据缩放到[0, 255]范围
        transforms.Lambda(lambda t: t.numpy().astype(np.uint8)),  # 将数据转换为uint8类型
        transforms.ToPILImage(),  # 将数据转换为PIL图像格式
    ])

    # 如果图像是批次数据,则取第一个图像
    if len(image.shape) == 4:
        image = image[0, :, :, :]
    return reverse_transforms(image)


if __name__ == "__main__":
    train_loader, test_loader = load_transformed_dataset()
    image, _ = next(iter(train_loader))
    plt.imshow(show_tensor_image(image))
    plt.show()

