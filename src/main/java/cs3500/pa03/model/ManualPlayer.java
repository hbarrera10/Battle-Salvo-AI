package cs3500.pa03.model;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * represents the Console/manual player
 */
public class ManualPlayer implements Player {
  private final Board board;


  /**
   * base constructor
   *
   * @param board associated with this player
   */
  public ManualPlayer(Board board) {
    this.board = board;
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return "Manual Player";
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   * EFFECT: sets up all the ships randomly
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    // take each ship
    Random random = new Random();
    return board.placeShips(specifications, random);
  }

  /**
   * take shots is performed from the controller rather than here. So this method is obsolete.
   *
   * @return null since the output for a Manual console player is disregarded in this case
   */
  @Override
  public List<Coord> takeShots() {
    return null;
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
   * the player doesn't do anything with the successful shots they landed
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
  }

  /**
   * The manual player doesn't do anything if notified of the game result
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {}
}
