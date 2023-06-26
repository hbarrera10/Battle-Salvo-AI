package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * represents an AI player
 */
public class AiPlayer implements Player {
  private Board board;
  private List<Coord> possibleShots;
  private int[][] heatMap;

  private int[][] shotMap;
  private Map<ShipType, Integer> fleetSpecs;

  private List<Coord> prevRoundShots;

  /**
   * @param board associated with the AI player
   */
  public AiPlayer(Board board) {
    this.board = board;
    this.possibleShots = new ArrayList<>();
    this.prevRoundShots = new ArrayList<>();
  }

  /**
   * base constructor of the AI Player
   */
  public AiPlayer() {
    this.possibleShots = new ArrayList<>();
    this.prevRoundShots = new ArrayList<>();
  }

  /**
   * Get the player's name. It is our GitHub Classroom's team name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return "Henry and Sasha";
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board. Generates the heat map for taking shots.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    this.fleetSpecs = specifications;
    this.shotMap = new int[height][width];
    if (board == null) {
      board = new Board(height, width, false);
    }
    // the heat map needs to be initialized with values that aren't 0
    // they should be specific to the fleet specifications
    generateHeatMap(height, width);
    possibleShots = new ArrayList<>();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        possibleShots.add(new Coord(i, j));
      }
    }
    Random random = new Random();
    return board.placeShips(specifications, random);
  }

  /**
   * generates the heat map used by the player to make the "most" optimal shots
   *
   * @param height of the board
   * @param width of the board
   */
  private void generateHeatMap(int height, int width) {
    // the first step is to iterate through each specification entry and check where that ship will
    // fit on the board
    heatMap = new int[height][width];
    for (Map.Entry<ShipType, Integer> entry : fleetSpecs.entrySet()) {
      int size = entry.getKey().getSize();
      int numOfShips = entry.getValue();
      for (int i = 0; i < numOfShips; i++) {
        for (int row = 0; row < height; row++) {
          for (int col = 0; col < width; col++) {
            // when placing the endpoints the first coord is the left/top most starting point
            // if proposed space doesn't have a shot then we add 1 to all cells in the prob map
            if (row - size >= 0) {
              if (ifSpaceDoesNotHaveMiss(new Coord(row - size, col), new Coord(row, col),
                  false)) {
                for (int updateRow = row - size; updateRow <= row; updateRow++) {
                  heatMap[updateRow][col] += 1;
                }
                int numOfHitsInSpace = getNumOfHitsInSpace(new Coord(row - size, col),
                    new Coord(row, col), false);
                for (int updateRow = row - size; updateRow <= row; updateRow++) {
                  heatMap[updateRow][col] += numOfHitsInSpace;
                }
              }
            }
            if (row + size < height) {
              if (ifSpaceDoesNotHaveMiss(new Coord(row, col), new Coord(row + size, col),
                  false)) {
                for (int updateRow = row; updateRow <= row + size; updateRow++) {
                  heatMap[updateRow][col] += 1;
                }
                int numOfHitsInSpace = getNumOfHitsInSpace(new Coord(row, col),
                    new Coord(row + size, col), false);
                for (int updateRow = row; updateRow <= row + size; updateRow++) {
                  heatMap[updateRow][col] += numOfHitsInSpace;
                }
              }
            }
            if (col - size >= 0) {
              if (ifSpaceDoesNotHaveMiss(new Coord(row, col - size), new Coord(row, col),
                  true)) {
                for (int updateCol = col - size; updateCol <= col; updateCol++) {
                  heatMap[row][updateCol] += 1;
                }
                int numOfHitsInSpace = getNumOfHitsInSpace(new Coord(row, col - size),
                    new Coord(row, col), true);
                for (int updateCol = col - size; updateCol <= col; updateCol++) {
                  heatMap[row][updateCol] += numOfHitsInSpace;
                }
              }
            }
            if (col + size < width) {
              if (ifSpaceDoesNotHaveMiss(new Coord(row, col), new Coord(row, col + size),
                  true)) {
                for (int updateCol = col; updateCol <= col + size; updateCol++) {
                  heatMap[row][updateCol] += 1;
                }
                int numOfHitsInSpace = getNumOfHitsInSpace(new Coord(row, col),
                    new Coord(row, col + size), true);
                for (int updateCol = col; updateCol <= col + size; updateCol++) {
                  heatMap[row][updateCol] += numOfHitsInSpace;
                }
              }
            }
          }
        }
      }
    }
  }

  /**
   * check whether the proposed space for a ship has a MISS already. Misses are represented as the
   * int 2 in the shotMap
   *
   * @param starting point of the ship
   * @param ending point of the ship
   * @param horizontal representing the orientation of the ship
   *
   * @return whether there is a miss in the proposed ship space
   */
  private boolean ifSpaceDoesNotHaveMiss(Coord starting, Coord ending, boolean horizontal) {
    if (horizontal) {
      for (int col = starting.getCol(); col <= ending.getCol(); col++) {
        if (shotMap[starting.getRow()][col] == 2) {
          return false;
        }
      }
    } else {
      for (int row = starting.getRow(); row <= ending.getRow(); row++) {
        if (shotMap[row][starting.getCol()] == 2) {
          return false;
        }
      }
    }
    return true;
  }


  /**
   * @param starting point of the ship
   * @param ending point of the ship
   * @param horizontal representing the orientation of the ship
   *
   * @return the number of hits that are present within the proposed space
   */
  private int getNumOfHitsInSpace(Coord starting, Coord ending, boolean horizontal) {
    int count = 0;
    if (horizontal) {
      for (int col = starting.getCol(); col <= ending.getCol(); col++) {
        if (shotMap[starting.getRow()][col] == 1) {
          count++;
        }
      }
    } else {
      for (int row = starting.getRow(); row <= ending.getRow(); row++) {
        if (shotMap[row][starting.getCol()] == 1) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    List<Coord> takenShots = generateShots();
    prevRoundShots = takenShots;
    return takenShots;
  }

  /**
   * generates shots using a heatmap algorithm
   *
   * @return the list of shots in Coord form
   */
  List<Coord> generateShots() {
    List<Coord> shots = new ArrayList<>();
    int numOfShots = Math.min(board.getNumOfUnsunkShips(), board.getShotsPossible());
    for (int i = 0; i < numOfShots; i++) {
      if (possibleShots.size() == 0) {
        break;
      }
      // Choose a shot based on the heatmap
      Coord generatedShot = getHighestProbCell();
      // remove the generated shot from the possible shots
      possibleShots.remove(generatedShot);
      shots.add(generatedShot);
    }
    return shots;
  }

  /**
   * @return the highest probability cell from the heat map and removes it from the possible shots
   */
  private Coord getHighestProbCell() {
    int max = -1;
    Coord maxCoord = null;
    for (int i = 0; i < board.getHeight(); i++) {
      for (int j = 0; j < board.getWidth(); j++) {
        if (heatMap[i][j] > max && possibleShots.contains(new Coord(i, j))) {
          max = heatMap[i][j];
          maxCoord = new Coord(i, j);
        }
      }
    }
    return maxCoord;
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *     ship on this board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Coord> shots = board.damageShots(opponentShotsOnBoard);
    board.update(opponentShotsOnBoard);
    return shots;
  }

  /**
   * updates the shotMap with misses and hits from the previous round and generates the heat map
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */

  //update the heatmap
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    // amplified probabilities
    prevRoundShots.removeAll(shotsThatHitOpponentShips);
    // all the shots in prevRoundShots are now Misses
    for (Coord miss : prevRoundShots) {
      shotMap[miss.getRow()][miss.getCol()] = 2;
    }
    for (Coord hit : shotsThatHitOpponentShips) {
      shotMap[hit.getRow()][hit.getCol()] = 1;
    }
    generateHeatMap(board.getHeight(), board.getWidth());
  }

  /**
   * The AI player doesn't do anything if notified of the game result
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
  }
}