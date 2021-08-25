package model;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import util.ChessMoveException;
import util.Coordinate;
import util.PlayerSide;
import view.BoardView;

public class ChessModelImpl implements ChessModel {

  AbstractGamePiece[][] board;
  // store the king's coordinates for "in check" verification
  HashMap<PlayerSide, Coordinate> kingCoords;
  boolean bCheck;
  boolean wCheck;

  public ChessModelImpl() {
    board = new AbstractGamePiece[8][8];
    kingCoords = new HashMap<>();
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
      kingCoords.put(PlayerSide.BLACK, new Coordinate(0, 4));
      // white king and queen
      board[7][3] = new Queen(PlayerSide.WHITE, wQueen);
      board[7][4] = new King(PlayerSide.WHITE, wKing);
      kingCoords.put(PlayerSide.WHITE, new Coordinate(7, 4));

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
  public int movePiece(PlayerSide currentTurn, int fromRank, int fromFile, int toRank, int toFile)
      throws ChessMoveException {
    // check that all coordinates are within the board area
    if (!(fromRank >= 0 && fromRank < 8 && fromFile >= 0 && fromFile < 8
        && toRank >= 0 && toRank < 8 && toFile >= 0 && toFile < 8)) {
      throw new IllegalArgumentException("All locations must be within the board area.");
    }

    boolean movingKing = validateMove(currentTurn, fromRank, fromFile, toRank, toFile);

    // since all checks have passed at this point, make the move for the player
    int takenPieceValue = 0;
    // if a piece is being taken, get its value for scoring
    if (board[toRank][toFile] != null) {
      takenPieceValue = board[toRank][toFile].value;
    }
    board[toRank][toFile] = board[fromRank][fromFile];
    board[fromRank][fromFile] = null;
    // update the king coordinates tracker if the king was moved
    if (movingKing) {
      kingCoords.get(currentTurn).update(toRank, toFile);
    }
    return takenPieceValue;
  }

  // Make sure that the move is possible and doesn't leave player's king in check, return true if moving king
  private boolean validateMove(PlayerSide currentTurn, int fromRank, int fromFile,
      int toRank, int toFile) throws ChessMoveException {
    AbstractGamePiece selectedPiece = board[fromRank][fromFile];
    // coordinates of the playing side's king
    Coordinate playingKingCoords = kingCoords.get(currentTurn);
    // see if king is in check before move
    boolean inCheck = isCurrentlyInCheck(currentTurn, playingKingCoords, board);

    // simulate the move if it is valid for the piece
    if (selectedPiece.canMoveTo(fromRank, fromFile, toRank, toFile, board)) {
      // see if the king is being moved this turn TODO: This boolean allows handling of castling logic, perhaps after the other in expanding the one-line movingKing if statement if it's not handling by the canmoveto of the above if
      boolean movingKing = playingKingCoords.match(fromRank, fromFile);

      // update moving king coords
      AbstractGamePiece[][] boardCopy = getBoard();
      // simulate the move on a copy of the board to see if it is possible
      boardCopy[toRank][toFile] = boardCopy[fromRank][fromFile];
      boardCopy[fromRank][fromFile] = null;
      // update the king coordinates if it was the move simulated
      if (movingKing) {
        playingKingCoords.update(toRank, toFile);
      }

      // now check if the move would leave the king in check
      boolean wouldBeInCheck = isCurrentlyInCheck(currentTurn, playingKingCoords, boardCopy);
      validateForCheck(inCheck, wouldBeInCheck);

      return movingKing;
    }
    // the piece could not move to the given location
    else {
      throw new ChessMoveException("Invalid move for " + selectedPiece.toString());
    }
  }


  // Determine whether the king is currently in check
  private boolean isCurrentlyInCheck(PlayerSide currentTurn, Coordinate kingCoords,
      AbstractGamePiece[][] board) {
    // Check whether the king is currently in check
    for (int r = 0; r < 8; r++) {
      for (int f = 0; f < 8; f++) {
        AbstractGamePiece boardLocation = board[r][f];
        // see if enemy pieces can move to the king
        if (boardLocation != null && boardLocation.side != currentTurn) {
          boolean reach = boardLocation.canMoveTo(r, f, kingCoords.rank, kingCoords.file, board);
          // return if we have found that the king is in check
          if (reach) {
            return true;
          }
        }
      }
    }
    return false;
  }

  // Determine whether player being in check is properly addressed, return true if acceptable move
  private void validateForCheck(boolean inCheck, boolean wouldBeInCheck)
      throws ChessMoveException {
    // If the player would not be in check from the move then the move is good
    if (!wouldBeInCheck) {
      return;
    }
    String errorMsg = "";
    // the player was in check before this move and it was not addressed
    if (inCheck) {
      errorMsg = "Must address king in check!";
    }
    // the player is moving into check after not being in check
    else {
      errorMsg = "Cannot expose king to check!";
    }
    // Since the king is in check, send situation-specific message to the user
    throw new ChessMoveException(errorMsg);
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
