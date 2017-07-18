public class Square {
  private final boolean isMine;
  private boolean isRevealed;
  private Flag flag;
  private int number;

  public Square(boolean isMine) {
    this.isMine = isMine;
    this.isRevealed = false;
    this.flag = Flag.EMPTY;
    this.number = 0;
  }

  public boolean isMineSquare() {
    return isMine;
  }

  public boolean isRevealedSquare() {
    return isRevealed;
  }

  public void reveal() {
    isRevealed = true;
  }

  public void setFlag(Flag flag) {
    this.flag = flag;
  }

  public Flag getFlag() {
    return flag;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public int getNumber() {
    return number;
  }
}
