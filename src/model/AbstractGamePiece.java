package model;

import java.awt.Image;
import util.PlayerSide;

/**
 * Represents a game piece that is either on the team that moves first or its opponent.
 * Able to determine whether
 */
public abstract class AbstractGamePiece {

  protected final int value; // the value of the piece to the score
  protected Image pieceImg;
  protected PlayerSide side;

  /**
   * Create a game piece and assign its team.
   * @param side The side this piece belongs to
   * @param icon The image that will be displayed to represent this piece
   */
  protected AbstractGamePiece(PlayerSide side, Image icon, int value) {
    this.side = side;
    this.pieceImg = icon;
    this.value = value;
  }

  /**
   * Determines whether a piece can move to a given location given its type of movement.
   * @param fromRank the current rank of the piece
   * @param fromFile the current file of the piece
   * @param toRank the desired rank of the piece
   * @param toFile the desired file of the piece
   * @param gameBoard the current state of the game
   * @return true if the move is possible for the given piece, or false otherwise
   */
  abstract protected boolean canMoveTo(int fromRank, int fromFile, int toRank, int toFile,
      AbstractGamePiece[][] gameBoard);
  // check for every piece up until the end that you can move there. That's how you know that nothing's in the way
  // pass in the board

  /**
   * @return true if this piece is on the side that moves first
   */
  public PlayerSide getSide() {
    return this.side;
  }


  /**
   * Copy this piece
   */
  public abstract AbstractGamePiece copy();
}
