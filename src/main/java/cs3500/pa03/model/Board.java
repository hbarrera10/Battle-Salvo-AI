package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * represents a board in a game
 */
public class Board {
  private final List<List<Coord>> board = new ArrayList<>();

  private final List<Ship> listOfShips = new ArrayList<>();

  private int shotsPossible;

  private final boolean hidden;

  /**
   * constructor
   *
   * @param height of board
   * @param width of board
   * @param hidden aspect of board
   */
  public Board(int height, int width, boolean hidden) {
    for (int i = 0; i < height; i++) {
      List<Coord> row = new ArrayList<>();
      for (int j = 0; j < width; j++) {
        row.add(new Coord(i, j, hidden));
      }
      board.add(row);
    }
    this.shotsPossible = height * width;
    this.hidden = hidden;
  }

  /**
   * @param hidden determines how the board will appear
   */
  public Board(boolean hidden) {
    this.hidden = hidden;
  }

  /**
   * updates the possible shots
   *
   * @param listOfShots representing the inputted list of shots
   */
  public void update(List<Coord> listOfShots) {
    for (Coord c : listOfShots) {
      Coord hitCoord = board.get(c.getRow()).get(c.getCol());
      if (hitCoord.isShip()) {
        board.get(c.getRow()).get(c.getCol()).setState(Coord.CellState.HIT);
      } else {
        board.get(c.getRow()).get(c.getCol()).setState(Coord.CellState.MISS);
      }
      if (shotsPossible > 0) {
        shotsPossible--;
      }

    }
  }

  /**
   * reports the damage caused by these shots
   *
   * @param listOfShots representing all the shots
   * @return the shots that caused damage to ships
   */
  public List<Coord> damageShots(List<Coord> listOfShots) {
    List<Coord> shots = new ArrayList<>();
    for (Coord c : listOfShots) {
      Coord hitCoord = board.get(c.getRow()).get(c.getCol());
      if (hitCoord.isShip()) {
        shots.add(c);
      }
    }
    return shots;
  }

  /**
   * @return shotsPossible representing the number of shots that are possible on the board
   */
  public int getShotsPossible() {
    return shotsPossible;
  }

  /**
   * checks whether the proposed shot has already been shot before
   *
   * @param row of shot
   * @param col of shot
   * @return boolean representing whether the shot has already been performed before
   */
  public boolean alreadyShot(int row, int col) {
    return board.get(row).get(col).isShot();
  }

  /**
   * EFFECT: update the board with the initialized Ships
   *
   * @param specifications represents the info about how many of each ship there is
   * @param random represent the instance of the Random class
   * @return the list of ships that have been initialized and added to the board
   */
  public List<Ship> placeShips(Map<ShipType, Integer> specifications, Random random) {
    // places the biggest ships first
    List<Integer> numOfShips = new ArrayList<>();
    numOfShips.add(specifications.get(ShipType.CARRIER));
    numOfShips.add(specifications.get(ShipType.BATTLESHIP));
    numOfShips.add(specifications.get(ShipType.DESTROYER));
    numOfShips.add(specifications.get(ShipType.SUBMARINE));

    for (int k = 0; k < 4; k++) {
      int num = numOfShips.get(k);
      for (int i = 0; i < num; i++) {
        Ship ship = new Ship(ShipType.values()[k]);
        List<Coord> proposedSpace = generateShipLocation(ship, random);
        while (!isValidShipSpace(proposedSpace)) {
          proposedSpace = generateShipLocation(ship, random);
        }
        for (Coord c : proposedSpace) {
          board.get(c.getRow()).set(c.getCol(), c);
        }
        ship.setSpace(proposedSpace);
        listOfShips.add(ship);
      }
    }
    return listOfShips;
  }

  /**
   * checks whether the ship space is valid
   *
   * @param proposedSpace represents the space of a ship
   * @return boolean indicating validity of ship space
   */
  private boolean isValidShipSpace(List<Coord> proposedSpace) {
    for (Coord c : proposedSpace) {
      if (board.get(c.getRow()).get(c.getCol()).isShip()) {
        return false;
      }
    }
    return true;
  }

  /**
   * generates a ship location based on the given ship
   *
   * @param ship the given ship
   * @param random the given instance of Random
   * @return the proposed ship space
   */
  private List<Coord> generateShipLocation(Ship ship, Random random) {
    int size = ship.getType().getSize();
    List<Coord> proposedSpace = new ArrayList<>();

    boolean horizontal = random.nextBoolean();

    if (horizontal) {
      int randomRow = random.nextInt(getHeight());
      int randomCol = random.nextInt(getWidth() - size + 1);

      for (int j = 0; j < size; j++) {
        proposedSpace.add(new Coord(randomRow, randomCol, ship.getType(), hidden));
        randomCol++;
      }
      ship.setDirection("HORIZONTAL");
    } else {
      int randomRow = random.nextInt(getHeight() - size + 1);
      int randomCol = random.nextInt(getWidth());

      for (int j = 0; j < size; j++) {
        proposedSpace.add(new Coord(randomRow, randomCol, ship.getType(), hidden));
        randomRow++;
      }
      ship.setDirection("VERTICAL");
    }
    return proposedSpace;
  }

  /**
   * @return String representation of the board
   */
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (List<Coord> row : board) {
      for (Coord cell : row) {
        stringBuilder.append(" ").append(cell).append(" ");
      }
      stringBuilder.append("\n");
    }
    return stringBuilder.toString();
  }

  /**
   * @return the height of the board
   */
  public int getHeight() {
    return board.size();
  }

  /**
   * @return the width of the board
   */
  public int getWidth() {
    return board.get(0).size();
  }

  /**
   * @return the smaller dimension of the board
   */
  public int getSmallerDimension() {
    return Math.min(getWidth(), getHeight());
  }

  /**
   * @return the number of ships that haven't been sunk
   */
  public int getNumOfUnsunkShips() {
    int count = 0;
    for (Ship s : listOfShips) {
      if (!s.isSunk()) {
        count++;
      }
    }
    return count;
  }

  /**
   * @return boolean indicating whether the game is over
   */
  public boolean isGameOver() {
    return getNumOfUnsunkShips() == 0;
  }
}
