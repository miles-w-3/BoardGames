package model;

import java.awt.Image;
import util.PlayerSide;

public class King extends AbstractGamePiece {


  /**
   * Create a game piece and assign its team.
   *
   * @param side The side this piece belongs to
   * @param icon The image that will be displayed to represent this piece
   */
  protected King(PlayerSide side, Image icon) {
    super(side, icon, 0); // Since king is priceless, don't let value affect end score
  }

  @Override
  protected boolean canMoveTo(int fromRank, int fromFile, int toRank, int toFile,
      AbstractGamePiece[][] gameBoard) {

    boolean moveHorizontal = Math.abs(fromFile - toFile) == 1 && fromRank == toRank;
    boolean moveVertical = Math.abs(fromRank - toRank) == 1 && fromFile == toFile;
    boolean moveDiagonal = Math.abs(fromFile - toFile) == 1 && Math.abs(fromRank - toRank) == 1;

    if (moveHorizontal || moveVertical || moveDiagonal) {
      return true;
    }

    // Castle - see if it's on either ends of the row
    // TODO: Need to move this stuff because we also need to check that there are pieces in between and then need to simulate the king at each space from where it is now to the destination
    boolean castling = fromRank == toRank && (toFile == 0 || toFile == 7);
    boolean haveNotMoved = !this.hasMoved && gameBoard[toRank][toFile] != null
        && !gameBoard[toRank][toFile].hasMoved;

    // make sure that the king is only moving one space
    return false;
  }

  @Override
  public String toString() {
    return "king";
  }

  public King copy() {
    King k = new King(side, pieceImg);
    k.hasMoved = hasMoved;
    return k;
  }

}
