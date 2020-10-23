package model;

/**
 * Represents the Queen chess Piece. Extends a rook and composes a bishop in order to have access
 * to both canMove methods.
 */
public class Queen extends AbstractGamePiece {

  // compose a bishop for access to its move calculations
  private Bishop bishopMoves;
  // compose a rook for access to its move calculations
  private Rook rookMoves;

  protected Queen(boolean isFirst) {
    super(isFirst);
    this.rookMoves = new Rook(isFirst);
    this.bishopMoves = new Bishop(isFirst);
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
    Queen c = new Queen(isFirst);
    c.isSelected = this.isSelected;
    return c;
  }


}
