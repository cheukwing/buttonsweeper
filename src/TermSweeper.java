import java.util.Scanner;

public class TermSweeper {

  private static boolean checkNumericString(String str) {
    int len = str.length();
    for (int i = 0; i < len; i++) {
      if (!Character.isDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  private static void game(Scanner sc) {
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

    Board board = new Board(width, length, difficulty);
    System.out.println();

    boolean isNecessaryToPrintBoard = true;
    while (!board.hasFinished()) {
      if (isNecessaryToPrintBoard) {
        // prints the board only if the previous move was possible.
        board.printBoard(false);
      }

      String[] tokens;
      do {
        System.out.println("Enter your move: ");
        tokens = sc.nextLine().split(" ");
      } while (!(tokens.length == 3 && Character.isAlphabetic(tokens[0].charAt(0))
          && checkNumericString(tokens[1]) && checkNumericString(tokens[2])));
      char c = tokens[0].charAt(0);
      int x = Integer.parseInt(tokens[1]);
      int y = Integer.parseInt(tokens[2]);

      switch (c) {
        case 'p': {
          isNecessaryToPrintBoard = board.play(x, y);
          break;
        }
        case 'f': {
          isNecessaryToPrintBoard = board.flag(x, y, Flag.FLAGGED);
          break;
        }
        case 'q': {
          isNecessaryToPrintBoard = board.flag(x, y, Flag.QUESTION_FLAGGED);
          break;
        }
        case 'c': {
          isNecessaryToPrintBoard = board.flag(x, y, Flag.EMPTY);
          break;
        }
        default: {
          // if unrecognised command, proceeds as if asking for move again.
          isNecessaryToPrintBoard = false;
        }
      }
    }

    board.printBoard(true);

    if (board.hasWon()) {
      System.out.println("You Win!");
    } else {
      System.out.println("You Lose!");
    }
  }

  public static void main(String[] args) {
    System.out.println("Welcome to TermSweeper!");
    System.out.println("To reveal a tile, type \"p <x> <y>\"");
    System.out.println("To flag, qFlag or clear type \"f/q/c <x> <y> \"");

    Scanner sc = new Scanner(System.in);
    String input;
    do {
      game(sc);
      System.out.println("Do you want to play again? <y>/<n>");
      input = sc.nextLine();
    } while (input.length() > 0 && Character.toLowerCase(input.charAt(0)) == 'y');
    sc.close();
  }
}
