import os
import shutil
import random
from tqdm import tqdm

def split_dataset(data_dir="./datasets/Animal-10", output_dir="./datasets/Animal-10-split",
                  train_ratio=0.8, seed=42):
    """
    随机划分 Animal-10 数据集为 train/val，并保存到新文件夹结构中
    data_dir: 原始数据路径，子文件夹为类别
    output_dir: 输出路径
    train_ratio: 训练集比例
    """
    random.seed(seed)

    # 创建输出文件夹
    for split in ["train", "val"]:
        split_dir = os.path.join(output_dir, split)
        os.makedirs(split_dir, exist_ok=True)

    # 遍历类别文件夹
    classes = [d for d in os.listdir(data_dir) if os.path.isdir(os.path.join(data_dir, d))]
    print(f"检测到类别: {classes}")

    for cls in classes:
        cls_dir = os.path.join(data_dir, cls)
        images = os.listdir(cls_dir)
        random.shuffle(images)

        total = len(images)
        train_end = int(total * train_ratio)

        splits = {
            "train": images[:train_end],
            "val": images[train_end:]
        }

        for split, split_images in splits.items():
            split_cls_dir = os.path.join(output_dir, split, cls)
            os.makedirs(split_cls_dir, exist_ok=True)

            for img in tqdm(split_images, desc=f"{cls}-{split}"):
                src = os.path.join(cls_dir, img)
                dst = os.path.join(split_cls_dir, img)
                shutil.copy(src, dst)

    print(f"数据划分完成，保存在 {output_dir} 中")


if __name__ == "__main__":
    split_dataset(data_dir="./datasets/Animals-10", output_dir="./datasets/Animals-10-split", train_ratio=0.8)
