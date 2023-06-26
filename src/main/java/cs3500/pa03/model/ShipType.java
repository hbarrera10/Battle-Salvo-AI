package cs3500.pa03.model;

/**
 * represents the type of ship
 */
public enum ShipType {
  /**
   * represents a Carrier ship
   */
  CARRIER,
  /**
   * represents a Battleship ship
   */
  BATTLESHIP,
  /**
   * represents a Destroyer ship
   */
  DESTROYER,
  /**
   * represents a Submarine ship
   */
  SUBMARINE;

  /**
   * @return the size of a type of ship
   */
  public int getSize() {
    if (this == ShipType.CARRIER) {
      return 6;
    } else if (this == ShipType.BATTLESHIP) {
      return 5;
    } else if (this == ShipType.DESTROYER) {
      return 4;
    } else {
      return 3;
    }
  }

  /**
   * @return the String representation of a Ship
   */
  @Override
  public String toString() {
    if (this == ShipType.CARRIER) {
      return "C";
    } else if (this == ShipType.BATTLESHIP) {
      return "B";
    } else if (this == ShipType.DESTROYER) {
      return "D";
    } else  {
      return "S";
    }
  }
}
