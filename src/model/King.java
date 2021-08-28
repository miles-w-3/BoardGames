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

  /**
   * Note that the king's check movement and castling logic is handled by the model.
   * This method simply checks that the king is moving a single space in any direction
   */
  @Override
  protected boolean canMoveTo(int fromRank, int fromFile, int toRank, int toFile,
      AbstractGamePiece[][] gameBoard) {

    boolean moveHorizontal = Math.abs(fromFile - toFile) == 1 && fromRank == toRank;
    boolean moveVertical = Math.abs(fromRank - toRank) == 1 && fromFile == toFile;
    boolean moveDiagonal = Math.abs(fromFile - toFile) == 1 && Math.abs(fromRank - toRank) == 1;

    return moveHorizontal || moveVertical || moveDiagonal;
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
