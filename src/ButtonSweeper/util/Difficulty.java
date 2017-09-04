package ButtonSweeper.util;

public enum Difficulty {
  EASY(90),
  MEDIUM(85),
  HARD(80);

  private int probability;
  Difficulty(int probability) {
    this.probability = probability;
  }

  public int getProbability() {
    return probability;
  }
}
