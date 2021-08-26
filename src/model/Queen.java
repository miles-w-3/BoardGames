package model;

import java.awt.Image;
import util.PlayerSide;

/**
 * Represents the Queen chess Piece. Extends a rook and composes a bishop in order to have access
 * to both canMove methods.
 */
public class Queen extends AbstractGamePiece {

  // compose a bishop for access to its move calculations
  private Bishop bishopMoves;
  // compose a rook for access to its move calculations
  private Rook rookMoves;

  /**
   * Create a game piece and assign its team.
   *
   * @param side The side this piece belongs to
   * @param icon The image that will be displayed to represent this piece
   */
  protected Queen(PlayerSide side, Image icon) {
    super(side, icon, 9);
    this.rookMoves = new Rook(side, icon);
    this.bishopMoves = new Bishop(side, icon);
  }


  protected boolean canMoveTo(int fromRank, int fromFile, int toRank, int toFile,
      AbstractGamePiece[][] gameBoard) {
    // check rook moves and bishop moves
    return rookMoves.canMoveTo(fromRank, fromFile, toRank, toFile, gameBoard)
        || bishopMoves.canMoveTo(fromRank, fromFile, toRank, toFile, gameBoard);
  }

  @Override
  public String toString() {
    return "queen";
  }

  public Queen copy() {
    Queen q = new Queen(side, pieceImg);
    q.hasMoved = hasMoved;
    return q;
  }


}
