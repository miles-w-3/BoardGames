package model;

// the individual pieces can just say if the move is consistent with what's allowed for them. Eg a bishop will return if the move is a proper diagonal
// but the model has to say if there is another piece in the way or if it is outside of the board area

import java.awt.Image;
import util.ChessMoveException;
import util.GameState;
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
   * Attempt to castle for the given side.
   *
   * @param forSide    the side for which castling will be executed
   * @param longCastle <c>true</c> if castling long towards file 0, false otherwise
   * @throws ChessMoveException if castling is illegal in the current board state
   */
  void attemptCastle(PlayerSide forSide, boolean longCastle) throws ChessMoveException;

  /**
   * See whether the piece at the given coordinates should be promoted
   */
  boolean shouldBePromoted(PlayerSide side, int rank, int file);

  /**
   * Swap out the piece at the given location with the new choice.
   *
   * @param pieceChoice the name of the piece to promote
   * @param rank        promoting piece rank
   * @param file        promoting piece file
   * @return true if promotion was successful
   */
  boolean promote(String pieceChoice, int rank, int file);

  /**
   * Scan for whether the side provided has been placed in checkmate or a stalemate has occurred
   *
   * @param currentTurn the side about to play
   * @return the game state found by the scan
   */
  GameState scanForMates(PlayerSide currentTurn);

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
