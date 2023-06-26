package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.List;

/**
 * represents a ship
 */
public class Ship {
  private final ShipType type;

  private List<Coord> space = new ArrayList<>();

  private String direction;

  /**
   * @param type of the ship
   */
  public Ship(ShipType type) {
    this.type = type;
  }

  /**
   * checks whether this ship is sunk
   *
   * @return boolean indicating whether this ship is sunk
   */
  public boolean isSunk() {
    for (Coord c : space) {
      if (!c.isHit()) {
        return false;
      }
    }
    return true;
  }

  /**
   * @return the type of this ship
   */
  public ShipType getType() {
    return type;
  }

  /**
   * @return the direction of this ship
   */
  public String getDirection() {
    return direction;
  }

  /**
   * @return starting coord of the ship
   */
  public Coord getStartingCoord() {
    if (space.isEmpty()) {
      throw new IllegalStateException("This ship does not take up any space.");
    }
    return space.get(0);
  }

  /**
   * sets the space of the ship
   *
   * @param proposedSpace of the ship
   */
  public void setSpace(List<Coord> proposedSpace) {
    space = proposedSpace;
  }

  /**
   * @param newDirection is the direction to be set for this ship
   */
  public void setDirection(String newDirection) {
    if (newDirection.equals("HORIZONTAL") || newDirection.equals("VERTICAL")) {
      direction = newDirection;
    } else {
      throw new IllegalArgumentException("The given direction is not valid.");
    }
  }
}
