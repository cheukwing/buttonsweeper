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

  public static final int INDEX_FLAG = 2;
  public static final int INDEX_Q_FLAG = 3;
  public static final int INDEX_T_BOMB = 6;
  public static final int INDEX_F_BOMB = 7;
  public static final int INDEX_BOMB = 5;
  public static final int INDEX_UNREVEALED = 0;
  public static final int INDEX_BLANK = 1;

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
    return (n == 0) ? sprites[INDEX_BLANK] : sprites[NUM_SPRITES_ROW + n - 1];
  }

  // PRE: is not empty flag
  public BufferedImage getFlagImg(Flag flag) {
    switch (flag) {
      case FLAGGED:
        return sprites[INDEX_FLAG];
      case QUESTION_FLAGGED:
        return sprites[INDEX_Q_FLAG];
      default:
        return null;
    }
  }

  public BufferedImage getTileImage(Tile tile, boolean revealBombs) {
    if (tile.isRevealedTile()) {
      if (tile.isMineTile()) {
        return sprites[INDEX_T_BOMB];
      }
      return getNumberImg(tile.getNumber());
    }

    if (revealBombs && tile.isMineTile()) {
      if (tile.getFlag() != Flag.EMPTY) {
        return sprites[INDEX_F_BOMB];
      }
      return sprites[INDEX_BOMB];
    }

    if (tile.getFlag() != Flag.EMPTY) {
      return getFlagImg(tile.getFlag());
    }
    return sprites[INDEX_UNREVEALED];
  }
}
