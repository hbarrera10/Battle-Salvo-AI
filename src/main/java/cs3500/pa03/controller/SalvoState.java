package cs3500.pa03.controller;

import cs3500.pa03.model.Coord;
import java.util.ArrayList;
import java.util.List;

/**
 * represents the main game state of Battle Salvo
 */
public class SalvoState implements BattleSalvoProcedureState {

  /**
   * handles input and updates the boards according to the user shots that are inputted
   *
   * @param controller represents the main controller in charge of the MVC
   * @param input      represents the user's proposed shots'
   */
  @Override
  public void handleInput(BattleSalvoController controller, int[][] input) {
    boolean validInput = validShots(controller, input);
    while (!validInput) {
      controller.view.displayError("""
            INVALID SHOTS INPUTTED.
            One of your shots is either beyond the dimensions of the board
            or has already been shot before.
            Please try again.
            """);
      controller.view.displayMessage("AI Board:\n" + controller.model.getPlayer2Board().toString()
                                     + "Your Board:\n"
                                     + controller.model.getPlayer1Board().toString());
      input = controller.view.getUserInput(controller.model.getNumOfShots(), 2,
          generateDirections(controller));
      validInput = validShots(controller, input);
    }
    List<Coord> userShots = inputToCoord(input);
    controller.model.update(userShots);

    displayRoundStats(controller);
  }

  /**
   * displays the stats of the round
   *
   * @param controller represents the main controller
   */
  private void displayRoundStats(BattleSalvoController controller) {
    StringBuilder message = new StringBuilder();
    List<Coord> userHitShots = controller.model.getUserHitShots();
    if (userHitShots.isEmpty()) {
      message.append("You did not hit any ships with your shots.");
    } else {
      message.append(userHitShots.size()).append(" of your shots hit AI ships:\n");
      for (Coord c : controller.model.getUserHitShots()) {
        message.append("{%d, %d}\n".formatted(c.getRow() + 1, c.getCol() + 1));
      }
    }
    controller.view.displayMessage(message.toString());

    List<Coord> aiHitShots = controller.model.getAiHitShots();
    if (aiHitShots.isEmpty()) {
      message = new StringBuilder("The AI missed all of its shots");
    } else {
      message = new StringBuilder(aiHitShots.size() + " of AI's shots hit your ships:\n");
      for (Coord c : controller.model.getAiHitShots()) {
        message.append("{%d, %d}\n".formatted(c.getRow() + 1, c.getCol() + 1));
      }
    }
    controller.view.displayMessage(message.toString());
  }

  /**
   * checks whether the inputted shots are valid
   *
   * @param controller represents the main controller
   * @param input represents the user's proposed shots
   * @return boolean representing whether the shots are valid or not
   */
  private boolean validShots(BattleSalvoController controller, int[][] input) {
    for (int[] shot : input) {
      if (!controller.model.validShot(shot[0] - 1, shot[1] - 1)) {
        return false;
      }
    }
    return true;
  }

  /**
   * turns the inputted shots into a List of Coord
   *
   * @param input inputted shots in raw form
   * @return List of Coord representing the shots inputted
   */
  private List<Coord> inputToCoord(int[][] input) {
    List<Coord> coords = new ArrayList<>();
    for (int[] ints : input) {
      coords.add(new Coord(ints[0] - 1, ints[1] - 1));
    }
    return coords;
  }


  /**
   * generate the proper directions
   *
   * @param controller represents the main controller in charge of the MVC
   * @return String representing the directions
   */
  @Override
  public String generateDirections(BattleSalvoController controller) {
    String directions = """
          Each Shot is on a new line. 2 inputted shots should look like this:
          1 5 0 3
          In this case the first shot represents the 1st row and the 5th column.
          Please Enter""";
    int shots = controller.model.getNumOfShots();
    if (shots == 1) {
      directions += " " + shots + " Shot:";
    } else {
      directions += " " + shots + " Shots:";
    }
    directions += "\n--------------------------------------------------------------------\n";
    return directions;
  }
}
