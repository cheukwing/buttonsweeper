package ButtonSweeper;

import ButtonSweeper.tiles.MineTile;
import ButtonSweeper.tiles.NumberTile;
import ButtonSweeper.util.Difficulty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Board {
  private final ButtonSweeper buttonSweeper;
  private final SpriteHolder spriteHolder;
  private final JFrame frame;
  private final JPanel mineField;
  private final Tile[][] tiles;
  private final int width;
  private final int length;
  private int numRevealed;
  private int numMines;
  private boolean hasGameEnded;
  private boolean hasReset;

  public static final int EASY_PROBABILITY = 90;
  public static final int MEDIUM_PROBABILITY = 85;
  public static final int HARD_PROBABILITY = 80;

  public Board(ButtonSweeper buttonSweeper, int width, int length,
               Difficulty difficulty) {
    this.buttonSweeper = buttonSweeper;
    this.spriteHolder = buttonSweeper.getSpriteHolder();
    this.frame = buttonSweeper.getFrame();
    this.width = width;
    this.length = length;
    this.numRevealed = 0;
    this.tiles = new Tile[length][width];
    this.numMines = 0;
    this.hasGameEnded = false;
    this.hasReset = false;
    this.mineField = new JPanel(new GridLayout(length, width));

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

    setup(probability);
  }

  private void setup(int probability) {
    frame.getContentPane().removeAll();

    initialiseGame(probability);
    frame.add(mineField, BorderLayout.CENTER);

    JPanel options = new JPanel();
    options.setLayout(new GridLayout(0, 2));

    JButton resetButton = new JButton("Reset");
    resetButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent mouseEvent) {
        reset();
      }
    });
    options.add(resetButton);

    JButton newGameButton = new JButton("New Game");
    newGameButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent mouseEvent) {
        buttonSweeper.newGame();
      }
    });
    options.add(newGameButton);

    frame.add(options, BorderLayout.SOUTH);
    frame.pack();

    frame.setVisible(true);
  }

  private void initialiseGame(int probability) {
    // add tiles to array
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < length; y++) {
        tiles[y][x] = new NumberTile(this, x, y);
      }
    }


    // add mines randomly, do not allow an empty board unless 1x1 board
    Random random = new Random();
    do {
      addMines(random, probability);
    } while (numMines < 1 && width * length != 1);

    refreshPanel();
    // update tile numbers
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

  private void messageHelper(String message) {
    JOptionPane.showMessageDialog(null, message,
        "ButtonSweeper", JOptionPane.INFORMATION_MESSAGE);
  }

  private void end(boolean victory) {
    updateTiles(true);
    if (victory) {
      messageHelper("You Win!");
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

  private void firstRevealBombMove(int revealedX, int revealedY) {
    int x = 0;
    int y = 0;

    // move the bomb to top left tile, if already a mine, continue till free space
    while (y < length && tiles[y][x].isMineTile() || (y == revealedY && x == revealedX)) {
      if (x >= width - 1) {
        x = 0;
        ++y;
      } else {
        ++x;
      }
    }

    // if there is a free space, set it as a mine and update the numbers
    if (x < width && y < length) {
      tiles[y][x] = new MineTile(this, x, y);
      setTileNumbers();
    }

    refreshPanel();
  }

  private void refreshPanel() {
    mineField.removeAll();
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < length; ++y) {
        mineField.add(tiles[y][x]);
      }
    }
  }

  // PRE: x, y are in bounds
  public void revealTile(int x, int y) {
    Tile tile = tiles[y][x];
    tile.setRevealed();

    // do not allow the first reveal to be a mine
    if (numRevealed == 0 && tile.isMineTile() && !hasReset && length * width != 1) {
      tile = new NumberTile(this, x, y);
      tiles[y][x] = tile;
      tile.setRevealed();
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

  private void reset() {
    numRevealed = 0;
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < length; y++) {
        tiles[y][x].removeRevealed();
        tiles[y][x].clearFlag();
      }
    }
    hasReset = true;
    updateTiles(false);
    hasGameEnded = false;
  }
}
