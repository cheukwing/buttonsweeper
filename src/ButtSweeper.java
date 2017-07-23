import javax.swing.*;
import java.util.Scanner;

public class ButtSweeper {

  private static boolean checkNumericString(String str) {
    int len = str.length();
    for (int i = 0; i < len; i++) {
      if (!Character.isDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  private static void game(JFrame frame, Scanner sc) {
    String input;
    do {
      System.out.println("Enter the board width: ");
      input = sc.nextLine();
    } while (input.length() < 1 || !checkNumericString(input));
    int width = Integer.parseInt(input);

    do {
      System.out.println("Enter the board length: ");
      input = sc.nextLine();
    } while (input.length() < 1 || !checkNumericString(input));
    int length = Integer.parseInt(input);

    do {
      System.out.println("Enter your difficulty: <E>asy <M>edium <H>ard");
      input = sc.nextLine();
    } while (input.length() != 1
        || (input.charAt(0) != 'E' && input.charAt(0) != 'M' && input.charAt(0) != 'H'));
    Difficulty difficulty;
    switch (input.charAt(0)) {
      case 'E': {
        difficulty = Difficulty.EASY;
        break;
      }
      case 'M': {
        difficulty = Difficulty.MEDIUM;
        break;
      }
      case 'H':
        // fall-through
      default:
        difficulty = Difficulty.HARD;
    }

    new Board(frame, width, length, difficulty);
  }

  public static void main(String[] args) {
    System.out.println("Welcome to ButtSweeper!");

    Scanner sc = new Scanner(System.in);
    JFrame frame = new JFrame("ButtSweeper");
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    game(frame, sc);
    sc.close();
  }
}
