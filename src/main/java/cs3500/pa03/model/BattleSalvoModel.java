package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * represents the model within the Battle Salvo
 */
public abstract class BattleSalvoModel implements Model<List<Coord>> {
  Player player1;
  Player player2;
  Board player1Board;
  Board player2Board;

  List<Coord> userHitShots = new ArrayList<>();
  List<Coord> aiHitShots = new ArrayList<>();

  /**
   * @return player1Board
   */
  public Board getPlayer1Board() {
    return player1Board;
  }

  /**
   * @return player2Board
   */
  public Board getPlayer2Board() {
    return player2Board;
  }


  /**
   * initializes the boards of both players
   *
   * @param height of the board
   * @param width of the baord
   */
  public abstract void initializeBoards(int height, int width);

  /**
   * @return the smallest board dimension
   */
  public abstract int minBoardDimension();

  /**
   * set up the ships on the board given the fleet info
   *
   * @param fleetInfo determines how many ships there will be
   */
  public abstract void setupBoard(Map<ShipType, Integer> fleetInfo);

  /**
   * @return the number of shots that can be performed
   */
  public abstract int getNumOfShots();

  /**
   * @return list of coords representing shots that the AI hit
   */
  public List<Coord> getAiHitShots() {
    return aiHitShots;
  }

  /**
   * @return list of coords representing shots that the User hit
   */
  public List<Coord> getUserHitShots() {
    return userHitShots;
  }

  /**
   * @return boolean representing whether the game is over
   */
  public abstract boolean isGameOver();

  /**
   * @return GameResult representing the ending state of the game
   */
  public abstract GameResult getGameResult();

  /**
   * checks whether the proposed shot is valid
   *
   * @param row of the shot
   * @param col of the shot
   * @return boolean indicating validity of shot
   */
  public abstract boolean validShot(int row, int col);
}
