package ButtonSweeper.tiles;

import ButtonSweeper.Tile;
import ButtonSweeper.Board;

public class MineTile extends Tile {
  public MineTile(Board board, int x, int y) {
    super(board, x, y);
  }

  @Override
  public boolean isMineTile() {
    return true;
  }
}
