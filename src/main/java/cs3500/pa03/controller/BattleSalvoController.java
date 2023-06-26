package cs3500.pa03.controller;

import cs3500.pa03.model.BattleSalvoModel;
import cs3500.pa03.view.UserInputBattleSalvoView;

/**
 * represents a BattleSalvo Game Controller
 */
public abstract class BattleSalvoController implements Controller {
  BattleSalvoModel model;

  UserInputBattleSalvoView view;

  /**
   * @param model represents the model that is a part of the MVC architecture
   * @param view represents the view that is a part of the MVC architecture
   */
  public BattleSalvoController(BattleSalvoModel model, UserInputBattleSalvoView view) {
    this.model = model;
    this.view = view;
  }
}
