package cs3500.pa03.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * test the UserVsAiBattleSalvoView class
 */
class UserVsAiBattleSalvoViewTest {

  private UserVsAiBattleSalvoView view;
  private ByteArrayOutputStream outputStream;

  @BeforeEach
  void setUp() {
    view = new UserVsAiBattleSalvoView();
    outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
  }

  @AfterEach
  void tearDown() {
    System.setOut(System.out);
  }

  @Test
  void testDisplayEndWin() {
    view.displayEnd("WIN");
    String expectedWin = "CONGRATULATIONS! YOU WON!!!\n";
    assertEquals(expectedWin, outputStream.toString());
  }

  @Test
  void testDisplayEndLoss() {
    view.displayEnd("LOSS");
    String expectedLoss = "You lost... better luck next time!\n";
    assertEquals(expectedLoss, outputStream.toString());
  }

  @Test
  void testDisplayEndDraw() {
    view.displayEnd("DRAW");
    String expectedDraw = "You drew! Your ships and the AI's were all sunk at the same time.\n";
    assertEquals(expectedDraw, outputStream.toString());
  }
}