package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import util.Coordinates;
import util.PlayerSide;

public class ChessView extends JFrame implements BoardView {

  private Image[][] gameBoardIcons;

  // TODO: Why aren't these numbs the same if each box is a square? Check the math I'm using to calculate them
  private static final int FRAME_HEIGHT = 840; // NOTE: Currently needs 840 to fit on screen
  private static final int FRAME_WIDTH = 817;
  private JPanel mainPanel; // main panel to handle layouts
  private ChessBoardPanel boardPanel;
  private ChessInfoPanel infoPanel;


  public ChessView() {
    super();
    this.setTitle("Chess");
    this.setSize(ChessView.FRAME_WIDTH, ChessView.FRAME_HEIGHT + 40);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setResizable(false);
    // initialize main panel
    mainPanel = new JPanel();
    // set main panel layout
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    this.add(mainPanel);

    // add panel with main chess board
    boardPanel = new ChessBoardPanel(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
    mainPanel.add(boardPanel);

    // add panel with game info
    // set info panel with leftover screen size
    infoPanel = new ChessInfoPanel(new Dimension(FRAME_WIDTH, 40));
    mainPanel.add(infoPanel);
  }


  @Override
  public void updateGameScreen(Image[][] gameBoardIcons) {
    this.gameBoardIcons = gameBoardIcons;
    boardPanel.setBoardIcons(gameBoardIcons);
    boardPanel.repaint();
  }

  @Override
  public void displayBoard() {
    this.setVisible(true);
    this.boardPanel.setBoardIcons(gameBoardIcons);
    boardPanel.repaint();
  }

  // Redraw the displayInfo panel
  @Override
  public void displayInfo() {
    this.setVisible(true);
    infoPanel.repaint();
  }

  public void setTurnInfo(PlayerSide currentSide) {
    this.infoPanel.currentTurn = currentSide;
  }

  @Override
  public void setCurrentlySelected(Coordinates selected) {
    this.boardPanel.selectedBox = selected;
  }

  @Override
  public void setCurrentScore(int whiteScore, int blackScore) {
    this.infoPanel.whiteScore = whiteScore;
    this.infoPanel.blackScore = blackScore;
  }

  @Override
  public void setMessage(String msg, Color msgColor) {
    infoPanel.msgColor = msgColor;
    infoPanel.msgText = msg;
  }

  @Override
  public String getUserPromotionChoice() {
    // open the user dialogue, get the input
    PromotionWindow promotionWin = new PromotionWindow();
    return promotionWin.getUserChoice();
  }


  @Override
  public void setMouseListener(MouseListener l) {
    boardPanel.setBoardMouseListener(l);
  }

}



