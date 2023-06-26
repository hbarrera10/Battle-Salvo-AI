package cs3500.pa03.view;

import cs3500.pa03.controller.IntegerScannerReader;
import cs3500.pa03.controller.Reader;

/**
 * represents the View of the User vs Ai Battle Salvo game
 */
public class UserVsAiBattleSalvoView extends UserInputBattleSalvoView {
  /**
   * gets the user input
   *
   * @param numOfLines       representing the number of lines
   * @param numOfIntsPerLine representing the number of integers per line
   * @param directions       representing the info needed for the user
   * @return a 2d array of ints
   */
  public int[][] getUserInput(int numOfLines, int numOfIntsPerLine, String directions) {
    Reader<int[][]> reader = new IntegerScannerReader(numOfLines, numOfIntsPerLine, directions);
    return reader.read();
  }

  /**
   * displays an error to the console
   *
   * @param message to be displayed
   */
  @Override
  public void displayError(String message) {
    System.out.println(message);
  }

  /**
   * displays a message to the console
   *
   * @param message to be displayed
   */
  @Override
  public void displayMessage(String message) {
    System.out.println(message);
  }

  /**
   * displays the end of the game to the console
   *
   * @param message to be displayed depending on input
   */
  @Override
  public void displayEnd(String message) {
    if (message.equals("WIN")) {
      System.out.println("CONGRATULATIONS! YOU WON!!!");
    } else if (message.equals("LOSS")) {
      System.out.println("You lost... better luck next time!");
    } else {
      // draw
      System.out.println("You drew! Your ships and the AI's were all sunk at the same time.");
    }
  }
}
