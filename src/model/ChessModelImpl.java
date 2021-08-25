package model;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageIO;
import util.PlayerSide;
import view.BoardView;

public class ChessModelImpl implements ChessModel {

  AbstractGamePiece[][] board;
  // store the king's coordinates at all times TODO: Why!? did I do this - is sweeping through board to find king that inefficient? If so switch to the Coordinate class
  int[] whiteKingCoords;
  int[] blackKingCoords;
  // TODO: Get rid of this whitesTurn variable, get that info from the controller if it's actually needed, can be passed in with MovesPiece
  boolean whitesTurn;
  boolean bCheck;
  boolean wCheck;

  public ChessModelImpl() {
    board = new AbstractGamePiece[8][8];
    whiteKingCoords = new int[2];
    blackKingCoords = new int[2];
    whitesTurn = true;
    this.setupBoard();
    bCheck = false;
    wCheck = false;

  }

  // initializes all pieces at their starting positions
  private void setupBoard() {
    // read the images from disk once
    try {
      Image wRook = ImageIO.read(new File("resources/wRook.png"));
      Image bRook = ImageIO.read(new File("resources/bRook.png"));
      Image wKnight = ImageIO.read(new File("resources/wKnight.png"));
      Image bKnight = ImageIO.read(new File("resources/bKnight.png"));
      Image wBishop = ImageIO.read(new File("resources/wBish.png"));
      Image bBishop = ImageIO.read(new File("resources/bBish.png"));
      Image bQueen = ImageIO.read(new File("resources/bQueen.png"));
      Image wQueen = ImageIO.read(new File("resources/wQueen.png"));
      Image bPawn = ImageIO.read(new File("resources/bPawn.png"));
      Image wPawn = ImageIO.read(new File("resources/wPawn.png"));
      Image bKing = ImageIO.read(new File("resources/bKing.png"));
      Image wKing = ImageIO.read(new File("resources/wKing.png"));

      // black rooks
      board[0][0] = new Rook(PlayerSide.BLACK, bRook);
      board[0][7] = new Rook(PlayerSide.BLACK, bRook);
      // white rooks
      board[7][0] = new Rook(PlayerSide.WHITE, wRook);
      board[7][7] = new Rook(PlayerSide.WHITE, wRook);
      // black bishops
      board[0][2] = new Bishop(PlayerSide.BLACK, bBishop);
      board[0][5] = new Bishop(PlayerSide.BLACK, bBishop);
      // white bishops
      board[7][2] = new Bishop(PlayerSide.WHITE, wBishop);
      board[7][5] = new Bishop(PlayerSide.WHITE, wBishop);
      // black knights
      board[0][1] = new Knight(PlayerSide.BLACK, bKnight);
      board[0][6] = new Knight(PlayerSide.BLACK, bKnight);
      // white knights
      board[7][1] = new Knight(PlayerSide.WHITE, wKnight);
      board[7][6] = new Knight(PlayerSide.WHITE, wKnight);
      // black king and queen
      board[0][3] = new Queen(PlayerSide.BLACK, bQueen);
      board[0][4] = new King(PlayerSide.BLACK, bKing);
      blackKingCoords[0] = 0;
      blackKingCoords[1] = 4;
      // white king and queen
      board[7][3] = new Queen(PlayerSide.WHITE, wQueen);
      board[7][4] = new King(PlayerSide.WHITE, wKing);
      whiteKingCoords[0] = 7;
      whiteKingCoords[1] = 4;

      // rows of pawns
      for (int f = 0; f < 8; f++) {
        // black pawns
        board[1][f] = new Pawn(PlayerSide.BLACK, bPawn);
        // white pawns
        board[6][f] = new Pawn(PlayerSide.WHITE, wPawn);
      }
    } catch (IOException readError) {
    BoardView.throwErrorFrame("Error!",
        "Unable to load piece images, verify resource contents.");
    }
  }

  @Override
  public int movePiece(int fromRank, int fromFile, int toRank, int toFile) {
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
    // TODO: This is no longer needed because Selection of a new allied piece is handled in the controller
    // make sure that the destination does not have a piece from the same team
    if (destinationPiece != null && destinationPiece.side == selectedPiece.side) {
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
            // TODO: Eliminate this confusion. Want to know who the current turn is, not who just moved, especially if that's the variable name
            // white has just moved - since this variable only updates on actual moves
            if (whitesTurn) {
              // see if the white piece put the black king in check
              if (boardCopy[r][f].side == PlayerSide.BLACK &&
                boardCopy[r][f].canMoveTo(r, f, blackKingCoords[0], blackKingCoords[1], boardCopy)) {
                bCheck = true;
              }
              // check if white would place itself in check with this move
              else if (boardCopy[r][f].side == PlayerSide.BLACK
                  && boardCopy[r][f].canMoveTo(r, f, whiteKingCoords[0], whiteKingCoords[1], boardCopy)) {
                throw new IllegalArgumentException("White side cannot expose its king!");
              }

            }
            // black turn
            else {
              // see if the black piece put the white king in check
              if (boardCopy[r][f].side == PlayerSide.WHITE &&
                  boardCopy[r][f].canMoveTo(r, f, whiteKingCoords[0], whiteKingCoords[1], boardCopy)) {
                wCheck = true;
              }
              // if a white piece can now move to the location of the black king
              else if (boardCopy[r][f].side == PlayerSide.WHITE
                  && boardCopy[r][f].canMoveTo(r, f, blackKingCoords[0], blackKingCoords[1], boardCopy)) {
                throw new IllegalArgumentException("Black side cannot expose its king!");
              }
            }
          }
        }
      }
      // make sure that king location arrays are updated
      // update white king position
      if (whiteKingCoords[0] == fromRank && whiteKingCoords[1] == fromFile) {
        whiteKingCoords[0] = toRank;
        whiteKingCoords[1] = toFile;
      }
      // update black king position
      else if (blackKingCoords[0] == fromRank && blackKingCoords[1] == fromFile) {
        blackKingCoords[0] = toRank;
        blackKingCoords[1] = toFile;
      }

      int takenPieceValue = 0;
      // if a piece is being taken, get its value for scoring
      if (board[toRank][toFile] != null) {
        takenPieceValue = board[toRank][toFile].value;
      }
      board[toRank][toFile] = board[fromRank][fromFile];
      board[fromRank][fromFile] = null;
      whitesTurn = !whitesTurn; // change turn side
      return takenPieceValue;
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
  public Image[][] getBoardIcons() {
    Image[][] boardPieceImages = new Image[8][8];
    for (int r = 0; r < 8; r++) {
      for (int f = 0; f < 8; f++) {
        if (board[r][f] != null) {
          boardPieceImages[r][f] = board[r][f].pieceImg;
        }
      }
    }
    return boardPieceImages;
  }

}
