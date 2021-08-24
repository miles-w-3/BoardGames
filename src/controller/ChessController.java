package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import model.AbstractGamePiece;
import model.ChessModel;
import model.ChessModelImpl;
import view.BoardView;
import view.ChessView;

public class ChessController implements BoardController, MouseListener {

  private ChessModel model;
  private BoardView view;
  private Integer[] moveFrom; // stores fromR, fromF if they exist, null otherwise
  private boolean isWhiteSideTurn;


  public ChessController() {
    model = new ChessModelImpl();
    view = new ChessView();
    view.setMouseListener(this);
    moveFrom = new Integer[2];
    isWhiteSideTurn = true;
  }

  @Override
  public void playGame() {
    while(true) {
      view.updateGameScreen(model.getBoard());
      view.displayBoard();
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    int newR = e.getY() / 100;
    int newF = e.getX() / 100;

    //System.out.printf("NewR is %s, NewF is %s", newR, newF);

    // Don't move if clicking outside of the board
    if (!(newF < 8 && newR < 8)) {
      return;
    }
    AbstractGamePiece[][] board = model.getBoard();

    // make sure that the clicked from position has a piece in it and that it's on the team of the
    // person playing right now
    if (board[newR][newF] != null && board[newR][newF].movesFirst() == isWhiteSideTurn) {
      // deselect the previous piece if there is one
      if (moveFrom[0] != null && moveFrom[1] != null) {
        model.deSelect(moveFrom[0], moveFrom[1]);
      }
      moveFrom[0] = newR;
      moveFrom[1] = newF;
      model.select(moveFrom[0], moveFrom[1]);
    }
    // if the from move has been chosen, look for a to position
    else if (moveFrom[0] != null && moveFrom[1] != null) {
      // deselect a piece if its the same as the one currently highlighted
      if (newR == moveFrom[0] && newF == moveFrom[1]) {
        model.deSelect(moveFrom[0], moveFrom[1]);
      }
      try {
        model.movePiece(moveFrom[0], moveFrom[1], newR, newF);
        view.updateGameScreen(model.getBoard());
        isWhiteSideTurn = !isWhiteSideTurn;
        model.deSelect(newR, newF); // unhighlight after move
        moveFrom = new Integer[2];
      } catch (IllegalArgumentException iae) {
        // TODO: Move this to the status bar and avoid using a popup
        BoardView.throwWarningFrame("Move Error!", iae.getMessage());
      }
    }
    view.displayInfo();
  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }
}
