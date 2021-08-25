package model;

import java.awt.Image;
import util.PlayerSide;

public class Knight extends AbstractGamePiece {


  /**
   * Create a game piece and assign its team.
   *
   * @param side The side this piece belongs to
   * @param icon The image that will be displayed to represent this piece
   */
  protected Knight(PlayerSide side, Image icon) {
    super(side, icon);
  }

  @Override
  protected boolean canMoveTo(int fromRank, int fromFile, int toRank, int toFile,
      AbstractGamePiece[][] gameBoard) {
    // check against all possible knight moves
    return ((toRank == fromRank + 2 || toRank == fromRank - 2)
        && (toFile == fromFile - 1 || toFile == fromFile + 1))
        || ((toFile == fromFile + 2 || toFile == fromFile - 2)
        && (toRank == fromRank + 1 || toRank == fromRank - 1));
  }

  @Override
  public String toString() {
    return "knight";
  }

  public Knight copy() {
    Knight c = new Knight(side, pieceImg);
    c.isSelected = this.isSelected;
    return c;
  }

}
