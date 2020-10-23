package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import model.AbstractGamePiece;

public class ChessPanel extends JPanel {

  private AbstractGamePiece[][] chessBoard;
  private static Color LIGHT_TILE = new Color(217, 179, 130);
  private static Color DARK_TILE = new Color(101, 67, 33);
  private Color tileColor;
  private Image bRook;
  private Image wRook;
  private Image bKnight;
  private Image wKnight;
  private Image bBishop;
  private Image wBishop;
  private Image bQueen;
  private Image wQueen;
  private Image bKing;
  private Image wKing;
  private Image bPawn;
  private Image wPawn;


  public ChessPanel() {
    tileColor = DARK_TILE;
    this.loadPieceImages();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    this.setBackground(Color.BLACK);

    g2d.setStroke(new BasicStroke(0.1f));
    if (chessBoard != null) {
      for (int r = 0; r < 8; r++) {
        for (int f = 0; f < 8; f++) {
          // add the tile with proper color
          this.setTileColor();
          g2d.setColor(tileColor); // alternate tile colors
          g2d.fillRect(1 + f * 100, 1 + r * 100, 99, 99);
          // add a chess piece if present
          if (chessBoard[r][f] != null) {
            if (chessBoard[r][f].isSelected()) {
              g2d.setColor(Color.red); // highlight the square of a selected piece
              g2d.fillRect(1 + f * 100, 1 + r * 100, 99, 99);
            }
            g2d = this.addPieceAt(r, f, g2d);
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


  // add a piece's image at the given location to the graphics object
  private Graphics2D addPieceAt(int r, int f, Graphics2D g2d) {
    AbstractGamePiece currentPiece = chessBoard[r][f];
    String pieceName = currentPiece.toString();
    Image toDraw;
    // piece is white
    if (currentPiece.movesFirst()) {
      toDraw = getImage(pieceName, wBishop, wRook, wKnight, wQueen, wKing, wPawn);
    }
    // piece is black
    else {
      toDraw = getImage(pieceName, bBishop, bRook, bKnight, bQueen, bKing, bPawn);
    }
    g2d.drawImage(toDraw, 20 + f * 100, 25 + r * 100, this);
    //dg2d.fillOval(1 + f * 100, 1 + r * 100, 20, 20);
    g2d.setColor(tileColor);
    return g2d;
  }

  private Image getImage(String pieceName, Image bishop, Image rook, Image knight, Image queen,
      Image king, Image pawn) {
    Image toDraw;
    switch (pieceName) {
      case "bishop":
        toDraw = bishop;
        break;
      case "rook":
        toDraw = rook;
        break;
      case "knight":
        toDraw = knight;
        break;
      case "queen":
        toDraw = queen;
        break;
      case "king":
        toDraw = king;
        break;
      default:
        toDraw = pawn;
    }
    return toDraw;
  }

  // set the proper tile color to alternate
  private void setTileColor() {
    if (tileColor.equals(LIGHT_TILE)) {
      tileColor = DARK_TILE;
    } else {
      tileColor = LIGHT_TILE;
    }
  }

  private void loadPieceImages() {
    try {
      bRook = ImageIO.read(new File("resources/bRook.png"));
      wRook = ImageIO.read(new File("resources/wRook.png"));
      bKnight = ImageIO.read(new File("resources/bKnight.png"));
      wKnight = ImageIO.read(new File("resources/wKnight.png"));
      bBishop = ImageIO.read(new File("resources/bBish.png"));
      wBishop = ImageIO.read(new File("resources/wBish.png"));
      bQueen = ImageIO.read(new File("resources/bQueen.png"));
      wQueen = ImageIO.read(new File("resources/wQueen.png"));
      bPawn = ImageIO.read(new File("resources/bPawn.png"));
      wPawn = ImageIO.read(new File("resources/wPawn.png"));
      bKing = ImageIO.read(new File("resources/bKing.png"));
      wKing = ImageIO.read(new File("resources/wKing.png"));
    } catch (IOException ex) {
      BoardView.throwErrorFrame("Error!", "Unable to load essential images.");
    }
  }

  /**
   * Update the panel's stored chess board
   *
   * @param newChessBoard the new board that will be displayed by the panel
   */
  public void setChessBoard(AbstractGamePiece[][] newChessBoard) {
    chessBoard = newChessBoard;
  }
}
