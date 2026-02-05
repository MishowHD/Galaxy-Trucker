import os
from PIL import Image, ImageDraw, ImageFont

# cartelle origine e destinazione
SRC_DIR = "/home/lollod/Downloads/IS25-AM05-main/src/main/resources/it/polimi/ingsw/graphics"
DST_DIR = "/home/lollod/Downloads/IS25-AM05-main/src/main/resources/it/polimi/ingsw/graphicsFree"

PLACEHOLDER_BG = (200, 200, 200)
TEXT_COLOR = (120, 120, 120)

def create_placeholder(path, width, height, ext):
    img = Image.new("RGB", (width, height), PLACEHOLDER_BG)
    draw = ImageDraw.Draw(img)

    text = "PLACEHOLDER"
    try:
        font = ImageFont.truetype("DejaVuSans-Bold.ttf", size=min(width, height) // 10)
    except:
        font = ImageFont.load_default()

    bbox = draw.textbbox((0, 0), text, font=font)
    text_w = bbox[2] - bbox[0]
    text_h = bbox[3] - bbox[1]

    draw.text(
        ((width - text_w) / 2, (height - text_h) / 2),
        text,
        fill=TEXT_COLOR,
        font=font
    )

    # salva in base all'estensione originale
    if ext.lower() in [".jpg", ".jpeg"]:
        img.save(path, "JPEG")
    else:
        img.save(path, "PNG")

def process():
    for root, _, files in os.walk(SRC_DIR):
        rel_path = os.path.relpath(root, SRC_DIR)
        dst_root = os.path.join(DST_DIR, rel_path)
        os.makedirs(dst_root, exist_ok=True)

        for file in files:
            ext = os.path.splitext(file)[1]
            if ext.lower() not in [".png", ".jpg", ".jpeg"]:
                continue

            src_img_path = os.path.join(root, file)
            dst_img_path = os.path.join(dst_root, file)

            try:
                with Image.open(src_img_path) as img:
                    w, h = img.size
            except Exception as e:
                print(f"❌ Errore su {src_img_path}: {e}")
                continue

            create_placeholder(dst_img_path, w, h, ext)
            print(f"✅ {dst_img_path}")

if __name__ == "__main__":
    process()

