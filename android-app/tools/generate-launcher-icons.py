"""Generate NFT Flicks adaptive + Play Store launcher icons from brand PNG."""
from __future__ import annotations

import os
from PIL import Image

SRC = r"C:\Users\User\Desktop\Website\nftflickbeta\images\nft-flicks-cast-icon-512.png"
RES = r"C:\Users\User\Desktop\Cursor\nft flicks beta\android-app\app\src\main\res"
DOCS = r"C:\Users\User\Desktop\Cursor\nft flicks beta\android-app\docs"
PLAY = r"C:\Users\User\Desktop\Cursor\nft flicks beta\android-app\app\src\main\play"

BG = (10, 10, 10, 255)  # #0A0A0A


def with_bg(im: Image.Image, size: int, pad_ratio: float = 0.0) -> Image.Image:
    canvas = Image.new("RGBA", (size, size), BG)
    content = size if pad_ratio <= 0 else int(size * (1 - pad_ratio))
    scaled = im.resize((content, content), Image.Resampling.LANCZOS)
    off = (size - content) // 2
    canvas.paste(scaled, (off, off), scaled)
    return canvas


def main() -> None:
    img = Image.open(SRC).convert("RGBA")

    legacy = {
        "mipmap-mdpi": 48,
        "mipmap-hdpi": 72,
        "mipmap-xhdpi": 96,
        "mipmap-xxhdpi": 144,
        "mipmap-xxxhdpi": 192,
    }
    foreground = {
        "mipmap-mdpi": 108,
        "mipmap-hdpi": 162,
        "mipmap-xhdpi": 216,
        "mipmap-xxhdpi": 324,
        "mipmap-xxxhdpi": 432,
    }

    for folder, size in legacy.items():
        d = os.path.join(RES, folder)
        os.makedirs(d, exist_ok=True)
        out = with_bg(img, size)
        out.save(os.path.join(d, "ic_launcher.png"), "PNG")
        out.save(os.path.join(d, "ic_launcher_round.png"), "PNG")
        print(f"legacy {folder}: {size}")

    for folder, size in foreground.items():
        d = os.path.join(RES, folder)
        os.makedirs(d, exist_ok=True)
        out = with_bg(img, size)
        out.save(os.path.join(d, "ic_launcher_foreground.png"), "PNG")
        print(f"fg {folder}: {size}")

    os.makedirs(DOCS, exist_ok=True)
    os.makedirs(PLAY, exist_ok=True)
    play_icon = with_bg(img, 512)
    play_icon.save(os.path.join(DOCS, "play-store-icon-512.png"), "PNG")
    play_icon.save(os.path.join(PLAY, "icon-512.png"), "PNG")
    print("play 512 written")
    print("done")


if __name__ == "__main__":
    main()
