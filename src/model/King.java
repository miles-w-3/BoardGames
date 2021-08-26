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

    // make sure that the king is only moving one space
    return moveHorizontal || moveVertical || moveDiagonal;
  }

  @Override
  public String toString() {
    return "king";
  }

  public King copy() {
    return new King(side, pieceImg);
  }

}
