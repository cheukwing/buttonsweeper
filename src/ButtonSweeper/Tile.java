package ButtonSweeper;

import ButtonSweeper.util.Flag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class Tile extends JButton {
  private boolean isRevealed;
  private Flag flag;
  private int number;

  public Tile(Board board, int x, int y) {
    this.isRevealed = false;
    this.flag = Flag.EMPTY;
    this.number = 0;

    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent mouseEvent) {
        if (!board.getHasGameEnded()) {
          if (SwingUtilities.isRightMouseButton(mouseEvent) && !isRevealed) {
            flag = Flag.values()[(flag.ordinal() + 1) % Flag.values().length];
          } else if (!isRevealed && flag == Flag.EMPTY) {
            board.revealTile(x, y);
          }
        }
        board.updateTiles(board.getHasGameEnded());
      }
    });

    this.setBorder(BorderFactory.createEmptyBorder());
    this.setContentAreaFilled(false);
    this.setMargin(new Insets(0, 0, 0, 0));
  }

  public Flag getFlag() {
    return flag;
  }

  public void clearFlag() {
    flag = Flag.EMPTY;
  }

  public abstract boolean isMineTile();

  public boolean isRevealedTile() {
    return isRevealed;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public int getNumber() {
    return number;
  }

  public void setRevealed(boolean b) {
    isRevealed = b;
  }
}
