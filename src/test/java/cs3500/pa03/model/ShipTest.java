package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * test the Ship class
 */
class ShipTest {
  Ship battleship;
  Ship carrier;
  Ship submarine;
  Ship destroyer;

  @BeforeEach
  void setUp() {
    battleship = new Ship(ShipType.BATTLESHIP);
    carrier = new Ship(ShipType.CARRIER);
    submarine = new Ship(ShipType.SUBMARINE);
    destroyer = new Ship(ShipType.DESTROYER);
  }

  @Test
  void isSunk() {
    // a ship that doesn't exist is technically sunk in my interpretation
    assertTrue(submarine.isSunk());
    List<Coord> shipSpaceWithOneHit = new ArrayList<>();
    shipSpaceWithOneHit.add(new Coord(1, 1));
    Coord hitCoord = new Coord(1, 2);
    hitCoord.setState(Coord.CellState.HIT);
    shipSpaceWithOneHit.add(hitCoord);
    shipSpaceWithOneHit.add(new Coord(1, 3));
    submarine.setSpace(shipSpaceWithOneHit);

    assertFalse(submarine.isSunk());

    Coord hitCoord1 = new Coord(1, 1);
    Coord hitCoord2 = new Coord(1, 2);
    Coord hitCoord3 = new Coord(1, 3);
    hitCoord1.setState(Coord.CellState.HIT);
    hitCoord2.setState(Coord.CellState.HIT);
    hitCoord3.setState(Coord.CellState.HIT);

    List<Coord> shipSpaceWithAllHit = new ArrayList<>();
    shipSpaceWithAllHit.add(hitCoord1);
    shipSpaceWithAllHit.add(hitCoord2);
    shipSpaceWithAllHit.add(hitCoord3);
    submarine.setSpace(shipSpaceWithAllHit);
    assertTrue(submarine.isSunk());
  }

  @Test
  void sanityCheck() {
    assertEquals(submarine.getType().getSize(), 3);
    assertEquals(destroyer.getType().getSize(), 4);
    assertEquals(battleship.getType().getSize(), 5);
    assertEquals(carrier.getType().getSize(), 6);

    assertEquals(submarine.getType().toString(), "S");
    assertEquals(destroyer.getType().toString(), "D");
    assertEquals(battleship.getType().toString(), "B");
    assertEquals(carrier.getType().toString(), "C");
  }
}