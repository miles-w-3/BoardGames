package view;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

class PromotionWindow extends JDialog {

  private volatile String choice;

  PromotionWindow() {
    this.setModal(true);
    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.setSize(new Dimension(350, 100));
    this.setTitle("Promotion Choice");
    this.setResizable(false);

    ActionListener buttonListener = new ButtonListener();

    // Create content panel and layouts
    JPanel contentPanel = new JPanel();
    FlowLayout flowLayout = new FlowLayout();
    contentPanel.setLayout(flowLayout);
    flowLayout.setAlignment(FlowLayout.TRAILING);
    contentPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

    // Initialize buttons
    JButton queen = new JButton("queen");
    queen.addActionListener(buttonListener);
    contentPanel.add(queen);
    JButton rook = new JButton("rook");
    rook.addActionListener(buttonListener);
    contentPanel.add(rook);
    JButton bishop = new JButton("bishop");
    bishop.addActionListener(buttonListener);
    contentPanel.add(bishop);
    JButton knight = new JButton("knight");
    knight.addActionListener(buttonListener);
    contentPanel.add(knight);
    this.add(contentPanel, BorderLayout.WEST);
    this.setVisible(true);
  }

  String getUserChoice() {
    return choice;
  }

  class ButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      choice = e.getActionCommand();
      // close the window since the choice has been given
      dispose();
    }
  }
}
