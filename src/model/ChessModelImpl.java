package model;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import util.ChessMoveException;
import util.Coordinates;
import util.GameState;
import util.PlayerSide;
import view.BoardView;

public class ChessModelImpl implements ChessModel {

  AbstractGamePiece[][] board;
  // store the king's coordinates for "in check" verification
  HashMap<PlayerSide, Coordinates> kingCoords;
  boolean bCheck;
  boolean wCheck;

  Image wRook;
  Image bRook;
  Image wKnight;
  Image bKnight;
  Image wBishop;
  Image bBishop;
  Image bQueen;
  Image wQueen;
  Image bPawn;
  Image wPawn;
  Image bKing;
  Image wKing;


  public ChessModelImpl(HashMap<PlayerSide, Coordinates> kingCoords) {
    board = new AbstractGamePiece[8][8];
    this.kingCoords = kingCoords;
    this.setupBoard();
    bCheck = false;
    wCheck = false;

  }

  // initializes all pieces at their starting positions
  private void setupBoard() {
    // read the images from disk once
    try {
      wRook = ImageIO.read(new File("resources/wRook.png"));
      bRook = ImageIO.read(new File("resources/bRook.png"));
      wKnight = ImageIO.read(new File("resources/wKnight.png"));
      bKnight = ImageIO.read(new File("resources/bKnight.png"));
      wBishop = ImageIO.read(new File("resources/wBish.png"));
      bBishop = ImageIO.read(new File("resources/bBish.png"));
      bQueen = ImageIO.read(new File("resources/bQueen.png"));
      wQueen = ImageIO.read(new File("resources/wQueen.png"));
      bPawn = ImageIO.read(new File("resources/bPawn.png"));
      wPawn = ImageIO.read(new File("resources/wPawn.png"));
      bKing = ImageIO.read(new File("resources/bKing.png"));
      wKing = ImageIO.read(new File("resources/wKing.png"));

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
      kingCoords.put(PlayerSide.BLACK, new Coordinates(0, 4));
      // white king and queen
      board[7][3] = new Queen(PlayerSide.WHITE, wQueen);
      board[7][4] = new King(PlayerSide.WHITE, wKing);
      kingCoords.put(PlayerSide.WHITE, new Coordinates(7, 4));

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

    return tryMove(currentTurn, fromRank, fromFile, toRank, toFile);
  }

  // Make sure that the move is possible and doesn't leave player's king in check, then move
  private int tryMove(PlayerSide currentTurn, int fromRank, int fromFile,
      int toRank, int toFile) throws ChessMoveException {
    AbstractGamePiece selectedPiece = board[fromRank][fromFile];
    // coordinates of the playing side's king
    Coordinates playingKingCoords = kingCoords.get(currentTurn).copy();
    // see if king is in check before move
    boolean inCheck = isCurrentlyInCheck(currentTurn, playingKingCoords, board);
    boolean movingKing = playingKingCoords.match(fromRank, fromFile);

    // see of it's a valid standard move
    if (selectedPiece.canMoveTo(fromRank, fromFile, toRank, toFile, board)) {
      AbstractGamePiece[][] boardCopy = getBoard();
      // simulate the move on a copy of the board
      simulateMove(fromRank, fromFile, toRank, toFile, boardCopy);
      // update the king coordinates if it was the move simulated
      if (movingKing) {
        playingKingCoords.update(toRank, toFile);
      }
      // now check if the move would leave the king in check
      boolean wouldBeInCheck = isCurrentlyInCheck(currentTurn, playingKingCoords, boardCopy);
      validateCheckScenario(inCheck, wouldBeInCheck);
      // since all checks have passed at this point, make the move for the player
      int takenPieceValue = 0;
      // if a piece is being taken, get its value for scoring
      if (board[toRank][toFile] != null) {
        takenPieceValue = board[toRank][toFile].value;
      }
      // now that checks have passed, execute the standard move
      executeMove(fromRank, fromFile, toRank, toFile);
      // update the king coordinates tracker if the king was moved
      if (movingKing) {
        kingCoords.get(currentTurn).update(toRank, toFile);
      }
      return takenPieceValue;
    }
    // the piece could not move to the given location
    else {
      throw new ChessMoveException("Invalid move for " + selectedPiece.toString());
    }
  }

  @Override
  public boolean shouldBePromoted(PlayerSide side, int rank, int file) {
    AbstractGamePiece piece = board[rank][file];
    boolean isPromotableAlly = piece != null && piece.side == side && piece.isPromotable();
    return isPromotableAlly && ((piece.side == PlayerSide.WHITE && rank == 0)
        || (piece.side == PlayerSide.BLACK && rank == 7));
  }

  @Override
  public boolean promote(String pieceChoice, int rank, int file) {
    AbstractGamePiece newPiece;
    AbstractGamePiece oldPiece = board[rank][file];
    // TODO: How do I want to deal with images? Constants sound good
    switch (pieceChoice) {
      case "rook":
        if (oldPiece.side == PlayerSide.BLACK) {
          newPiece = new Rook(oldPiece.side, bRook);
        }
        else {
          newPiece = new Rook(oldPiece.side, wRook);
        }
        break;
      case "bishop":
        if (oldPiece.side == PlayerSide.BLACK) {
          newPiece = new Bishop(oldPiece.side, bBishop);
        }
        else {
          newPiece = new Bishop(oldPiece.side, wBishop);
        }
        break;
      case "knight":
        if (oldPiece.side == PlayerSide.BLACK) {
          newPiece = new Knight(oldPiece.side, bKnight);
        }
        else {
          newPiece = new Knight(oldPiece.side, wKnight);
        }
        break;
      case "queen":
        if (oldPiece.side == PlayerSide.BLACK) {
          newPiece = new Queen(oldPiece.side, bQueen);
        }
        else {
          newPiece = new Queen(oldPiece.side, wQueen);
        }
        break;
      default:
        return false;
    }
    newPiece.hasMoved = true;
    board[rank][file] = newPiece;
    return true;
  }

  @Override
  public void attemptCastle(PlayerSide forSide, boolean longCastle) throws ChessMoveException {
    // First, check whether king has not moved
    Coordinates kingLoc = kingCoords.get(forSide);
    if (board[kingLoc.rank][kingLoc.file].hasMoved) {
      throw new ChessMoveException("Cannot castle if the king has already been moved");
    }
    // Make sure king is not currently in check
    if (isCurrentlyInCheck(forSide, kingLoc, board)) {
      throw new ChessMoveException("Cannot castle when currently in check");
    }

    System.out.printf("longCastle is %s\n", longCastle);
    // Attempt long castle towards file 0
    if (longCastle) {
      executeCastle(forSide, kingLoc, 0, kingLoc.file - 1, kingLoc.file - 2);
    } else {
      executeCastle(forSide, kingLoc, 7, kingLoc.file + 1, kingLoc.file + 2);
    }
  }

  // Make sure that there are no pieces in the way and that the king does not move through check
  private void executeCastle(PlayerSide currentTurn, Coordinates kingLoc, int rookFile,
      int firstMove, int secondMove) throws ChessMoveException {
    // make sure that there are no pieces in between the king and rook about to castle
    int start = Math.min(kingLoc.file, rookFile) + 1;
    int end = Math.max(kingLoc.file, rookFile);
    System.out.printf("King file is %d, rookFile is %d; Start is %d, end is %d.\n", kingLoc.file,
        rookFile, start, end);
    for (int i = start; i < end; i++) {
      if (board[kingLoc.rank][i] != null) {
        throw new ChessMoveException("Cannot castle with pieces in between");
      }
    }
    // next, make sure that the king does not go through check when moving to the new spot
    verifyCastleThroughCheck(currentTurn, kingLoc, firstMove, secondMove);
    // move king to new spot
    executeMove(kingLoc.rank, kingLoc.file, kingLoc.rank, secondMove);
    // move rook to new spot
    executeMove(kingLoc.rank, rookFile, kingLoc.rank, firstMove);
    // update king position
    kingCoords.get(currentTurn).update(kingLoc.rank, secondMove);
  }

  // make sure that the king would not pass through check while castling
  private void verifyCastleThroughCheck(PlayerSide currentTurn, Coordinates kingLoc,
      int firstMove, int secondMove) throws ChessMoveException {
    AbstractGamePiece[][] boardCopy = getBoard();
    simulateMove(kingLoc.rank, kingLoc.file, kingLoc.rank, firstMove, boardCopy);
    boolean validMoves = !isCurrentlyInCheck(currentTurn,
        new Coordinates(kingLoc.rank, firstMove), boardCopy);
    // attempt the second move
    if (validMoves) {
      simulateMove(kingLoc.rank, firstMove, kingLoc.rank, secondMove, boardCopy);
      validMoves = !isCurrentlyInCheck(currentTurn,
          new Coordinates(kingLoc.rank, secondMove), boardCopy);
    }
    if (!validMoves) {
      throw new ChessMoveException("Cannot castle through or into check");
    }
  }

  // Determine whether the king is currently in check
  private boolean isCurrentlyInCheck(PlayerSide sideToScan, Coordinates kingCoords,
      AbstractGamePiece[][] board) {
    // Check whether the king is currently in check
    for (int r = 0; r < 8; r++) {
      for (int f = 0; f < 8; f++) {
        AbstractGamePiece boardLocation = board[r][f];
        // see if enemy pieces can move to the king
        if (boardLocation != null && boardLocation.side != sideToScan) {
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
  private void validateCheckScenario(boolean inCheck, boolean wouldBeInCheck)
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
  public GameState scanForMates(PlayerSide currentTurn) {
    // use copy to avoid changing main tracker
    boolean canMove = ableToMove(currentTurn, kingCoords.get(currentTurn).copy());
    // If the side can't move, determine if checkmate or stalemate
    if (!canMove) {
      boolean currentlyInCheck = isCurrentlyInCheck(currentTurn, kingCoords.get(currentTurn),
          board);
      if (currentlyInCheck) {
        return GameState.CHECKMATE;
      }
      return GameState.STALEMATE;
    }
    // If a side can move, then we're still playing
    return GameState.PLAYING;
  }

  // true if the given side can make a move without putting themselves in check
  private boolean ableToMove(PlayerSide currentTurn, Coordinates currentKingCoords) {
    // Loop through every piece matching the playing side
    for (int r = 0; r < 8; r++) {
      for (int f = 0; f < 8; f++) {
        AbstractGamePiece fromLocation = board[r][f];
        // make sure that the boardLocation is an allied piece
        if (fromLocation != null && fromLocation.side == currentTurn) {
          // see if a black piece can move to any position to resolve check
          for (int toR = 0; toR < 8; toR++) {
            for (int toF = 0; toF < 8; toF++) {
              AbstractGamePiece toLocation = board[toR][toF];
              // make sure that the tryLocation is not an allied piece
              if ((toLocation == null || toLocation.side != currentTurn) &&
                  fromLocation.canMoveTo(r, f, toR, toF, board)) {
                // if the movement is possible, simulate the move and scan for check
                AbstractGamePiece[][] boardCopy = getBoard();
                simulateMove(r, f, toR, toF, boardCopy);
                // update the king position if it was moved in the simulated board
                if (currentKingCoords.match(r, f)) {
                  currentKingCoords.update(toR, toF);
                }
                // now see if the king is still in check
                if (!isCurrentlyInCheck(currentTurn, currentKingCoords, boardCopy)) {
                  // signal that the king can escape from check
                  return true;
                }
              }
            }
          }
        }
      }
    }
    // there was no move that allowed the king to escape from check
    return false;
  }

  // move the piece to the new space, settings the contents of the old space to null on game board
  private void executeMove(int fromR, int fromF, int toR, int toF) {
    board[fromR][fromF].hasMoved = true;
    board[toR][toF] = board[fromR][fromF];
    board[fromR][fromF] = null;
  }

  // simulate the move on the given board copy
  private void simulateMove(int fromR, int fromF, int toR, int toF, AbstractGamePiece[][] board) {
    board[fromR][fromF].hasMoved = true;
    board[toR][toF] = board[fromR][fromF];
    board[fromR][fromF] = null;
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
