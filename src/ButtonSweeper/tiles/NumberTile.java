package ButtonSweeper.tiles;

import ButtonSweeper.Tile;
import ButtonSweeper.Board;

public class NumberTile extends Tile {
  public NumberTile(Board board, int x, int y) {
    super(board, x, y);
  }

  @Override
  public boolean isMineTile() {
    return false;
  }
}
