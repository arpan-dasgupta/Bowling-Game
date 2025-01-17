/*

  To change this generated comment edit the template variable "typecomment":
  Window>Preferences>Java>Templates.
  To enable and disable the creation of type comments go to
  Window>Preferences>Java>Code Generation.
 */

//import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class EndGameReport implements ActionListener, ListSelectionListener {

	private JFrame win;
	private JButton printButton, finished;
	private Vector<String> retVal;

	private int result;

	private String selectedMember;

	public EndGameReport(String partyName, Party party) {

		result = 0;
		retVal = new Vector<>();
		win = new JFrame("End Game Report for " + partyName + "?");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout(1, 2));

		// Member Panel
		JPanel partyPanel = memberPanel(party);

		// Button Panel
		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 1));

		new Insets(4, 4, 4, 4);

		printButton = new JButton("Print Report");
		JPanel printButtonPanel = new JPanel();
		printButtonPanel.setLayout(new FlowLayout());
		printButtonPanel.add(printButton);

		finished = new JButton("Finished");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);

		buttonPanel.add(printButton);
		buttonPanel.add(finished);

		// Clean up main panel
		colPanel.add(partyPanel);
		colPanel.add(buttonPanel);

		win.getContentPane().add("Center", colPanel);

		win.pack();
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

//	@NotNull
	private JPanel memberPanel(Party party) {
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Party Members"));

		Vector<String> myVector = new Vector<>();
		for (Object o : party.getMembers()) {
			myVector.add(((Bowler) o).getNickName());
		}
		JList<String> memberList = new JList<>(myVector);
		memberList.setFixedCellWidth(120);
		memberList.setVisibleRowCount(5);
		memberList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(memberList);
		// partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);

		partyPanel.add(memberList);
		return partyPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(printButton)) {
			// Add selected to the vector.
			retVal.add(selectedMember);
		}
		if (e.getSource().equals(finished)) {
			win.setVisible(false);
			result = 1;
		}

	}

	public void valueChanged(ListSelectionEvent e) {
		selectedMember = ((String) ((JList) e.getSource()).getSelectedValue());
	}

	public Vector<String> getResult() {
		while (result == 0) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.err.println("Interrupted");
			}
		}
		return (Vector<String>) retVal.clone();
	}

	public void destroy() {
		win.setVisible(false);
	}

	public static void main(String[] args) {
		Vector<Bowler> bowlers = new Vector<>();
		for (int i = 0; i < 4; i++) {
			bowlers.add(new Bowler("aaaaa", "aaaaa", "aaaaa"));
		}
		Party party = new Party(bowlers);
		String partyName = "wank";
		new EndGameReport(partyName, party);
	}

}
