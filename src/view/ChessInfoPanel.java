package view;

import util.PlayerSide;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class ChessInfoPanel extends JPanel{
  protected int whiteScore;
  protected int blackScore;
  protected PlayerSide currentTurn;

  ChessInfoPanel(Dimension size){
    this.setPreferredSize(size);
    // initialize current turn to white
    currentTurn = PlayerSide.WHITE;
    whiteScore = 0;
    blackScore = 0;
    // TODO: For in the future if we want to set a font size
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    this.setBackground(Color.gray);

    g2d.setStroke(new BasicStroke(0.1f));
    g2d.setColor(Color.BLACK);
    g2d.drawString("Current turn: " + currentTurn.name(), 1, 12);
    g2d.drawString("White " + whiteScore + " - " + blackScore + " Black", 1, 24);
    //g2d.fillRect(10, 10, 50, 10);
    //g2d.fillRect(10, 10, 50, 10);
  }
}

