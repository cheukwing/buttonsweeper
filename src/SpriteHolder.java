import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteHolder {
  public static final int SPRITE_SIZE = 16;
  public static final int X_SPRITE_START = 2;
  public static final int Y_SPRITE_START = 53;
  public static final int NUM_SPRITES_ROW = 8;
  public static final int NUM_SPRITES_COL = 2;

  private final BufferedImage[] sprites;

  public SpriteHolder() throws IOException {
    sprites = new BufferedImage[NUM_SPRITES_COL * NUM_SPRITES_ROW];

    BufferedImage spriteSheet = ImageIO.read(new File("img/spritesheet.png"));
    for (int i = 0; i < NUM_SPRITES_ROW * NUM_SPRITES_COL; ++i) {
      sprites[i] = spriteSheet.getSubimage(
          X_SPRITE_START + (i % NUM_SPRITES_ROW) * (SPRITE_SIZE + 1),
          Y_SPRITE_START + ((i >= NUM_SPRITES_ROW) ? (1 + SPRITE_SIZE) : 0),
          SPRITE_SIZE,
          SPRITE_SIZE);
    }
  }

  public BufferedImage getNumberImg(int n) {
    return (n == 0) ? sprites[1] : sprites[NUM_SPRITES_ROW + n - 1];
  }

  // PRE: is not empty flag
  public BufferedImage getFlagImg(Flag flag) {
    switch (flag) {
      case FLAGGED: return sprites[2];
      case QUESTION_FLAGGED:return sprites[3];
      default: return null;
    }
  }

  public BufferedImage getTileImage(Tile tile, boolean revealBombs) {
    if (tile.isRevealedTile()) {
      if (tile.isMineTile()) {
        return sprites[6];
      }
      return getNumberImg(tile.getNumber());
    }

    if (revealBombs && tile.isMineTile()) {
      if (tile.getFlag() != Flag.EMPTY) {
        return sprites[7];
      }
      return sprites[5];
    }

    if (tile.getFlag() != Flag.EMPTY) {
      return getFlagImg(tile.getFlag());
    }
    return sprites[0];
  }
}
