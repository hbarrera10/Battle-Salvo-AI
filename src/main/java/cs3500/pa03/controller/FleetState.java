package cs3500.pa03.controller;

import cs3500.pa03.model.ShipType;
import java.util.HashMap;
import java.util.Map;

/**
 * represents the Fleet choosing stage of the Battle Salvo Game
 */
public class FleetState implements BattleSalvoProcedureState {


  /**
   * handles the input accordingly and updates the boards as needed with the fleet
   *
   * @param controller represents the main controller in charge of the MVC
   * @param input      represents the input of a player
   */
  @Override
  public void handleInput(BattleSalvoController controller, int[][] input) {
    boolean validInput = validFleet(controller, input);
    while (!validInput) {
      input = controller.view.getUserInput(1,
          4, generateDirections(controller));
      validInput = validFleet(controller, input);
    }

    Map<ShipType, Integer> fleetInfo = new HashMap<>();
    int index = 0;
    for (ShipType s : ShipType.values()) {
      fleetInfo.put(s, input[0][index]);
      index++;
    }
    controller.model.setupBoard(fleetInfo);
  }

  /**
   * checks whether the inputted fleet info is valid
   *
   * @param controller the main controller
   * @param input the user input
   * @return boolean representing whether the inputted Fleet info is valid
   */
  private boolean validFleet(BattleSalvoController controller, int[][] input) {
    int totalCount = 0;
    for (int numOfShips : input[0]) {
      if (numOfShips == 0) {
        controller.view.displayError("Uh Oh! You've entered an invalid fleet size.");
        controller.view.displayError("You must have at least ONE of each ship.");
        return false;
      } else {
        totalCount += numOfShips;
      }
    }
    if (totalCount > controller.model.minBoardDimension()) {
      controller.view.displayError("Uh Oh! You've entered an invalid fleet size.");
      controller.view.displayError("Fleet size is too large.");
      return false;
    } else {
      return true;
    }
  }


  /**
   * generates directions for fleet info
   *
   * @param controller represents the main controller in charge of the MVC
   * @return a String representation of the directions to prompt the user for fleet info
   */
  @Override
  public String generateDirections(BattleSalvoController controller) {
    String directions = """
            Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].
            Remember, your fleet must have at least one of each ship and may not exceed size {NUM}.
            ---------------------------------------------------------------------------------------
            """;
    return directions.replace("{NUM}",
        Integer.toString(controller.model.minBoardDimension()));
  }
}
