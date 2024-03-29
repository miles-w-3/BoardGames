package model;


import java.awt.Image;
import util.PlayerSide;

public class Bishop extends AbstractGamePiece {


  /**
   * Create a game piece and assign its team.
   *
   * @param side The side this piece belongs to
   * @param icon The image that will be displayed to represent this piece
   */
  protected Bishop(PlayerSide side, Image icon) {
    super(side, icon, 3);
  }

  /**
   * Returns true if a Bishop's movement is valid along a proper diagonal with no obstacles.
   */
  @Override
  protected boolean canMoveTo(int fromRank, int fromFile, int toRank, int toFile,
      AbstractGamePiece[][] gameBoard) {
    // store the destination space
    AbstractGamePiece destinationSpace = gameBoard[toRank][toFile];
    // if the move is a proper diagonal
    if (Math.abs(toRank - fromRank) == Math.abs(toFile - fromFile)
        //either there is no piece at the destination, or the piece is of the opposite side
        && (destinationSpace == null || destinationSpace.side != this.side)) {

      // these variables will store the direction of each movement component
      int rDir = 1;
      int fDir = 1;
      if (toRank < fromRank) {
        rDir = -1;
      }
      if (toFile < fromFile) {
        fDir = - 1;
      }

      int r = fromRank + rDir;
      int f = fromFile + fDir;
      //make sure that there are no obstacles in between the start and the destination
      while (r != toRank && f != toFile) {
        // if there is a piece here, it is in the way and the move can't happen
        if (gameBoard[r][f] != null) {
          return false;
        }
        r += rDir;
        f += fDir;
      }
      return true;
    }
    else {
      return false;
    }
  }

  @Override
  public String toString() {
    return "bishop";
  }

  public Bishop copy() {
    Bishop b = new Bishop(side, pieceImg);
    b.hasMoved = hasMoved;
    return b;
  }
}
