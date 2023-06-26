package cs3500.pa03.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.BattleSalvoModel;
import cs3500.pa03.model.UserVsAiBattleSalvoModel;
import cs3500.pa03.view.UserInputBattleSalvoView;
import cs3500.pa03.view.UserVsAiBattleSalvoView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * test the FleetState class
 */
class FleetStateTest {
  BattleSalvoProcedureState state;

  BattleSalvoController controller;
  ByteArrayOutputStream outputStream;
  InputStream originalIn;
  PrintStream originalOut;

  @BeforeEach
  public void setUpStreams() {
    // Redirect System.out to capture the output
    outputStream = new ByteArrayOutputStream();
    originalOut = System.out;
    originalIn = System.in;
    System.setOut(new PrintStream(outputStream));
  }


  @BeforeEach
  void setUp() {
    state = new DimensionsState();
    UserInputBattleSalvoView view = new UserVsAiBattleSalvoView();
    BattleSalvoModel model = new UserVsAiBattleSalvoModel();
    controller = new UserVsAiBattleSalvoController(model, view);
    int[][] validInput = new int[1][2];
    validInput[0][0] = 6;
    validInput[0][1] = 6;
    state.handleInput(controller, validInput);

    state = new FleetState();
  }

  @AfterEach
  void restoreSystemStreams() {
    System.setIn(System.in);
    System.setOut(System.out);
  }

  @Test
  void handleInputValid() {
    int[][] validInput = new int[1][4];
    validInput[0][0] = 1;
    validInput[0][1] = 1;
    validInput[0][2] = 1;
    validInput[0][3] = 1;

    state.handleInput(controller, validInput);

    String expected = "";

    assertEquals(expected, outputStream.toString());
  }

  @Test
  void handleInputInvalidTooBig() {
    int[][] invalidInput = new int[1][4];
    invalidInput[0][0] = 1;
    invalidInput[0][1] = 4;
    invalidInput[0][2] = 1;
    invalidInput[0][3] = 1;

    String input = "1 1 1 1\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    state.handleInput(controller, invalidInput);


    String expected = """
          Uh Oh! You've entered an invalid fleet size.
          Fleet size is too large.
          Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].
          Remember, your fleet must have at least one of each ship and may not exceed size 6.
          ---------------------------------------------------------------------------------------
          """;

    assertEquals(expected, outputStream.toString());
  }

  @Test
  void handleInputInvalidNoShip() {
    int[][] invalidInput = new int[1][4];
    invalidInput[0][0] = 1;
    invalidInput[0][1] = 0;
    invalidInput[0][2] = 1;
    invalidInput[0][3] = 1;

    String input = "1 1 1 1\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    state.handleInput(controller, invalidInput);


    String expected = """
          Uh Oh! You've entered an invalid fleet size.
          You must have at least ONE of each ship.
          Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].
          Remember, your fleet must have at least one of each ship and may not exceed size 6.
          ---------------------------------------------------------------------------------------
          """;

    assertEquals(expected, outputStream.toString());
  }

  @Test
  void generateDirections() {
    String expected = """
            Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].
            Remember, your fleet must have at least one of each ship and may not exceed size 6.
            ---------------------------------------------------------------------------------------
            """;
    assertEquals(expected, state.generateDirections(controller));
  }
}