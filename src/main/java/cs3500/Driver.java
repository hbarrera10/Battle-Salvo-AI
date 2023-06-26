package cs3500;

import cs3500.pa03.controller.BattleSalvoController;
import cs3500.pa03.controller.Controller;
import cs3500.pa03.controller.UserVsAiBattleSalvoController;
import cs3500.pa03.model.AiPlayer;
import cs3500.pa03.model.BattleSalvoModel;
import cs3500.pa03.model.Player;
import cs3500.pa03.model.UserVsAiBattleSalvoModel;
import cs3500.pa03.view.UserInputBattleSalvoView;
import cs3500.pa03.view.UserVsAiBattleSalvoView;
import cs3500.pa04.controller.ProxyController;
import java.io.IOException;
import java.net.Socket;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * runs the client with the given host and port
   *
   * @param host in String form
   * @param port in int form
   * @throws IOException issue with the client or connecting
   * @throws IllegalStateException if the program ever reaches a state that can't be dealt with
   */
  private static void runClient(String host, int port)
      throws IOException, IllegalStateException {
    Socket server = new Socket(host, port);
    Player aiPlayer = new AiPlayer();
    Controller controller = new ProxyController(server, aiPlayer);
    controller.run();
  }

  /**
   * Either single player or Client based game is run. No arguments will run single player while
   * 2 arguments given a host and port as parameters, runs the
   * client. If there is an issue with the client or connecting,
   * an error message will be printed.
   *
   * @param args The expected parameters are the server's host and port
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      UserInputBattleSalvoView view = new UserVsAiBattleSalvoView();
      BattleSalvoModel model = new UserVsAiBattleSalvoModel();
      BattleSalvoController controller = new UserVsAiBattleSalvoController(model, view);
      controller.run();
    } else if (args.length == 2) {
      String host = args[0];
      // 0.0.0.0 35001
      int port = Integer.parseInt(args[1]);
      try {
        Driver.runClient(host, port);
      } catch (IOException e) {
        throw new IllegalStateException("IOException has been reached from running the client");
      }
    } else {
      throw new IllegalArgumentException("Invalid arguments given. You can either submit a host "
          + "and a port or no arguments");
    }

  }
}