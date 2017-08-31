package ButtonSweeper;

import ButtonSweeper.util.Difficulty;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ButtonSweeper {
  private final JFrame frame;
  private final SpriteHolder spriteHolder;

  private ButtonSweeper() throws IOException {
    this.frame = new JFrame("ButtonSweeper");
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.spriteHolder = new SpriteHolder();
  }

  public JFrame getFrame() {
    return frame;
  }

  public SpriteHolder getSpriteHolder() {
    return spriteHolder;
  }

  public void newGame() {
    JPanel optionBox = new JPanel();
    JTextField widthField = new JTextField();
    JTextField lengthField = new JTextField();
    String[] difficulties = {"Easy", "Medium", "Hard"};
    JComboBox<String> difficultySelector = new JComboBox<>(difficulties);
    optionBox.setLayout(new GridLayout(3, 2));

    optionBox.add(new JLabel("Width: "));
    optionBox.add(widthField);
    optionBox.add(new JLabel("Length: "));
    optionBox.add(lengthField);
    optionBox.add(new JLabel("Difficulty: "));
    optionBox.add(difficultySelector);

    boolean haveInput = false;
    while (!haveInput) {
      int result = JOptionPane.showConfirmDialog(null, optionBox, "New Game", JOptionPane.OK_CANCEL_OPTION);
      if (result == JOptionPane.OK_OPTION) {
        try {
          int width = Integer.parseInt(widthField.getText());
          int length = Integer.parseInt(lengthField.getText());
          Difficulty difficulty = Difficulty.values()[difficultySelector.getSelectedIndex()];
          haveInput = true;
          new Board(this, width, length, difficulty);
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog(null, "Invalid input!");
        }
      } else {
        haveInput = true;
      }
    }
  }

  public static void main(String[] args) {
    try {
      ButtonSweeper buttonSweeper = new ButtonSweeper();
      buttonSweeper.newGame();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("ERROR: Could not find spritesheet!");
    }
  }
}
