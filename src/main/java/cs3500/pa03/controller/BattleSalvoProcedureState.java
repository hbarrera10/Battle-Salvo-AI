package cs3500.pa03.controller;

/**
 * represents the different state of a BattleSalvo game that are handled by the controller
 * This follows a state machine patter
 */
interface BattleSalvoProcedureState {
  /**
   * handles user input and deals with it with the help of the controller
   *
   * @param controller represents the main controller in charge of the MVC
   * @param input represents the input of a player
   */
  void handleInput(BattleSalvoController controller, int[][] input);

  /**
   * generates the necessary directions with the help of the controller
   *
   * @param controller represents the main controller in charge of the MVC
   * @return a String that represents the necessary directions
   */
  String generateDirections(BattleSalvoController controller);
}
