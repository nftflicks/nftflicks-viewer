"""Generate NFT Flicks adaptive + Play Store launcher icons from brand PNG."""
from __future__ import annotations

import os
from pathlib import Path

from PIL import Image

ROOT = Path(__file__).resolve().parents[1]  # android-app/
# Optional brand source: set NFTFLICKS_ICON_SRC, or place icon at docs/source-icon-512.png
SRC = Path(
    os.environ.get(
        "NFTFLICKS_ICON_SRC",
        str(ROOT / "docs" / "source-icon-512.png"),
    )
)
RES = ROOT / "app" / "src" / "main" / "res"
DOCS = ROOT / "docs"
PLAY = ROOT / "app" / "src" / "main" / "play"

BG = (10, 10, 10, 255)  # #0A0A0A


def with_bg(im: Image.Image, size: int, pad_ratio: float = 0.0) -> Image.Image:
    canvas = Image.new("RGBA", (size, size), BG)
    content = size if pad_ratio <= 0 else int(size * (1 - pad_ratio))
    scaled = im.resize((content, content), Image.Resampling.LANCZOS)
    off = (size - content) // 2
    canvas.paste(scaled, (off, off), scaled)
    return canvas


def main() -> None:
    if not SRC.is_file():
        raise SystemExit(
            f"Missing icon source: {SRC}\n"
            "Set NFTFLICKS_ICON_SRC to a 512 PNG, or add docs/source-icon-512.png"
        )
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
        d = RES / folder
        d.mkdir(parents=True, exist_ok=True)
        out = with_bg(img, size)
        out.save(d / "ic_launcher.png", "PNG")
        out.save(d / "ic_launcher_round.png", "PNG")
        print(f"legacy {folder}: {size}")

    for folder, size in foreground.items():
        d = RES / folder
        d.mkdir(parents=True, exist_ok=True)
        out = with_bg(img, size)
        out.save(d / "ic_launcher_foreground.png", "PNG")
        print(f"fg {folder}: {size}")

    DOCS.mkdir(parents=True, exist_ok=True)
    PLAY.mkdir(parents=True, exist_ok=True)
    play_icon = with_bg(img, 512)
    play_icon.save(DOCS / "play-store-icon-512.png", "PNG")
    play_icon.save(PLAY / "icon-512.png", "PNG")
    print("play 512 written")
    print("done")


if __name__ == "__main__":
    main()
