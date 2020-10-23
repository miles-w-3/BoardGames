package model;

public class Knight extends AbstractGamePiece {

  protected Knight(boolean isFirst) {
    super(isFirst);
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
    Knight c = new Knight(isFirst);
    c.isSelected = this.isSelected;
    return c;
  }

}
