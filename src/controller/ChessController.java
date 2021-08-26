package controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import model.AbstractGamePiece;
import model.ChessModel;
import model.ChessModelImpl;
import util.ChessMoveException;
import util.Coordinates;
import util.GameState;
import util.PlayerSide;
import view.BoardView;
import view.ChessView;

public class ChessController implements BoardController, MouseListener {

  private ChessModel model;
  private BoardView view;
  private GameState gameState;
  private Coordinates moveFrom; // stores fromR, fromF if they exist, null otherwise
  private PlayerSide currentTurn;
  private HashMap<PlayerSide, Integer> score;


  public ChessController() {
    // initialize score keeping
    score = new HashMap<>();
    score.put(PlayerSide.WHITE, 0);
    score.put(PlayerSide.BLACK, 0);
    gameState = GameState.PLAYING;

    model = new ChessModelImpl();
    view = new ChessView();
    moveFrom = new Coordinates();
    view.setMouseListener(this);
    currentTurn = PlayerSide.WHITE;
  }

  @Override
  public void playGame() {
    while (true) {
      view.updateGameScreen(model.getBoardIcons());
      view.displayBoard();
      view.displayInfo();
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  // handle in mouseReleased rather than mouseClicked to make movement more forgiving
  public void mouseReleased(MouseEvent e) {
    if (gameState == GameState.PLAYING) {
      handleUserClick(e);
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
  private void handleUserClick(MouseEvent event) {
    int newR = event.getY() / 100;
    int newF = event.getX() / 100;
    // Don't move if clicking outside of the board
    if (!(newF < 8 && newR < 8)) {
      return;
    }
    AbstractGamePiece[][] board = model.getBoard();

    // highlight a clicked location if it contains a piece from the playing side
    if (board[newR][newF] != null && board[newR][newF].getSide() == currentTurn) {
      // if clicking on the coordinates that are already selected, deselect
      if (moveFrom.isValid() && moveFrom.match(newR, newF)) {
        moveFrom.invalidate();
      }
      else {
        moveFrom.update(newR, newF);
      }
      view.setCurrentlySelected(moveFrom);
      view.setMessage("", Color.BLACK);
    }
    // valid move from selection cases, attempt to move
    else if (moveFrom.isValid()) {
      executeModelMove(newR, newF);
      // now that a move has been made, see if the other side has been put into checkmate
      GameState stateCheck = model.scanForMates(currentTurn);
      // send message to user if game state has changed
      if (stateCheck != GameState.PLAYING) {
        if (stateCheck == GameState.CHECKMATE) {
          view.setMessage("Game over! " + currentTurn.name() + " has been checkmated.",
              new Color(8, 146, 8));
        } else if (stateCheck == GameState.STALEMATE) {
          view.setMessage("Stalemate! " + currentTurn.name() + " cannot move.!",
              new Color(255, 138, 0));
        }
        gameState = stateCheck;
      }
    }
    // If the user is not selecting one of their pieces to move
    else {
      view.setMessage("Must select a " + currentTurn.name() + " piece to move",
          new Color(252, 123, 3));
    }
    view.displayInfo();
  }

  // Execute the move within the model and relay any errors to the user
  private void executeModelMove(int newR, int newF) {
    // deselect a piece if its the same as the one currently highlighted
    if (newR == moveFrom.rank && newF == moveFrom.file) {
      view.setCurrentlySelected(new Coordinates());
    }
    // if from coords are selected and player is not clicking one of their pieces, attempt to move
    else {
      try {
        // move piece and update score
        int takenValue = model.movePiece(currentTurn, moveFrom.rank, moveFrom.file, newR, newF);
        if (takenValue > 0) {
          score.replace(currentTurn, score.get(currentTurn) + takenValue);
        }
        // update view information
        view.updateGameScreen(model.getBoardIcons());
        toggleTurn();
      } catch (ChessMoveException cme) {
        view.setMessage("Move Error - " + cme.getMessage(), new Color(252, 123, 3));
      } catch (IllegalArgumentException iae) {
        BoardView.throwErrorFrame("Position error", iae.getMessage());
      }
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
    moveFrom = new Coordinates();
    view.setCurrentlySelected(moveFrom);
  }
}
