import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Random;

public class Board {
  private final JFrame frame;
  private final Tile[][] tiles;
  private final int width;
  private final int length;
  private int numRevealed;
  private int numMines;

  public static final int EASY_PROBABILITY = 90;
  public static final int MEDIUM_PROBABILITY = 80;
  public static final int HARD_PROBABILITY = 70;

  public Board(JFrame frame, int width, int length, Difficulty difficulty) {
    this.frame = frame;
    this.width = width;
    this.length = length;
    this.numRevealed = 0;
    this.tiles = new Tile[length][width];
    this.numMines = 0;

    int probability;
    switch (difficulty) {
      case EASY: {
        probability = EASY_PROBABILITY;
        break;
      }
      case MEDIUM: {
        probability = MEDIUM_PROBABILITY;
        break;
      }
      case HARD: // fall-through
      default: {
        probability = HARD_PROBABILITY;
      }
    }

    frame.getContentPane().removeAll();
    frame.setLayout(new GridLayout(length, width));
    initialiseTiles(frame, probability);
    frame.pack();
    frame.setVisible(true);
  }

  private void initialiseTiles(JFrame frame, int probability) {
    Random random = new Random();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < length; y++) {
        boolean isMine = random.nextInt(100) > probability;
        if (isMine) {
          ++numMines;
        }
        tiles[y][x] = new Tile(this, x, y, isMine);
        frame.add(tiles[y][x]);
      }
    }

    setTileNumbers();
    updateTiles(false);
  }

  private void setTileNumbers() {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < length; y++) {
        tiles[y][x].setNumber(getSurroundingMines(x, y));
      }
    }
  }

  private int getSurroundingMines(int x, int y) {
    int mines = 0;
    for (int i = Math.max(0, x - 1); i <= Math.min(width - 1, x + 1); i++) {
      for (int j = Math.max(0, y - 1); j <= Math.min(length - 1, y + 1); j++) {
        if (tiles[j][i].isMineTile()) {
          ++mines;
        }
      }
    }
    return mines;
  }

  private void incrementRevealed() {
    ++numRevealed;
  }

  private void revealSurroundings(int x, int y) {
    for (int i = Math.max(0, x - 1); i <= Math.min(width - 1, x + 1); i++) {
      for (int j = Math.max(0, y - 1); j <= Math.min(length - 1, y + 1); j++) {
        if (!tiles[j][i].isRevealedTile() && !tiles[j][i].isMineTile()) {
          revealTile(i, j);
        }
      }
    }
  }

  private boolean hasWon() {
    return numRevealed == length * width - numMines;
  }

  private void end() {
    updateTiles(true);
    if (hasWon()) {
      System.out.println("You win!");
    } else {
      System.out.println("You lose!");
    }
  }


  public void updateTiles(boolean revealBombs) {
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < width; j++) {
        Tile tile = tiles[i][j];
        Flag flag = tile.getFlag();
        if (flag != Flag.EMPTY) {
          if (flag == Flag.FLAGGED) {
            tile.setText("f");
          } else {
            tile.setText("?");
          }
        } else if (tile.isMineTile() && (revealBombs || tile.isRevealedTile())) {
          tile.setText("x");
        } else if (!tile.isRevealedTile()) {
          tile.setText(".");
        } else {
          tile.setText(Integer.toString(tile.getNumber()));
        }
      }
    }
    frame.pack();
  }

  private void firstRevealBombMove() {
    int x = 0;
    int y = 0;
    while (y < length && tiles[y][x].isMineTile()) {
      x = x >= width - 1 ? 0 : x + 1;
    }
    tiles[y][x].setMine();
    setTileNumbers();
  }

  public int getNumRevealed() {
    return numRevealed;
  }

  // PRE: x, y are in bounds
  public void revealTile(int x, int y) {
    Tile tile = tiles[y][x];
    tile.setRevealed();

    if (numRevealed == 0 && tile.isMineTile()) {
      firstRevealBombMove();
    }

    if (tile.getNumber() == 0) {
      revealSurroundings(x, y);
    }
    incrementRevealed();

    if (hasWon() || tile.isMineTile()) {
      end();
    }
  }
}
