package model;

// the individual pieces can just say if the move is consistent with what's allowed for them. Eg a bishop will return if the move is a proper diagonal
// but the model has to say if there is another piece in the way or if it is outside of the board area

import java.awt.Image;
import util.ChessMoveException;
import util.PlayerSide;

public interface ChessModel {

  /**
   * Move a selected chess piece to the desired position.
   *
   * @param currentTurn the side making the move
   * @param fromRank    the rank that the piece is moving from
   * @param fromFile    the file that the piece is moving from
   * @param toRank      the rank that the piece is moving to
   * @param toFile      the file that the piece is moving to
   * @return the value of a piece that was taken during the move, or zero if no piece was taken
   * @throws IllegalArgumentException if either set of coordinates are out of bounds
   * @throws ChessMoveException       if there are obstacles in the path of the move or the move is
   *                                  illegal
   */
  int movePiece(PlayerSide currentTurn, int fromRank, int fromFile, int toRank, int toFile)
      throws ChessMoveException;

  /**
   * Scan for whether the side provided has been placed in checkmate.
   *
   * @param currentTurn the playing side that is potentially in checkmate
   * @return true if the provided side is in checkmate
   */
  boolean scanForCheckmate(PlayerSide currentTurn);

  /**
   * Get a copy of the current board and the pieces on it
   *
   * @return A 2D array of game pieces, where null represents an empty space
   */
  AbstractGamePiece[][] getBoard();

  /**
   * Return the board grid with piece images at their current locations
   *
   * @return
   */
  Image[][] getBoardIcons();


}
