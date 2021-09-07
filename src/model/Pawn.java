package model;

import java.awt.Image;
import util.PlayerSide;

public class Pawn extends AbstractGamePiece {

  /**
   * Create a game piece and assign its team.
   *
   * @param side The side this piece belongs to
   * @param icon The image that will be displayed to represent this piece
   */
  protected Pawn(PlayerSide side, Image icon) {
    super(side, icon, 1);
    hasMoved = false;
  }


  @Override
  protected boolean canMoveTo(int fromRank, int fromFile, int toRank, int toFile,
      AbstractGamePiece[][] gameBoard) {
    // check that white pieces are only moving forward
    if (this.side == PlayerSide.WHITE && toRank < fromRank) {

      //if this is the first move, allow double move forward as long as both spaces in front are empty
      if (!hasMoved && fromRank - toRank == 2 && fromFile == toFile
      && gameBoard[toRank][toFile] == null && gameBoard[fromRank - 1][toFile] == null) {
        return true;
      }
      // for moves only one rank forward
      return checkSingle(fromRank, fromFile, toRank, toFile, gameBoard[toRank]);
    }

    // checks that black pieces are only moving forward
    if (this.side == PlayerSide.BLACK && toRank > fromRank) {
      //if this is the first move, allow double move forward as long as both spaces in front are empty
      if (!hasMoved && toRank - fromRank == 2 && fromFile == toFile
          && gameBoard[toRank][toFile] == null && gameBoard[fromRank + 1][toFile] == null) {
        return true;
      }
      // for moves only one rank forward
      return checkSingle(toRank, fromFile, fromRank, toFile, gameBoard[toRank]);
    }
    return false;
  }

  // check for moves one rank forward
  private boolean checkSingle(int fromRank, int fromFile, int toRank, int toFile,
      AbstractGamePiece[] boardRank) {
    if (fromRank - toRank == 1) {
      // single space forward - ensure that the file is the same and dest. is empty
      if (fromFile == toFile && boardRank[toFile] == null) {
        return true;
      }
      // capturing a piece - dest. must have a piece, diagonal must be one space away
      // note that side checking is done by the controller
      return boardRank[toFile] != null && Math.abs(fromFile - toFile) == 1;
    }
    return false;
  }

  @Override
  public String toString() {
    return "pawn";
  }

  public Pawn copy() {
    Pawn c = new Pawn(side, pieceImg);
    c.hasMoved = this.hasMoved;
    return c;
  }

  @Override
  public boolean isPromotable() {
    return true;
  }


}
