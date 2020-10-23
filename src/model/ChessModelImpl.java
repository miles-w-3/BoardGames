package model;

public class ChessModelImpl implements ChessModel {

  AbstractGamePiece[][] board;
  // store the king's coordinates at all times
  int[] wKing;
  int[] bKing;
  boolean whitesTurn;
  boolean bCheck;
  boolean wCheck;

  public ChessModelImpl() {
    board = new AbstractGamePiece[8][8];
    wKing = new int[2];
    bKing = new int[2];
    whitesTurn = true;
    this.setupBoard();
    bCheck = false;
    wCheck = false;

  }

  // place all pieces on their starting positions 
  private void setupBoard() {
    // black rooks
    board[0][0] = new Rook(false);
    board[0][7] = new Rook(false);
    // white rooks
    board[7][0] = new Rook(true);
    board[7][7] = new Rook(true);
    // black bishops
    board[0][2] = new Bishop(false);
    board[0][5] = new Bishop(false);
    // white bishops
    board[7][2] = new Bishop(true);
    board[7][5] = new Bishop(true);
    // black knights
    board[0][1] = new Knight(false);
    board[0][6] = new Knight(false);
    // white knights
    board[7][1] = new Knight(true);
    board[7][6] = new Knight(true);
    // black king and queen
    board[0][3] = new Queen(false);
    board[0][4] = new King(false);
    bKing[0] = 0;
    bKing[1] = 4;
    // white king and queen
    board[7][3] = new Queen(true);
    board[7][4] = new King(true);
    wKing[0] = 7;
    wKing[1] = 4;

    // pawns
    for (int f = 0; f < 8; f++) {
      // black pawns
      board[1][f] = new Pawn(false);
      // white pawns
      board[6][f] = new Pawn(true);
    }
  }

  @Override
  public void movePiece(int fromRank, int fromFile, int toRank, int toFile) {
    AbstractGamePiece selectedPiece;
    AbstractGamePiece destinationPiece;
    // check that all coordinates are within the board area
    if (!(fromRank >= 0 && fromRank < 8 && fromFile >= 0 && fromFile < 8
        && toRank >= 0 && toRank < 8 && toFile >= 0 && toFile < 8)) {
      throw new IllegalArgumentException("All locations must be within the board area.");
    }
    destinationPiece = board[toRank][toFile];
    selectedPiece = board[fromRank][fromFile];
    // make sure that there is a piece at the from location
    if (selectedPiece == null) {
      throw new IllegalArgumentException("There is no piece at the selected starting space.");
    }
    // make sure that the destination does not have a piece from the same team
    if (destinationPiece != null && destinationPiece.isFirst == selectedPiece.isFirst) {
      throw new IllegalArgumentException("There is already an allied piece in that destination.");
    }

    AbstractGamePiece[][] boardCopy = getBoard();
    if (selectedPiece.canMoveTo(fromRank, fromFile, toRank, toFile, boardCopy)) {
      // make sure that king of the side moving right now is not left exposed by the move

      // simulate the move
      boardCopy[toRank][toFile] = boardCopy[fromRank][fromFile];
      boardCopy[fromRank][fromFile] = null;

      // check all pieces for potential voluntary check and to keep track of when king is put in check
      for (int r = 0; r < 8; r++) {
        for (int f = 0; f < 8; f++) {
          // for any given piece, make sure that it cannot move to the location of the enemy king
          if (boardCopy[r][f] != null) {
            // white has just moved - since this variable only updates on actual moves
            if (whitesTurn) {
              // see if the white piece put the black king in check
              if (!boardCopy[r][f].isFirst &&
                boardCopy[r][f].canMoveTo(r, f, bKing[0], bKing[1], boardCopy)) {
                bCheck = true;
              }
              // check if white would place itself in check with this move
              else if (!boardCopy[r][f].isFirst
                  && boardCopy[r][f].canMoveTo(r, f, wKing[0], wKing[1], boardCopy)) {
                throw new IllegalArgumentException("White side cannot expose its king!");
              }

            }
            // black turn
            else {
              // see if the black piece put the white king in check
              if (boardCopy[r][f].isFirst &&
                  boardCopy[r][f].canMoveTo(r, f, wKing[0], wKing[1], boardCopy)) {
                wCheck = true;
              }
              // if a white piece can now move to the location of the black king
              else if (boardCopy[r][f].isFirst
                  && boardCopy[r][f].canMoveTo(r, f, bKing[0], bKing[1], boardCopy)) {
                throw new IllegalArgumentException("Black side cannot expose its king!");
              }
            }
          }
        }
      }
      // make sure that king location arrays are updated
      // update white king position
      if (wKing[0] == fromRank && wKing[1] == fromFile) {
        wKing[0] = toRank;
        wKing[1] = toFile;
      }
      // update black king position
      else if (bKing[0] == fromRank && bKing[1] == fromFile) {
        bKing[0] = toRank;
        bKing[1] = toFile;
      }

      board[toRank][toFile] = board[fromRank][fromFile];
      board[fromRank][fromFile] = null;
      whitesTurn = !whitesTurn; // change turn side
    }
    // move is not allowed
    else {
      throw new IllegalArgumentException("Illegal Move.");
    }
  }

  @Override
  public AbstractGamePiece[][] getBoard() {
    AbstractGamePiece[][] boardCopy = new AbstractGamePiece[8][8];
    for (int r = 0; r < 8; r++) {
      for (int f = 0; f < 8; f++) {
        if (board[r][f] != null) {
          boardCopy[r][f] = board[r][f].copy();
        }
      }
    }
    return boardCopy;
  }

  @Override
  public boolean isSelected(int r, int f) {
    return board[r][f].isSelected;
  }

  @Override
  public void select(int r, int f) {
    board[r][f].select();
  }

  @Override
  public void deSelect(int r, int f) {
    board[r][f].deSelect();
  }

}
