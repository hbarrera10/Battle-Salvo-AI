package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * test the AiPlayer class
 */
class AiPlayerTest {
  AiPlayer player;

  @BeforeEach
  void setUp() {
    HashMap<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.SUBMARINE, 1);

    Board playerBoard = new Board(6, 6, false);
    Random random = new Random(1);
    List<Ship> placedShips = playerBoard.placeShips(specifications, random);
    assertEquals(4, placedShips.size());

    assertSame(placedShips.get(0).getType(), ShipType.CARRIER);
    assertSame(placedShips.get(1).getType(), ShipType.BATTLESHIP);
    assertSame(placedShips.get(2).getType(), ShipType.DESTROYER);
    assertSame(placedShips.get(3).getType(), ShipType.SUBMARINE);
    player = new AiPlayer(playerBoard);
  }

  @Test
  void name() {
    assertEquals("Henry and Sasha", player.name());
  }

}