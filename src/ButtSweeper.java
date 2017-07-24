import javax.swing.*;

public class ButtSweeper {

  public static final int DEFAULT_SIZE = 5;

  private static String getInputHelper(String inputDialog, Object[] options) {
    Object input = JOptionPane.showInputDialog(null, inputDialog, "ButtSweeper",
        JOptionPane.PLAIN_MESSAGE, null, options, options == null ? "" : options[0]);
    if (input == null) {
      System.exit(0);
    }
    return (String) input;
  }

  private static void game(JFrame frame) {
    int width = DEFAULT_SIZE;
    int length = DEFAULT_SIZE;
    String input;

    try {
      input = getInputHelper("Enter the board width: ", null);
      width = Integer.parseInt(input);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(null, "Invalid input, defaulting to 5.");
    }

    try {
      input = getInputHelper("Enter the board length: ", null);
      length = Integer.parseInt(input);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(null, "Invalid input, defaulting to 5.");
    }

    Object[] difficulties = {"Easy", "Medium", "Hard"};
    input = getInputHelper("Choose difficulty: ", difficulties);

    Difficulty difficulty;
    switch (input) {
      case "Easy": {
        difficulty = Difficulty.EASY;
        break;
      }
      case "Medium": {
        difficulty = Difficulty.MEDIUM;
        break;
      }
      case "Hard": // fall-through
      default: {
        difficulty = Difficulty.HARD;
      }
    }

    new Board(frame, width, length, difficulty);
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("ButtSweeper");
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    game(frame);
  }
}
