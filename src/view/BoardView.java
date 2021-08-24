package view;

import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.AbstractGamePiece;

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
   * Updates the board state currently stored in the view.
   * @param gameBoard the current board state
   */
  void updateGameScreen(AbstractGamePiece[][] gameBoard);

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
}
