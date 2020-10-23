package model;

public class King extends AbstractGamePiece {

  protected King(boolean isFirst) {
    super(isFirst);
  }

  @Override
  // need to account for the fact that king cant move into check
  /*
  one way to do this is to check all diags for a bishop/queen, check lines for rook/queen, check
  special positions for knight, and check immediate diags for pawn
   */
  // run canmove on every piece on the board with the destination being the coordinates that the king wants to go to
  protected boolean canMoveTo(int fromRank, int fromFile, int toRank, int toFile,
      AbstractGamePiece[][] gameBoard) {
    boolean moveHorizontal = Math.abs(fromFile - toFile) == 1 && fromRank == toRank;
    boolean moveVertical = Math.abs(fromRank - toRank) == 1 && fromFile == toFile;
    boolean moveDiagonal = Math.abs(fromFile - toFile) == 1 && Math.abs(fromRank - toRank) == 1;

    // make sure that the king is only moving one space
    if (moveHorizontal || moveVertical || moveDiagonal) {
      // ensure that the king is not moving into check
      for(int r = 0; r < 8; r++){
        for(int f = 0; f < 8; f++){
          // we want to make sure that every enemy piece on the board cannot move to where
          // the king is moving right now
          if (gameBoard[r][f] != null && gameBoard[r][f].isFirst != this.isFirst){
            // specifically check if a piece can move to our desired location
            if (gameBoard[r][f].canMoveTo(r, f, toRank, toFile, gameBoard)) {
              throw new IllegalArgumentException("You cannot move your king into check!");
            }
          }
        }
      }
      // otherwise, if the king isn't in danger, then it can move
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "king";
  }

  public King copy() {
    King c = new King(isFirst);
    c.isSelected = this.isSelected;
    return c;
  }

}
