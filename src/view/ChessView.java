package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Insets;
import javax.swing.JScrollPane;
import model.AbstractGamePiece;
import util.PlayerSide;

public class ChessView extends JFrame implements BoardView {

  private AbstractGamePiece[][] gameBoard;

  // TODO: Why aren't these numbs the same if each box is a square? Check the math I'm using to calculate them
  private static final int FRAME_HEIGHT = 840; // NOTE: Currently needs 840 to fit on screen
  private static final int FRAME_WIDTH = 817;
  private ChessBoardPanel boardPanel;
  private ChessInfoPanel infoPanel;


  public ChessView() {
    super();
    this.setTitle("Chess");
    this.setSize(ChessView.FRAME_WIDTH, ChessView.FRAME_HEIGHT + 60);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    // add panel with main chess board
    boardPanel = new ChessBoardPanel();
    // Don't want a scroller for now, working with fixed heights
    //JScrollPane scroller = new JScrollPane(this.boardPanel);
    //this.add(scroller, BorderLayout.CENTER);
    this.add(boardPanel); // instead, directly add board panel

    // add panel with game info
    // set info panel with leftover screen size
    // TODO: Figure out how to get on top of each other, maybe using BorderLayout
    // TODO: Answer: Create one unified ContentPanel using the BoxLayout and add all of the [anels to it
    infoPanel = new ChessInfoPanel(new Dimension(FRAME_WIDTH, 60));
    this.add(infoPanel);
    this.setResizable(false);

    J
  }


  @Override
  public void updateGameScreen(AbstractGamePiece[][] newGameBoard) {
    gameBoard = newGameBoard;
    boardPanel.setChessBoard(gameBoard);
    boardPanel.repaint();
  }

  @Override
  public void displayBoard() {
    this.setVisible(true);
    this.boardPanel.setChessBoard(gameBoard);
    boardPanel.repaint();
  }

  // Redraw the displayInfo panel
  public void displayInfo() {
    this.setVisible(true);
    infoPanel.repaint();
  }

  public void setTurn(PlayerSide currentSide) {
    //TODO: Set turn here
  }

  public void updateScore(int whiteScore, int blackScore) {
    // TODO: Draw the updated score here, circles represent each color
  }


  @Override
  public void setMouseListener(MouseListener l) {
    boardPanel.setBoardMouseListener(l);
  }
}



