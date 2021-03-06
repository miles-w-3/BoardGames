package model;

/**
 * Represents a game piece that is either on the team that moves first or its opponent.
 * Able to determine whether
 */
public abstract class AbstractGamePiece {

  protected boolean isSelected;
  protected boolean isFirst;

  /**
   * Create a game piece and assign its team.
   * @param isFirst true if this piece is on the team that moves first
   */
  protected AbstractGamePiece(boolean isFirst) {
    this.isFirst = isFirst;
  }

  /**
   * Determines whether a piece can move to a given location given its type of movement.
   * @param fromRank the current rank of the piece
   * @param fromFile the current file of the piece
   * @param toRank the desired rank of the piece
   * @param toFile the desired file of the piece
   * @param gameBoard the current state of the game
   * @return true if the move is possible for the given piece, or false otherwise
   */
  abstract protected boolean canMoveTo(int fromRank, int fromFile, int toRank, int toFile,
      AbstractGamePiece[][] gameBoard);
  // check for every piece up until the end that you can move there. That's how you know that nothing's in the way
  // pass in the board

  /**
   * @return true if this piece is on the side that moves first
   */
  public boolean movesFirst() {
    return this.isFirst;
  }

  /**
   * Highlights this piece
   */
  public void select() {
    this.isSelected = true;
  }

  /**
   * Removes highlight from this piece
   */
  public void deSelect() {
    this.isSelected = false;
  }

  /**
   * @return true if this piece is selected
   */
  public boolean isSelected(){
    return this.isSelected;
  }

  /**
   * Copy this piece
   */
  public abstract AbstractGamePiece copy();
}
