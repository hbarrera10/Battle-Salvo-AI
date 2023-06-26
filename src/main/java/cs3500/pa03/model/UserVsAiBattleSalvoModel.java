package cs3500.pa03.model;

import cs3500.pa03.controller.UserVsAiBattleSalvoController;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * represents the BattleSalvoModel that deals with an Ai and User player
 */
public class UserVsAiBattleSalvoModel extends BattleSalvoModel {

  /**
   * initialize the boards of this game
   *
   * @param height of board
   * @param width of board
   */
  @Override
  public void initializeBoards(int height, int width) {
    player1Board = new Board(height, width, false);
    player2Board = new Board(height, width, true);
    player1 = new ManualPlayer(player1Board);
    player2 = new AiPlayer(player2Board);
  }

  /**
   * sets up the board with the fleet info
   *
   * @param fleetInfo determines how many ships there will be
   */
  @Override
  public void setupBoard(Map<ShipType, Integer> fleetInfo) {
    player1.setup(player1Board.getHeight(), player1Board.getWidth(), fleetInfo);
    player2.setup(player2Board.getHeight(), player2Board.getWidth(), fleetInfo);
  }

  /**
   * @return the number of shots that the manual player can use
   */
  @Override
  public int getNumOfShots() {
    return Math.min(player2Board.getShotsPossible(), player1Board.getNumOfUnsunkShips());
  }

  /**
   * @return boolean indicating whether the game is won, lost, or tied
   */
  @Override
  public boolean isGameOver() {
    return player1Board.isGameOver() || player2Board.isGameOver();
  }

  /**
   * @return a GameResult form the perspective of the user
   */
  @Override
  public GameResult getGameResult() {
    if (isGameOver()) {
      if (player1Board.isGameOver() && player2Board.isGameOver()) {
        return GameResult.DRAW;
      } else if (!player1Board.isGameOver() && player2Board.isGameOver()) {
        return GameResult.WIN;
      } else {
        // in the case that the Ai won
        return GameResult.LOSS;
      }
    } else {
      throw new NoSuchElementException();
    }

  }

  /**
   * This is only called when checking for the User's inputted shots
   *
   * @param row of shot
   * @param col of shot
   * @return boolean indicating validity of shot
   */
  @Override
  public boolean validShot(int row, int col) {
    return row <= player2Board.getHeight() && row >= 0 && col <= player2Board.getWidth()
           && col >= 0 && !player2Board.alreadyShot(row, col);
  }


  /**
   * @return smaller board dimension
   */
  @Override
  public int minBoardDimension() {
    return player1Board.getSmallerDimension();
  }

  /**
   * info received from user and is sent to the AI Board. The AI is prompted for shots and updates
   * the user Board
   *
   * @param userShots represent the shots taken by the user
   */
  @Override
  public void update(List<Coord> userShots) {
    //
    List<Coord> aiShots = player2.takeShots();
    // EFFECT: update the board with the all the shots
    aiHitShots = player1.reportDamage(aiShots);
    userHitShots = player2.reportDamage(userShots);
  }
}
