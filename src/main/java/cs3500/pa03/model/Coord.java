package cs3500.pa03.model;

/**
 * represents a Coord
 */
public class Coord {
  private final int row;
  private final int col;
  CellState state;

  ShipType shipType;

  boolean hidden;

  /**
   * represents the state of this "cell"
   */
  public enum CellState {
    /**
     * represents an empty cell
     */
    EMPTY,
    /**
     * represents a cell with a ship
     */
    SHIP,
    /**
     * represents a ship cell that has been shot
     */
    HIT,
    /**
     * represents an empty cell that has been shot
     */
    MISS
  }

  /**
   * @param row of coordinate
   * @param col of coordinate
   * @param shipType of ship within the "cell"
   * @param hidden whether the String output is meant for user or opponent
   */
  public Coord(int row, int col, ShipType shipType, boolean hidden) {
    this.row = row;
    this.col = col;
    this.state = CellState.SHIP;
    this.shipType = shipType;
    this.hidden = hidden;
  }

  /**
   * default EMPTY and not hidden
   *
   * @param row of coordinate
   * @param col of coordinate
   */
  public Coord(int row, int col) {
    this.row = row;
    this.col = col;
    this.state = CellState.EMPTY;
    this.shipType = null;
    this.hidden = false;
  }

  /**
   * default EMPTY
   *
   * @param row of coordinate
   * @param col of coordinate
   * @param hidden whether the String output is meant for user or opponent
   */
  public Coord(int row, int col, boolean hidden) {
    this.row = row;
    this.col = col;
    this.state = CellState.EMPTY;
    this.shipType = null;
    this.hidden = hidden;
  }

  /**
   * @return boolean determining whether the cell has a ship that is hit
   */
  public boolean isHit() {
    return state.equals(CellState.HIT);
  }

  /**
   * @return boolean determining whether the cell has been shot
   */
  public boolean isShot() {
    return isHit() || state.equals(CellState.MISS);
  }

  /**
   * @return boolean determining whether the cell has a ship
   */
  public boolean isShip() {
    return state.equals(CellState.SHIP);
  }

  /**
   * @return the row of this Coord
   */
  public int getRow() {
    return row;
  }

  /**
   * @return the col of this Coord
   */
  public int getCol() {
    return col;
  }

  /**
   * @param newState to be set to the field this.state
   */
  public void setState(CellState newState) {
    state = newState;
  }

  /**
   * produces the string representation of this Coord along with the Ship if there is one
   *
   * @return string representation of this Coord
   */
  @Override
  public String toString() {
    if (state == CellState.MISS) {
      return "M";
    } else if (state == CellState.SHIP) {
      if (hidden) {
        return ".";
      } else {
        return shipType.toString();
      }
    } else if (state == CellState.HIT) {
      return "H";
    } else {
      return ".";
    }
  }

  /**
   * check the row and col values within each coordinate
   *
   * @param obj some Object
   * @return whether the two coordinates are the same
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Coord coord) {
      return (coord.getRow() == getRow() && coord.getCol() == getCol());
    } else {
      return false;
    }
  }

  /**
   * @return unique hashCode for this
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + getRow();
    result = prime * result + getCol();
    return result;
  }
}
