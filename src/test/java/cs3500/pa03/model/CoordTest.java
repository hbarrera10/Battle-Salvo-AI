package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * test the Coord class
 */
class CoordTest {
  Coord emptyCoord;
  Coord missCoord;
  Coord battleshipCoordHidden;

  Coord carrierCoordShown;


  @BeforeEach
  void setUp() {
    emptyCoord = new Coord(2, 3);
    missCoord = new Coord(6, 5, false);
    battleshipCoordHidden = new Coord(3, 4, ShipType.BATTLESHIP, true);
    carrierCoordShown = new Coord(3, 4, ShipType.CARRIER, false);
  }

  @Test
  void isHit() {
    assertFalse(emptyCoord.isHit());
    emptyCoord.setState(Coord.CellState.HIT);
    assertTrue(emptyCoord.isHit());
  }

  @Test
  void isShot() {
    assertFalse(battleshipCoordHidden.isShot());
    assertFalse(emptyCoord.isShot());
    emptyCoord.setState(Coord.CellState.MISS);
    battleshipCoordHidden.setState(Coord.CellState.HIT);
    assertTrue(battleshipCoordHidden.isShot());
    assertTrue(emptyCoord.isShot());
  }

  @Test
  void isShip() {
    assertFalse(emptyCoord.isShip());
    assertTrue(carrierCoordShown.isShip());
    assertTrue(battleshipCoordHidden.isShip());
  }

  @Test
  void getRow() {
    assertEquals(emptyCoord.getRow(), 2);
  }

  @Test
  void getCol() {
    assertEquals(emptyCoord.getCol(), 3);
  }

  @Test
  void testToString() {
    assertEquals(emptyCoord.toString(), ".");
    assertEquals(battleshipCoordHidden.toString(), ".");
    assertEquals(carrierCoordShown.toString(), "C");
    missCoord.setState(Coord.CellState.MISS);
    assertEquals(missCoord.toString(), "M");
    carrierCoordShown.setState(Coord.CellState.HIT);
    assertEquals(carrierCoordShown.toString(), "H");

  }
}