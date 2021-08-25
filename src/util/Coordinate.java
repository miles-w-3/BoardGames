package util;

/**
 * Represents a file, rank zero-indexed coordinate pair on the game board
 */
public class Coordinate {
  public int rank;
  public int file;

  // Represents an empty coordinate object
  public Coordinate() {
    this.rank = -1;
    this.file = -1;
  }

  // Represents an actual coordinate on the board
  public Coordinate(int rank, int file) {
    this.rank = rank;
    this.file = file;
  }

  // Convenience method to quickly update a coordinate's rank and file
  public void update(int newRank, int newFile) {
    this.rank = newRank;
    this.file = newFile;
  }

  // Return true if both rank and file coordinates are non-negative
  public boolean isValid() {
    return rank > -1 && file > -1;
  }





}
