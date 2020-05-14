/* AddPartyView.java
 *
 *  Version:
 * 		 $Id$
 *
 *  Revisions:
 * 		$Log: AddPartyView.java,v $
 * 		Revision 1.7  2003/02/20 02:05:53  ???
 * 		Fixed addPatron so that duplicates won't be created.
 *
 * 		Revision 1.6  2003/02/09 20:52:46  ???
 * 		Added comments.
 *
 * 		Revision 1.5  2003/02/02 17:42:09  ???
 * 		Made updates to migrate to observer model.
 *
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 *
 *
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *
 */

public class AddPartyView implements ActionListener, ListSelectionListener {

	private int maxSize;

	private JFrame win;
	private JButton addPatron, newPatron, remPatron, finished;
	private JList<String> partyList;
	private JList<Object> allBowlers;
	private Vector<String> party;
	private Vector<Object> bowlerDB;
	private Integer lock;

	private ControlDeskView controlDesk;

	private String selectedNick, selectedMember;

	public AddPartyView(ControlDeskView controlDesk, int max) {

		this.controlDesk = controlDesk;
		maxSize = max;

		win = new JFrame("Add Party");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout(1, 3));

		// Party Panel
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Your Party"));

		party = new Vector<>();
		Vector<String> empty = new Vector<>();
		empty.add("(Empty)");

		partyList = new JList<>(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(5);
		partyList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(partyList);
		// partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);

		// Bowler Database
		JPanel bowlerPanel = new JPanel();
		bowlerPanel.setLayout(new FlowLayout());
		bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

		try {
			bowlerDB = new Vector<>(BowlerFile.getBowlers());
		} catch (Exception e) {
			System.err.println("File Error");
			bowlerDB = new Vector();
		}
		allBowlers = new JList(bowlerDB);
		allBowlers.setVisibleRowCount(8);
		allBowlers.setFixedCellWidth(120);
		JScrollPane bowlerPane = new JScrollPane(allBowlers);
		bowlerPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		allBowlers.addListSelectionListener(this);
		bowlerPanel.add(bowlerPane);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));

		new Insets(4, 4, 4, 4);

		addPatron = new JButton("Add to Party");
		JPanel addPatronPanel = new JPanel();
		addPatronPanel.setLayout(new FlowLayout());
		addPatron.addActionListener(this);
		addPatronPanel.add(addPatron);

		remPatron = new JButton("Remove Member");
		JPanel remPatronPanel = new JPanel();
		remPatronPanel.setLayout(new FlowLayout());
		remPatron.addActionListener(this);
		remPatronPanel.add(remPatron);

		newPatron = new JButton("New Patron");
		JPanel newPatronPanel = new JPanel();
		newPatronPanel.setLayout(new FlowLayout());
		newPatron.addActionListener(this);
		newPatronPanel.add(newPatron);

		finished = new JButton("Finished");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);

		Drawer d = new Drawer();
		d.addPartyPanels(colPanel, partyPanel, bowlerPanel, buttonPanel, addPatronPanel, remPatronPanel, newPatronPanel,
				finishedPanel);
		d.windowPos(colPanel, win);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addPatron)) {
			if (selectedNick != null && party.size() < maxSize) {
				if (party.contains(selectedNick)) {
					System.err.println("Member already in Party");
				} else {
					party.add(selectedNick);
					partyList.setListData(party);
				}
			}
		}
		if (e.getSource().equals(remPatron)) {
			if (selectedMember != null) {
				party.removeElement(selectedMember);
				partyList.setListData(party);
			}
		}
		if (e.getSource().equals(newPatron)) {
			NewPatronView newPatron = new NewPatronView(this);
		}
		if (e.getSource().equals(finished)) {
			if (party != null && party.size() > 0) {
				controlDesk.updateAddParty(this);
			}
			win.hide();
		}

	}

	/**
	 * Handler for List actions
	 * 
	 * @param e the ListActionEvent that triggered the handler
	 */

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(allBowlers)) {
			selectedNick = ((String) ((JList) e.getSource()).getSelectedValue());
		}
		if (e.getSource().equals(partyList)) {
			selectedMember = ((String) ((JList) e.getSource()).getSelectedValue());
		}
	}

	/**
	 * Accessor for Party
	 */

	public Vector<String> getNames() {
		return (Vector<String>) party.clone();
	}

	/**
	 * Called by NewPatronView to notify AddPartyView to update
	 *
	 * @param newPatron the NewPatronView that called this method
	 */

	public void updateNewPatron(NewPatronView newPatron) {
		try {
			Bowler checkBowler = BowlerFile.getBowlerInfo(newPatron.getNickName());
			if (checkBowler == null) {
				BowlerFile.putBowlerInfo(newPatron.getNickName(), newPatron.getFull(), newPatron.getEmail());
				bowlerDB = new Vector<Object>(BowlerFile.getBowlers());
				allBowlers.setListData(bowlerDB);
				party.add(newPatron.getNickName());
				partyList.setListData(party);
			} else {
				System.err.println("A Bowler with that name already exists.");
			}
		} catch (Exception e2) {
			System.err.println("File I/O Error");
		}
	}

	/**
	 * Accessor for Party
	 */

	public Vector<String> getParty() {
		return (Vector<String>) party.clone();
	}

}