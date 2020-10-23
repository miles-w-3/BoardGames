package model;

// the individual pieces can just say if the move is consistent with what's allowed for them. Eg a bishop will return if the move is a proper diagonal
// but the model has to say if there is another piece in the way or if it is outside of the board area

public interface ChessModel {

  /**
   * Move a selected chess piece to the desired position.
   *
   * @param fromRank the rank that the piece is moving from
   * @param fromFile the file that the piece is moving from
   * @param toRank   the rank that the piece is moving to
   * @param toFile   the file that the piece is moving to
   * @throws IllegalArgumentException if either set of coordinates are out of bounds
   * @throws IllegalArgumentException if there is an allied piece in the destination
   * @throws IllegalArgumentException if there are obstacles in the path of the move
   */
  void movePiece(int fromRank, int fromFile, int toRank, int toFile);

  /**
   * Get a copy of the current board and the pieces on it
   *
   * @return A 2D array of game pieces, where null represents an empty space
   */
  AbstractGamePiece[][] getBoard();

  /**
   * @param r the row of the desired piece
   * @param f the file of the desired piece
   *
   * @return true if the given piece is selected
   */
  boolean isSelected(int r, int f);

  /**
   * Select the given piece.
   *
   * @param r the row of the desired piece
   * @param f the file of the desired piece
   */
  void select(int r, int f);

  /**
   * deSelect the given piece.
   *
   * @param r the row of the desired piece
   * @param f the file of the desired piece
   */
  void deSelect(int r, int f);


}
