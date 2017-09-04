package ButtonSweeper;

import ButtonSweeper.tiles.MineTile;
import ButtonSweeper.tiles.NumberTile;
import ButtonSweeper.util.Difficulty;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Board extends JPanel {
  private final SpriteHolder spriteHolder;
  private final Tile[][] tiles;
  private final int width;
  private final int length;
  private int numRevealed;
  private int numMines;
  private boolean hasGameEnded;
  private boolean hasReset;
  private long timeStart;

  public Board(int width, int length, Difficulty difficulty, SpriteHolder spriteHolder) {
    this.spriteHolder = spriteHolder;
    this.width = width;
    this.length = length;
    this.numRevealed = 0;
    this.tiles = new Tile[length][width];
    this.numMines = 0;
    this.hasGameEnded = false;
    this.hasReset = false;

    setLayout(new GridLayout(length, width));
    initialiseGame(difficulty);

    this.timeStart = System.currentTimeMillis();
  }

  private void initialiseGame(Difficulty difficulty) {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < length; y++) {
        tiles[y][x] = new NumberTile(this, x, y);
      }
    }

    int probability = difficulty.getProbability();
    // add mines randomly, do not allow an empty board unless 1x1 board
    Random random = new Random();
    do {
      addMines(random, probability);
    } while (numMines < 1 && width * length != 1);

    setTileNumbers();
    updateTiles(false);
  }

  private void addMines(Random random, int probability) {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < length; y++) {
        if (random.nextInt(100) > probability) {
          tiles[y][x] = new MineTile(this, x, y);
          ++numMines;
        }
      }
    }
    refreshPanel();
  }

  private void setTileNumbers() {
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < length; ++y) {
        tiles[y][x].setNumber(getSurroundingMines(x, y));
      }
    }
  }

  private int getSurroundingMines(int x, int y) {
    int mines = 0;
    for (int i = Math.max(0, x - 1); i <= Math.min(width - 1, x + 1); ++i) {
      for (int j = Math.max(0, y - 1); j <= Math.min(length - 1, y + 1); ++j) {
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
    for (int i = Math.max(0, x - 1); i <= Math.min(width - 1, x + 1); ++i) {
      for (int j = Math.max(0, y - 1); j <= Math.min(length - 1, y + 1); ++j) {
        if (!tiles[j][i].isRevealedTile() && !tiles[j][i].isMineTile()) {
          revealTile(i, j);
        }
      }
    }
  }

  private boolean hasWon() {
    return numRevealed == length * width - numMines;
  }

  private void messageHelper(String message) {
    JOptionPane.showMessageDialog(null, message,
        "ButtonSweeper", JOptionPane.INFORMATION_MESSAGE);
  }

  private void end(boolean victory) {
    updateTiles(true);
    if (victory) {
      messageHelper("You Win!\nTime: "
          + ((System.currentTimeMillis() - timeStart) / 1000.0));
    } else {
      messageHelper("You Lose!");
    }
    hasGameEnded = true;
  }

  public boolean getHasGameEnded() {
    return hasGameEnded;
  }


  public void updateTiles(boolean revealBombs) {
    for (int i = 0; i < length; ++i) {
      for (int j = 0; j < width; ++j) {
        tiles[i][j].setIcon(
            new ImageIcon(
                spriteHolder
                    .getTileImage(tiles[i][j], revealBombs)));
      }
    }
  }

  private void firstRevealBombMove(int x, int y) {
    for (int i = 0; i < width; ++i) {
      for (int j = 0; j < length; ++j) {
        if (!tiles[j][i].isMineTile() && (i != x || j != y)) {
          tiles[j][i] = new MineTile(this, i, j);
          setTileNumbers();
          refreshPanel();
          return;
        }
      }
    }
  }

  private void refreshPanel() {
    removeAll();
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < length; ++y) {
        add(tiles[y][x]);
      }
    }
  }

  // PRE: x, y are in bounds
  public void revealTile(int x, int y) {
    Tile tile = tiles[y][x];
    tile.setRevealed(true);

    // do not allow the first reveal to be a mine
    if (numRevealed == 0 && tile.isMineTile() && !hasReset && length * width != 1) {
      tile = new NumberTile(this, x, y);
      tiles[y][x] = tile;
      tile.setRevealed(true);
      firstRevealBombMove(x, y);
    }

    if (tile.getNumber() == 0) {
      revealSurroundings(x, y);
    }
    incrementRevealed();

    if (hasWon() || tile.isMineTile()) {
      end(!tile.isMineTile());
    }
  }

  public void reset() {
    numRevealed = 0;
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < length; ++y) {
        tiles[y][x].setRevealed(false);
        tiles[y][x].clearFlag();
      }
    }
    hasReset = true;
    updateTiles(false);
    hasGameEnded = false;
    timeStart = System.currentTimeMillis();
  }
}
