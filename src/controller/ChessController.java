package controller;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import model.AbstractGamePiece;
import model.ChessModel;
import model.ChessModelImpl;
import util.ChessMoveException;
import util.Coordinates;
import util.GameState;
import util.PlayerSide;
import view.BoardView;
import view.ChessView;

public class ChessController extends MouseAdapter implements BoardController {

  private ChessModel model;
  private BoardView view;
  private GameState gameState;
  private Coordinates moveFrom; // stores fromR, fromF if they exist, null otherwise
  private PlayerSide currentTurn;
  // track score for both sides
  private final HashMap<PlayerSide, Integer> score;
  // track the coordinates of both kings
  private final HashMap<PlayerSide, Coordinates> kingCoords;
  // input queue for promotion choice
  private volatile ArrayBlockingQueue<Character> inputQueue;

  public ChessController() {
    // initialize score keeping
    score = new HashMap<>();
    score.put(PlayerSide.WHITE, 0);
    score.put(PlayerSide.BLACK, 0);
    gameState = GameState.PLAYING; // initialize gamestate

    // capacity 1 blocking queue to get input from key listener and relay it to promotion logic
    inputQueue = new ArrayBlockingQueue<>(1);

    kingCoords = new HashMap<>();

    model = new ChessModelImpl(kingCoords);
    view = new ChessView();
    moveFrom = new Coordinates();
    view.setMouseListener(this);
    currentTurn = PlayerSide.WHITE;
  }

  @Override
  public void playGame() {
    view.updateGameScreen(model.getBoardIcons());
    view.displayBoard();
    view.displayInfo();
    // everything else his handled by the mouse listener
  }

  @Override
  // handle in mouseReleased rather than mouseClicked to make movement more forgiving
  public void mouseReleased(MouseEvent e) {
    if (gameState == GameState.PLAYING) {
      handleUserMove(e);
    }
  }


  // TODO: Could use mouseEntered/mouseExited to pause the timer if/when that's added
  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  // helper to handle click across mouse listeners
  private void handleUserMove(MouseEvent event) {
    int newR = event.getY() / 100;
    int newF = event.getX() / 100;
    // Don't move if clicking outside of the board
    if (!(newF < 8 && newR < 8)) {
      return;
    }
    AbstractGamePiece[][] board = model.getBoard();

    boolean possibleCastle = validateCastleConditions(newR, newF);
    // clicking on an allied piece while not attempting to castle
    if (!possibleCastle && board[newR][newF] != null && board[newR][newF].getSide() == currentTurn) {
      // if a from space has already been selected
      if (moveFrom.isValid() && moveFrom.match(newR, newF)) {
        moveFrom.invalidate();
      }
      // otherwise, just selected the clicked square as long as player isn't castling
      else {
        moveFrom.update(newR, newF);
      }
      view.setCurrentlySelected(moveFrom);
      view.setMessage("", Color.BLACK);
      view.displayBoard();
      view.displayInfo();
    }
    // valid move from selection cases, attempt to move
    else if (moveFrom.isValid() && !moveFrom.match(newR, newF)) {
      executeModelMove(possibleCastle, newR, newF);
    }
    // If the playing side has not yet selected a space
    else {
      // tell user to select from the playing side
      view.setMessage("Must select a " + currentTurn.name() + " piece to move",
          new Color(252, 123, 3));
    }
    view.displayInfo();
  }

  // Validate whether the user is attempting to castle
  private boolean validateCastleConditions(int newR, int newF) {
    // trying to move the king and king has not yet moved
    boolean movingKing = moveFrom.match(kingCoords.get(currentTurn));
    // moving either to the end rook or two spaces over
    boolean movingProperly = newR == moveFrom.rank && (newF == 0 || newF == 7 || Math.abs(newF - moveFrom.file) == 2);
    return movingKing && movingProperly;
  }

  // Execute the move within the model and relay any errors to the user
  private void executeModelMove(boolean potentialCastle, int newR, int newF) {
    // deselect a piece if its the same as the one currently highlighted
    if (newR == moveFrom.rank && newF == moveFrom.file) {
      view.setCurrentlySelected(new Coordinates());
    }
    // if from coords are selected and player is not clicking one of their pieces, attempt to move
    else {
      try {
        if (potentialCastle) {
          boolean longCastle = moveFrom.file > newF;
          System.out.printf("longCastle is %s, king rank is %d and rook rank is %d", longCastle, moveFrom.file, newF);
          model.attemptCastle(currentTurn, longCastle);
        } else {
          // move piece and update score
          int takenValue = model.movePiece(currentTurn, moveFrom.rank, moveFrom.file, newR, newF);
          score.replace(currentTurn, score.get(currentTurn) + takenValue);
          if (model.shouldBePromoted(currentTurn, newR, newF)) {
            handlePromotion(newR, newF);
          }
        }
        // update view information
        view.updateGameScreen(model.getBoardIcons());
        toggleTurn();
        updateGameState();
      } catch (ChessMoveException cme) {
        view.setMessage("Move Error - " + cme.getMessage(), new Color(252, 123, 3));
      } catch (IllegalArgumentException iae) {
        BoardView.throwErrorFrame("Position error", iae.getMessage());
      }
    }
  }

  private void updateGameState() {
    // now that a move has been made, see if the other side has been put into checkmate
    GameState stateCheck = model.scanForMates(currentTurn);
    // send message to user if game state has changed
    if (stateCheck != GameState.PLAYING) {
      if (stateCheck == GameState.CHECKMATE) {
        view.setMessage("Game over! " + currentTurn.name() + " has been checkmated.",
            new Color(8, 146, 8));
      } else if (stateCheck == GameState.STALEMATE) {
        view.setMessage("Game over - Stalemate! " + currentTurn.name() + " cannot move.!",
            new Color(255, 138, 0));
      }
      gameState = stateCheck;
    }
  }

  // Change to the other side's turn
  private void toggleTurn() {
    if (currentTurn == PlayerSide.WHITE) {
      currentTurn = PlayerSide.BLACK;
    } else {
      currentTurn = PlayerSide.WHITE;
    }
    // clear out info message
    view.setMessage("", Color.BLACK);
    view.setTurnInfo(currentTurn);
    // set score info
    view.setCurrentScore(score.get(PlayerSide.WHITE), score.get(PlayerSide.BLACK));
    // clear the moveFrom tracker and selection
    moveFrom.invalidate();
    view.setCurrentlySelected(moveFrom);
  }

  // handle promotion of the piece at the given location
  private void handlePromotion(int row, int col) {
    view.setMessage("Promotion: q) queen | r) rook | k) knight | b) bishop", Color.CYAN);
    view.displayInfo();
    // update view information
    view.updateGameScreen(model.getBoardIcons());
    view.displayBoard();
    boolean successfulPromotion = false;
    gameState = GameState.PROMOTING;
    System.out.println("Handling promotion");
    System.out.println("Beginning loop");
    String choice = view.getUserPromotionChoice();
    successfulPromotion = model.promote(choice, row, col);
    System.out.printf("Promotion success was: %s", successfulPromotion);

    gameState = GameState.PLAYING;
    System.out.println("Ended loop");
    view.setMessage("", Color.BLACK);
    view.displayInfo();
    view.displayBoard();
  }

  private class PromotionListener extends KeyAdapter {

    @Override
    public void keyTyped(KeyEvent e) {
      System.out.printf("In keypressed, keyEvent is %s\n", e.getKeyChar());

      try {
        ChessController.this.inputQueue.put(e.getKeyChar());
        /*if (ChessController.this.gameState == GameState.PROMOTING) {
          System.out.printf("Adding %s to queue\n", e.getKeyChar());
          ChessController.this.inputQueue.clear();
          ChessController.this.inputQueue.put(e.getKeyChar());
        }*/
      } catch (InterruptedException error) {
        BoardView.throwErrorFrame("Key read error", error.getMessage());
      }
    }
  }
}
