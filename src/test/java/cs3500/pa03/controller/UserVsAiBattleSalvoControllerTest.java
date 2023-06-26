package cs3500.pa03.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.BattleSalvoModel;
import cs3500.pa03.model.Board;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.model.UserVsAiBattleSalvoModel;
import cs3500.pa03.view.UserInputBattleSalvoView;
import cs3500.pa03.view.UserVsAiBattleSalvoView;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserVsAiBattleSalvoControllerTest {
  BattleSalvoController controller;
  ByteArrayOutputStream outputStream;
  InputStream originalIn;
  PrintStream originalOut;

  @BeforeEach
  public void setup() {
    UserVsAiBattleSalvoModel model = new UserVsAiBattleSalvoModel();
    UserInputBattleSalvoView view = new UserVsAiBattleSalvoView();
    controller = new UserVsAiBattleSalvoController(model, view);
  }

  @BeforeEach
  public void setUpStreams() {
    // Redirect System.out to capture the output
    outputStream = new ByteArrayOutputStream();
    originalOut = System.out;
    originalIn = System.in;
    System.setOut(new PrintStream(outputStream));
  }

  @Test
  public void testRun() {
    // Redirect System.out to capture the output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    // Create the necessary dependencies (view, model) for the game
    ViewStub view = new ViewStub();
    ModelStub model = new ModelStub();

    // Create an instance of the game and call the run method
    BattleSalvoController controller1 = new UserVsAiBattleSalvoController(model, view);
    controller1.run();

    // Restore the original System.out
    System.setOut(System.out);

    // Define the expected output
    String expectedOutput = """
        Hello! Welcome to the OOD BattleSalvo Game!
        WIN
        """;

    // Compare the expected output with the captured output
    assertEquals(expectedOutput, outputStream.toString());
  }

  // Stub implementation of the View interface for testing
  private static class ViewStub extends UserInputBattleSalvoView {
    private int stateCounter = 0;

    @Override
    public void displayError(String message) {
      System.out.println(message);
    }

    public void displayMessage(String message) {
      System.out.println(message);
    }

    @Override
    public void displayEnd(String message) {
      System.out.println(message);
    }

    @Override
    public int[][] getUserInput(int numOfLines, int numOfIntsPerLine, String directions) {
      if (stateCounter == 0) {
        int[][] intArray = new int[1][2];
        intArray[0][0] = 6;
        intArray[0][1] = 6;
        stateCounter++;
        return intArray;
      } else if (stateCounter == 1) {
        int[][] intArray = new int[1][4];
        intArray[0][0] = 1;
        intArray[0][1] = 1;
        intArray[0][2] = 1;
        intArray[0][3] = 1;
        stateCounter++;
        return intArray;
      } else {
        return new int[0][];
      }

    }
  }

  // Stub implementation of the Model interface for testing
  private static class ModelStub extends BattleSalvoModel {
    @Override
    public Board getPlayer1Board() {
      // Return a stubbed player 1 board
      return new Board(true);
    }

    @Override
    public Board getPlayer2Board() {
      // Return a stubbed player 2 board
      return new Board(true);
    }

    @Override
    public void initializeBoards(int height, int width) {

    }

    @Override
    public int minBoardDimension() {
      return 6;
    }

    @Override
    public void setupBoard(Map<ShipType, Integer> fleetInfo) {

    }

    @Override
    public boolean isGameOver() {
      // Return a stubbed game over condition
      return true;
    }

    @Override
    public int getNumOfShots() {
      // Return a stubbed number of shots
      return 0;
    }

    @Override
    public GameResult getGameResult() {
      // Return a stubbed game result
      return GameResult.WIN;
    }

    @Override
    public boolean validShot(int row, int col) {
      return true;
    }

    @Override
    public void update(List<Coord> data) {

    }
  }


}