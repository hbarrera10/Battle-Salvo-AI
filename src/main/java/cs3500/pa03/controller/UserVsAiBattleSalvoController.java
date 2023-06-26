package cs3500.pa03.controller;

import cs3500.pa03.model.BattleSalvoModel;
import cs3500.pa03.view.UserInputBattleSalvoView;

/**
 * represents the Controller of the UserVsAiBattleSalvo Game
 */
public class UserVsAiBattleSalvoController extends BattleSalvoController {

  /**
   * constructor
   *
   * @param model represents the model of the MVC structure
   * @param view represents the view of the MVC structure
   */
  public UserVsAiBattleSalvoController(BattleSalvoModel model, UserInputBattleSalvoView view) {
    super(model, view);
  }


  /**
   * runs the game
   */
  @Override
  public void run() {
    view.displayMessage("Hello! Welcome to the OOD BattleSalvo Game!");

    BattleSalvoProcedureState state = new DimensionsState();
    state.handleInput(this, view.getUserInput(1, 2,
        state.generateDirections(this)));

    state = new FleetState();
    state.handleInput(this, view.getUserInput(1, 4,
        state.generateDirections(this)));

    state = new SalvoState();
    while (!model.isGameOver()) {
      view.displayMessage("AI Board:\n" + model.getPlayer2Board().toString()
                          + "Your Board:\n" + model.getPlayer1Board().toString());
      state.handleInput(this, view.getUserInput(model.getNumOfShots(), 2,
          state.generateDirections(this)));
    }

    view.displayEnd(model.getGameResult().toString());
  }

}
