package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import model.AbstractGamePiece;

public class ChessView extends JFrame implements BoardView {

  private AbstractGamePiece[][] gameBoard;
  private ChessPanel boardPanel;

  public ChessView() {
    super();
    this.setTitle("Chess");
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    boardPanel = new ChessPanel();
    JScrollPane scroller = new JScrollPane(this.boardPanel);
    this.add(scroller, BorderLayout.CENTER);
    this.setSize(820, 841);
    this.setResizable(false);


  }

  @Override
  public void displayBoard() {
    this.setVisible(true);
    boardPanel.setChessBoard(gameBoard);
    boardPanel.repaint();

  }

  @Override
  public void updateGameBoard(AbstractGamePiece[][] newGameBoard) {
    gameBoard = newGameBoard;
    boardPanel.setChessBoard(gameBoard);
    boardPanel.repaint();
  }

  @Override
  public void setMouseListener(MouseListener l) {
    boardPanel.setBoardMouseListener(l);
  }
}
