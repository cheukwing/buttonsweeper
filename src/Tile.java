import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tile extends JButton {
  private boolean isMine;
  private boolean isRevealed;
  private Flag flag;
  private int number;

  public Tile(Board board, int x, int y) {
    this.isMine = false;
    this.isRevealed = false;
    this.flag = Flag.EMPTY;
    this.number = 0;

    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent mouseEvent) {
        if (!board.getHasGameEnded()) {
          if (SwingUtilities.isRightMouseButton(mouseEvent) && !isRevealed) {
            switch (flag) {
              case EMPTY: {
                flag = Flag.FLAGGED;
                break;
              }
              case FLAGGED: {
                flag = Flag.QUESTION_FLAGGED;
                break;
              }
              case QUESTION_FLAGGED: {
                flag = Flag.EMPTY;
                // fall-through
              }
              default:
            }
          } else if (!isRevealed && flag == Flag.EMPTY) {
            board.revealTile(x, y);
          }
        }
        board.updateTiles(board.getHasGameEnded());
      }
    });
  }

  public Flag getFlag() {
    return flag;
  }

  public void clearFlag() {
    flag = Flag.EMPTY;
  }

  public boolean isMineTile() {
    return isMine;
  }

  public void setMine() {
    isMine = true;
  }

  public void removeMine() {
    isMine = false;
  }

  public boolean isRevealedTile() {
    return isRevealed;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public int getNumber() {
    return number;
  }

  public void setRevealed() {
    isRevealed = true;
  }

  public void removeRevealed() {
    isRevealed = false;
  }

}
