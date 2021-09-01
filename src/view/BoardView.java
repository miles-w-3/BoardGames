package view;

import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Image;
import util.Coordinates;
import util.PlayerSide;

/**
 * Represents a View for the board games.
 */
public interface BoardView {

  /**
   * Displays a board and all of the pieces in play.
   */
  void displayBoard();

  /**
   * Displays game info such as player turn and score.
   */
  void displayInfo();

  /**
   * Set the current turn to display.
   */
  void setTurnInfo(PlayerSide currentSide);

  /**
   * Set the space which should be highlighted as currently selected.
   */
  void setCurrentlySelected(Coordinates selected);

  /**
   * Set the current score for both sides.
   */
  void setCurrentScore(int whiteScore, int blackScore);

  /**
   * Set a colored message for the user to view.
   * @param msg Message contents
   * @param msgColor Message color object
   */
  void setMessage(String msg, Color msgColor);

  /**
   * Updates the board state currently stored in the view.
   * @param gameBoardIcons the current board state
   */
  void updateGameScreen(Image[][] gameBoardIcons);

  /**
   * Create an error pop-up to display an error to the user.
   *
   * @param errorName    the name of the error
   * @param frameMessage the information about the error
   */
  static void throwErrorFrame(String errorName, String frameMessage) {
    JFrame frame = new JFrame();
    JOptionPane.showMessageDialog(frame, frameMessage, "Error: " + errorName,
        JOptionPane.ERROR_MESSAGE);
    frame.pack();
    frame.setVisible(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    System.exit(-1);
  }

  /**
   * Create a pop-up to display non-fatal warning to the user.
   *
   * @param errorName    the name of the error
   * @param frameMessage the information about the error
   */
  static void throwWarningFrame(String errorName, String frameMessage) {
    JFrame frame = new JFrame();
    JOptionPane.showMessageDialog(frame, frameMessage, "Error: " + errorName,
        JOptionPane.WARNING_MESSAGE);
    //frame.pack();
    //frame.setVisible(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * Set the mouse listener to interact with the view
   * @param l the desired listener
   */
  void setMouseListener(MouseListener l);

  /**
   * Open a dialogue window to get the user's choice for promotion and return it
   * @return the name of the piece that a pawn will be promoted to
   */
  String getUserPromotionChoice();

}
