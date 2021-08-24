package view;

import util.PlayerSide;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class ChessInfoPanel extends JPanel{
  int whiteScore;
  int blackScore;
  PlayerSide currentTurn;

  ChessInfoPanel(Dimension size){
    this.setSize(size);
    this.setBackground(Color.LIGHT_GRAY);
    currentTurn = PlayerSide.WHITE;
    whiteScore = 0;
    blackScore = 0;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    this.setBackground(Color.LIGHT_GRAY);

    g2d.setStroke(new BasicStroke(0.1f));
    g2d.setColor(Color.CYAN);
    g2d.drawString("This is a test draw", 0, 0);
  }
}

