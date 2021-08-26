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
  protected Color msgColor;
  protected String msgText;
  protected PlayerSide currentTurn;

  ChessInfoPanel(Dimension size){
    this.setPreferredSize(size);
    // initialize current turn to white
    currentTurn = PlayerSide.WHITE;
    whiteScore = 0;
    blackScore = 0;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    this.setBackground(Color.white);

    g2d.setStroke(new BasicStroke(0.1f));
    g2d.setColor(Color.BLACK);
    g2d.drawString("Current turn: " + currentTurn.name(), 1, 12);
    g2d.drawString("White " + whiteScore + " - " + blackScore + " Black", 1, 24);

    if (msgText != null && msgColor != null) {
      g2d.setColor(msgColor);
      g2d.drawString(msgText, 1, 36);
    }
  }
}

