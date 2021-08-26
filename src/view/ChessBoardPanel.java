package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import util.Coordinates;

public class ChessBoardPanel extends JPanel {
  // list of chess icons to display
  private Image[][] chessBoardIcons;
  // the currently selected box, if any
  protected Coordinates selectedBox;
  private static final Color LIGHT_TILE = new Color(217, 179, 130);
  private static final Color DARK_TILE = new Color(101, 67, 33);
  private static final Color SELECT_COLOR = new Color(251, 255, 130);
  private Color tileColor;


  public ChessBoardPanel(Dimension size) {
    this.setPreferredSize(size);
    tileColor = DARK_TILE;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    this.setBackground(Color.BLACK);

    g2d.setStroke(new BasicStroke(0.1f));
    if (chessBoardIcons != null) {
      for (int r = 0; r < 8; r++) {
        for (int f = 0; f < 8; f++) {
          // add the tile with proper color
          this.setTileColor();
          g2d.setColor(tileColor); // alternate tile colors
          // set width/height of 99 for black outline on each square
          g2d.fillRect(1 + f * 100, 1 + r * 100, 99, 99);
          // draw a piece image if present
          if (chessBoardIcons[r][f] != null) {
            // check whether this piece is the one selected before drawing icon
            if (selectedBox != null && r == this.selectedBox.rank && f == this.selectedBox.file) {
              g2d.setColor(SELECT_COLOR); // highlight the square of a selected piece
              g2d.fillRect(1 + f * 100, 1 + r * 100, 99, 99);
            }
            this.drawPieceAt(r, f, g2d);
          }
        }
        // offset the color order for the next row
        this.setTileColor();
        g2d.setColor(tileColor); // alternate tile colors
      }
    }
  }

  /**
   * Set the mouse listener for the board panel.
   * @param l the desired listener
   */
  public void setBoardMouseListener(MouseListener l) {
    this.addMouseListener(l);
  }


  // draw a piece's image at the given location on the g2d object
  private void drawPieceAt(int r, int f, Graphics2D g2d) {
    Image pieceImg = chessBoardIcons[r][f];

    // draw the piece image in the relative center of the tile
    g2d.drawImage(pieceImg, 20 + f * 100, 25 + r * 100, this);
    g2d.setColor(tileColor);
  }

  // set the proper tile color to alternate
  private void setTileColor() {
    if (tileColor.equals(LIGHT_TILE)) {
      tileColor = DARK_TILE;
    } else {
      tileColor = LIGHT_TILE;
    }
  }


  /**
   * Update the panel's stored chess board
   *
   * @param newBoardIcons the new board that will be displayed by the panel
   */
  public void setBoardIcons(Image[][] newBoardIcons) {
    chessBoardIcons = newBoardIcons;
  }
}
