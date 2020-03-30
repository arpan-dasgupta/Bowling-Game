/*

  To change this generated comment edit the template variable "typecomment":
  Window>Preferences>Java>Templates.
  To enable and disable the creation of type comments go to
  Window>Preferences>Java>Code Generation.
 */

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndGamePrompt implements ActionListener {

	private JFrame win;
	private JButton yesButton, noButton;

	private int result;

	private String selectedNick, selectedMember;

	public EndGamePrompt(String partyName) {

		result = 0;

		win = new JFrame("Another Game for " + partyName + "?");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout(2, 1));

		// Label Panel
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new FlowLayout());

		JLabel message = new JLabel(
				"Party " + partyName + " has finished bowling.\nWould they like to bowl another game?");

		labelPanel.add(message);

		// Button Panel
		JPanel buttonPanel = buttonjPanel();

		// Clean up main panel
		colPanel.add(labelPanel);
		colPanel.add(buttonPanel);

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		centerWindow();

	}

	private void centerWindow() {
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		int screenWidth = winWidth(screenSize);
		int screenHeight = winHeight(screenSize);
//		int winHeight = (win.getSize().height) / 2;
		win.setLocation(
				screenWidth - winWidth(win.getSize()),
				screenHeight - winHeight(win.getSize()));
		win.setVisible(true);
	}

	private int winWidth(Dimension size) {
		return (size.width) / 2;
	}

	private int winHeight(Dimension size) {
		return (size.height) / 2;
	}

	@NotNull
	private JPanel buttonjPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));

		new Insets(4, 4, 4, 4);

		yesButton = new JButton("Yes");
		JPanel yesButtonPanel = new JPanel();
		yesButtonPanel.setLayout(new FlowLayout());
		yesButton.addActionListener(this);
		yesButtonPanel.add(yesButton);

		noButton = new JButton("No");
		JPanel noButtonPanel = new JPanel();
		noButtonPanel.setLayout(new FlowLayout());
		noButton.addActionListener(this);
		noButtonPanel.add(noButton);

		buttonPanel.add(yesButton);
		buttonPanel.add(noButton);
		return buttonPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(yesButton)) {
			result = 1;
		}
		if (e.getSource().equals(noButton)) {
			result = 2;
		}

	}

	public int getResult() {
		while (result == 0) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.err.println("Interrupted");
			}
		}
		return result;
	}

	public void destroy() {
		win.setVisible(false);
	}

}
