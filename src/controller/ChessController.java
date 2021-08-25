package controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import model.AbstractGamePiece;
import model.ChessModel;
import model.ChessModelImpl;
import util.ChessMoveException;
import util.Coordinate;
import util.PlayerSide;
import view.BoardView;
import view.ChessView;

public class ChessController implements BoardController, MouseListener {

  private ChessModel model;
  private BoardView view;
  private Coordinate moveFrom; // stores fromR, fromF if they exist, null otherwise
  private PlayerSide currentTurn;
  private HashMap<PlayerSide, Integer> score;


  public ChessController() {
    // initialize scorekeeping
    score = new HashMap<>();
    score.put(PlayerSide.WHITE, 0);
    score.put(PlayerSide.BLACK, 0);

    model = new ChessModelImpl();
    view = new ChessView();
    moveFrom = new Coordinate();
    view.setMouseListener(this);
    currentTurn = PlayerSide.WHITE;
  }

  @Override
  public void playGame() {
    while(true) {
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
    handleUserClick(e);
  }

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
      moveFrom.update(newR, newF);

      view.setCurrentlySelected(moveFrom);
    }
    // valid move from selection cases
    else if (moveFrom.isValid()) {
      // deselect a piece if its the same as the one currently highlighted
      if (newR == moveFrom.rank && newF == moveFrom.file) {
        view.setCurrentlySelected(new Coordinate());
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
          view.setMessage("Move Error - " + cme.getMessage(), Color.RED);
        } catch (IllegalArgumentException iae) {
          BoardView.throwErrorFrame("Position error", iae.getMessage());
        }
      }
    }
    view.displayInfo();
  }

  // Change to the other side's turn
  private void toggleTurn() {
    if (currentTurn == PlayerSide.WHITE) {
      currentTurn = PlayerSide.BLACK;
    }
    else {
      currentTurn = PlayerSide.WHITE;
    }
    // clear out info message
    view.setMessage("", Color.BLACK);
    view.setTurnInfo(currentTurn);
    // set score info
    view.setCurrentScore(score.get(PlayerSide.WHITE), score.get(PlayerSide.BLACK));
    // clear the moveFrom tracker and selection
    moveFrom = new Coordinate();
    view.setCurrentlySelected(moveFrom);
  }
}
