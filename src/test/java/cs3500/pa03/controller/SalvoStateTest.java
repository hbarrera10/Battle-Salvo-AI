package cs3500.pa03.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
 * test the SalvoState
 */
class SalvoStateTest {
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

    int[][] validInput2 = new int[1][4];
    validInput2[0][0] = 1;
    validInput2[0][1] = 1;
    validInput2[0][2] = 1;
    validInput2[0][3] = 1;

    state.handleInput(controller, validInput2);

    state = new SalvoState();
  }

  @AfterEach
  void restoreSystemStreams() {
    System.setIn(System.in);
    System.setOut(System.out);
  }

  @Test
  void handleInput() {
    int[][] validInput = new int[4][2];
    validInput[0][0] = 1;
    validInput[0][1] = 1;
    validInput[1][0] = 1;
    validInput[1][1] = 2;
    validInput[2][0] = 1;
    validInput[2][1] = 3;
    validInput[3][0] = 1;
    validInput[3][1] = 4;


    state.handleInput(controller, validInput);
  }

  @Test
  void handleInvalidInput() {
    int[][] validInput = new int[4][2];
    validInput[0][0] = 1;
    validInput[0][1] = 1;
    validInput[1][0] = 1;
    validInput[1][1] = 2;
    validInput[2][0] = 1;
    validInput[2][1] = 3;
    validInput[3][0] = 1;
    validInput[3][1] = 0;

    String input = "1 1 1 2 1 3 1 4\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    state.handleInput(controller, validInput);

    String expected = """
            INVALID SHOTS INPUTTED.
            One of your shots is either beyond the dimensions of the board
            or has already been shot before.
            Please try again.
            """;

    assertTrue(outputStream.toString().contains(expected));
  }

  @Test
  void generateDirections() {
    String expected = """
          Each Shot is on a new line. 2 inputted shots should look like this:
          1 5 0 3
          In this case the first shot represents the 1st row and the 5th column.
          Please Enter 4 Shots:
          --------------------------------------------------------------------
          """;
    assertEquals(expected, state.generateDirections(controller));
  }
}