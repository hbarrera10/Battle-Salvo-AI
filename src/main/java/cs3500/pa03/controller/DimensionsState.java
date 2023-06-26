package cs3500.pa03.controller;


/**
 * represents the dimension state of the BattleSalvoGame
 */
public class DimensionsState implements BattleSalvoProcedureState {

  /**
   * handles user input and initializes the boards of the Battle Salvo game
   *
   * @param controller represents the main controller
   * @param input represents the user input (dimensions of the game)
   */
  @Override
  public void handleInput(BattleSalvoController controller, int[][] input) {
    boolean validInput = validBoardDimensions(input);
    while (!validInput) {
      controller.view.displayError("""
          Uh Oh! You've entered invalid dimensions. Please remember that the height and width
          of the game must be in the range (6, 15), inclusive. Try again!
          """);
      input = controller.view.getUserInput(1, 2,
          generateDirections(controller));
      validInput = validBoardDimensions(input);
    }
    controller.model.initializeBoards(input[0][0], input[0][1]);
  }

  /**
   * checks whether the inputted board dimensions are valid (6 to 15)
   *
   * @param input user input
   * @return a boolean representing whether the inputted dimensions are valid
   */
  private boolean validBoardDimensions(int[][] input) {
    for (int dim : input[0]) {
      if (dim > 15 || dim < 6) {
        return false;
      }
    }
    return true;
  }

  /**
   * generates the directions prompting the user to input dimensions
   *
   * @param controller represents the main controller in charge of the MVC
   * @return returns the directions to input the dimensions
   */
  @Override
  public String generateDirections(BattleSalvoController controller) {
    return """
        Please enter a valid height and width below:
        ------------------------------------------------------
        """;
  }
}
