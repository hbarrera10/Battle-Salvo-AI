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
 * test the DimensionsState class
 */
class DimensionsStateTest {
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
  }

  @AfterEach
  void restoreSystemStreams() {
    System.setIn(System.in);
    System.setOut(System.out);
  }

  @Test
  void testGenerateDirections() {
    String expected = """
        Please enter a valid height and width below:
        ------------------------------------------------------
        """;
    assertEquals(expected, state.generateDirections(controller));
  }

  @Test
  void testHandleInput() {
    int[][] validInput = new int[1][2];
    validInput[0][0] = 6;
    validInput[0][1] = 7;

    state.handleInput(controller, validInput);

    String expected = "";

    assertEquals(expected, outputStream.toString());
  }

  @Test
  void testHandleInvalidInput() {
    int[][] invalidInput = new int[1][2];
    invalidInput[0][0] = 6;
    invalidInput[0][1] = 4;

    String input = "7 6 s\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    state.handleInput(controller, invalidInput);

    String expected = """
          Uh Oh! You've entered invalid dimensions. Please remember that the height and width
          of the game must be in the range (6, 15), inclusive. Try again!
          
          Please enter a valid height and width below:
          ------------------------------------------------------
          """;

    assertEquals(expected, outputStream.toString());
  }

  @Test
  void testHandleInvalidInput2() {
    int[][] invalidInput = new int[1][2];
    invalidInput[0][0] = 6;
    invalidInput[0][1] = 16;

    String input = "7 6\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    state.handleInput(controller, invalidInput);

    String expected = """
          Uh Oh! You've entered invalid dimensions. Please remember that the height and width
          of the game must be in the range (6, 15), inclusive. Try again!
          
          Please enter a valid height and width below:
          ------------------------------------------------------
          """;

    assertEquals(expected, outputStream.toString());
  }

}