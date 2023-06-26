package cs3500.pa03.view;

/**
 * represents the view of the MVC architecture
 */
public interface View {
  /**
   * display the given error message
   *
   * @param message to be displayed
   */
  void displayError(String message);

  /**
   * displays the given message
   *
   * @param message to be displayed
   */
  void displayMessage(String message);

  /**
   * displays the end
   *
   * @param message to be displayed
   */
  void displayEnd(String message);
}
