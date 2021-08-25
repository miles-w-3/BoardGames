package model;

import java.awt.Image;
import util.PlayerSide;

/**
 * Represents a rook on either side. Allows vertical and horizontal moves.
 */
public class Rook extends AbstractGamePiece {

  /**
   * Create a game piece and assign its team.
   *
   * @param side The side this piece belongs to
   * @param icon The image that will be displayed to represent this piece
   */
  protected Rook(PlayerSide side, Image icon) {
    super(side, icon, 5);
  }

  @Override
  protected boolean canMoveTo(int fromRank, int fromFile, int toRank, int toFile,
      AbstractGamePiece[][] gameBoard) {

    AbstractGamePiece destinationSpace = gameBoard[toRank][toFile];
    // horizontal if the rank isn't changing
    boolean isHorizontal = fromRank == toRank;
    // vertical if the file isn't changing
    boolean isVertical = fromFile == toFile;
    // ensures the move is either horizontal or vertical
    if (isHorizontal) {
      // check that there are no pieces in the way
      if (fromFile > toFile) {
        for (int f = toFile + 1; f < fromFile; f++) {
          if (gameBoard[fromRank][f] != null) {
            return false;
          }
        }
      }
      // going right
      else {
        for (int f = fromFile + 1; f < toFile; f++) {
          if (gameBoard[fromRank][f] != null) {
            return false;
          }
        }
      }
      return true;
    }
    else if (isVertical) {
      if (toRank < fromRank) {
        for (int r = toRank + 1; r < fromRank; r++) {
          if(gameBoard[r][fromFile] != null) {
            return false;
          }
        }
      }
      // going down
      else {
        for (int r = fromRank + 1; r < toRank; r++) {
          if (gameBoard[r][fromFile] != null) {
            return false;
          }
        }
      }
      return true;
    }
    return false;
  }


  @Override
  public String toString() {
    return "rook";
  }

  public Rook copy() {
    return new Rook(side, pieceImg);
  }
}
