package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * test the Board class
 */
class BoardTest {
  Board sixBySix;

  @BeforeEach
  void setUp() {
    sixBySix = new Board(6, 6, false);
  }

  @Test
  void placeShips() {

    assertEquals(" .  .  .  .  .  . \n".repeat(6), sixBySix.toString());



    HashMap<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.SUBMARINE, 1);

    Random random = new Random(1);
    sixBySix.placeShips(specifications, random);

    String expectedShipLayout = """
         .  .  .  .  .  .\s
         B  B  B  B  B  .\s
         .  .  .  S  S  S\s
         D  D  D  D  .  .\s
         C  C  C  C  C  C\s
         .  .  .  .  .  .\s
        """;

    assertEquals(expectedShipLayout, sixBySix.toString());

    List<Coord> shots = new ArrayList<>();
    shots.add(new Coord(0, 0));
    shots.add(new Coord(1, 0));


    assertEquals(sixBySix.damageShots(shots).get(0).getRow(), 1);
    assertEquals(sixBySix.damageShots(shots).get(0).getCol(), 0);
    sixBySix.update(shots);

    String expectedShotLayout = """
         M  .  .  .  .  .\s
         H  B  B  B  B  .\s
         .  .  .  S  S  S\s
         D  D  D  D  .  .\s
         C  C  C  C  C  C\s
         .  .  .  .  .  .\s
        """;

    assertEquals(expectedShotLayout, sixBySix.toString());
    assertEquals(4, sixBySix.getNumOfUnsunkShips());

    List<Coord> moreShots = new ArrayList<>();
    moreShots.add(new Coord(2, 3));
    moreShots.add(new Coord(2, 4));
    moreShots.add(new Coord(2, 5));

    sixBySix.update(moreShots);

    String expectedRound2 = """
         M  .  .  .  .  .\s
         H  B  B  B  B  .\s
         .  .  .  H  H  H\s
         D  D  D  D  .  .\s
         C  C  C  C  C  C\s
         .  .  .  .  .  .\s
        """;

    assertEquals(expectedRound2, sixBySix.toString());
    assertEquals(3, sixBySix.getNumOfUnsunkShips());
    assertEquals(31, sixBySix.getShotsPossible());
    assertTrue(sixBySix.alreadyShot(2, 3));
    assertFalse(sixBySix.alreadyShot(2, 1));
    assertFalse(sixBySix.isGameOver());
  }

  @Test
  void getSmallerDimension() {
    assertEquals(6, sixBySix.getSmallerDimension());
  }

  @Test
  void isGameOver() {
    assertTrue(sixBySix.isGameOver());
  }
}