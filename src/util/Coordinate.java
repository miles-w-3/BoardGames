package util;

/**
 * Represents a file, rank zero-indexed coordinate pair on the game board
 */
public class Coordinate {
  public int rank;
  public int file;


  Coordinate(int rank, int file) {
    this.rank = rank;
    this.file = file;
  }
}
