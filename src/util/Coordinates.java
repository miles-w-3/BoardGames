package util;

/**
 * Represents a file, rank zero-indexed coordinate pair on the game board
 */
public class Coordinates {
  public int rank;
  public int file;

  // Represents an empty coordinate object
  public Coordinates() {
    this.rank = -1;
    this.file = -1;
  }

  // Represents an actual coordinate on the board
  public Coordinates(int rank, int file) {
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

  // Conveniently invalidate a coordinates object
  public void invalidate() {
    this.rank = -1;
    this.file = -1;
  }

  @Override
  public String toString() {
    return "(Rank: " + rank + ", File: " + file + ")";
  }

  public Coordinates copy() {
    return new Coordinates(this.rank, this.file);
  }

  // true if the given rank and file match the coordinate's
  public boolean match(int rank, int file) {
    return this.rank == rank && this.file == file;
  }


  public boolean match(Coordinates c) {
    return this.rank == c.rank && this.file == c.file;
  }





}
