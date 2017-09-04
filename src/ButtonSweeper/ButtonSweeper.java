package ButtonSweeper;

import ButtonSweeper.util.MouseButton;
import ButtonSweeper.util.Difficulty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class ButtonSweeper extends JFrame {
  private final SpriteHolder spriteHolder;
  private final MouseButton resetButton;
  private Board board;

  private ButtonSweeper() throws IOException {
    super("ButtonSweeper");
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.spriteHolder = new SpriteHolder();

    JPanel options = new JPanel();
    options.setLayout(new GridLayout(0, 2));

    this.resetButton = new MouseButton("Reset", new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent mouseEvent) {}
    });
    options.add(resetButton);

    MouseButton newGameButton = new MouseButton("New Game", new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent mouseEvent) {
        newGame();
      }
    });
    options.add(newGameButton);

    this.add(options, BorderLayout.SOUTH);
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
          if (board != null) {
            this.remove(board);
          }
          board = new Board(width, length, difficulty, spriteHolder);
          resetButton.replaceMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
              board.reset();
            }
          });
          this.add(board, BorderLayout.NORTH);
          this.pack();
          this.setVisible(true);
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
