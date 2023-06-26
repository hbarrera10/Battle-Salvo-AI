package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * test the UserVsAiBattleSalvoModel class
 */
class UserVsAiBattleSalvoModelTest {
  BattleSalvoModel model;

  @BeforeEach
  void setUp() {
    model = new UserVsAiBattleSalvoModel();
    model.initializeBoards(6, 6);

    assertEquals(0, model.getNumOfShots());

    HashMap<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    model.setupBoard(specifications);

    assertEquals(4, model.getNumOfShots());
  }

  @Test
  void testGetNumOfShots() {
    assertEquals(4, model.getNumOfShots());
  }

  @Test
  void validShot() {
    /*
    row <= player2Board.getHeight() && row >= 0 && col <= player2Board.getWidth() &&
           col >= 0 && !player2Board.alreadyShot(row, col)
     */
    assertFalse(model.validShot(-1, 0));
    assertFalse(model.validShot(1, -1));
    assertFalse(model.validShot(8, 0));
    assertFalse(model.validShot(0, 8));
    assertTrue(model.validShot(3, 4));

  }

  @Test
  void testMinBoardDimension() {
    assertEquals(6, model.minBoardDimension());
  }

  @Test
  void testWin() {
    List<Coord> wholeBoard = new ArrayList<>();
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        wholeBoard.add(new Coord(i, j));
      }
    }
    model.update(wholeBoard);

    assertTrue(model.isGameOver());
    assertEquals(model.getGameResult(), GameResult.WIN);
  }

  @Test
  void testDraw() {
    model = new UserVsAiBattleSalvoModel();
    model.initializeBoards(6, 6);

    assertEquals(0, model.getNumOfShots());

    HashMap<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 0);
    specifications.put(ShipType.DESTROYER, 0);
    specifications.put(ShipType.BATTLESHIP, 0);
    specifications.put(ShipType.SUBMARINE, 0);
    model.setupBoard(specifications);

    assertEquals(0, model.getNumOfShots());

    assertTrue(model.isGameOver());
    assertEquals(model.getGameResult(), GameResult.DRAW);
  }

  @Test
  void testGameResultException() {
    model = new UserVsAiBattleSalvoModel();
    model.initializeBoards(6, 6);

    assertEquals(0, model.getNumOfShots());

    HashMap<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 0);
    specifications.put(ShipType.DESTROYER, 0);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.SUBMARINE, 0);
    model.setupBoard(specifications);

    assertEquals(1, model.getNumOfShots());

    assertFalse(model.isGameOver());
    assertThrows(NoSuchElementException.class, () -> model.getGameResult());
  }



}