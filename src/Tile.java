import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tile extends JButton {
  private final Board board;
  private final int x;
  private final int y;
  private final boolean isMine;
  private boolean isRevealed;
  private Flag flag;
  private int number;

  public Tile(Board board, int x, int y, boolean isMine) {
    this.board = board;
    this.x = x;
    this.y = y;
    this.isMine = isMine;
    this.isRevealed = false;
    this.flag = Flag.EMPTY;
    this.number = 0;

    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent mouseEvent) {
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
        } else if (!isRevealed) {
          reveal();
        }
      }
    });
  }

  public boolean isMineTile() {
    return isMine;
  }

  public boolean isRevealedTile() {
    return isRevealed;
  }

  private void reveal() {
    isRevealed = true;

  }

  public void setNumber(int number) {
    this.number = number;
  }

  public int getNumber() {
    return number;
  }
}
