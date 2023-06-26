package cs3500.pa03.view;

/**
 * represents the view of the BattleSalvo game
 */
public abstract class UserInputBattleSalvoView implements View {

  /**
   * gets the user's input
   *
   * @param numOfLines representing the number of lines
   * @param numOfIntsPerLine representing the number of integers per line
   * @param directions representing the info needed for the user
   * @return a 2d array of ints
   */
  public abstract int[][] getUserInput(int numOfLines, int numOfIntsPerLine, String directions);
}
