/*

  To change this generated comment edit the template variable "typecomment":
  Window>Preferences>Java>Templates.
  To enable and disable the creation of type comments go to
  Window>Preferences>Java>Code Generation.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LaneStatusView implements ActionListener, LaneObserver, Observer {

	transient private final JPanel jp;

	transient final JLabel curBowler;
	transient private final JLabel pinsDown;
	transient private final JButton viewLane;
	transient private final JButton viewPinSetter, maintenance;
	transient private final JButton pause, save;

	private final PinSetterView psv;
	private final LaneView lv;
	private final Lane lane;
	int laneNum;

	boolean laneShowing;
	boolean psShowing;

	public LaneStatusView(final Lane lane, final int laneNum) {

		this.lane = lane;
		this.laneNum = laneNum;

		laneShowing = false;
		psShowing = false;

		psv = new PinSetterView(laneNum);
		final Pinsetter ps = lane.getPinsetter();
		ps.subscribe(psv);

		lv = new LaneView(lane, laneNum);
		lane.subscribe(lv);

		jp = new JPanel();
		jp.setLayout(new FlowLayout());
		final JLabel cLabel = new JLabel("Now Bowling: ");
		curBowler = new JLabel("(no one)");
		final JLabel fLabel = new JLabel("Foul: ");
		final JLabel foul = new JLabel(" ");
		final JLabel pdLabel = new JLabel("Pins Down: ");
		pinsDown = new JLabel("0");

		// Button Panel
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		new Insets(4, 4, 4, 4);

		viewLane = new JButton("View Lane");
		final JPanel viewLanePanel = new JPanel();
		viewLanePanel.setLayout(new FlowLayout());
		viewLane.addActionListener(this);
		viewLanePanel.add(viewLane);

		viewPinSetter = new JButton("Pinsetter");
		final JPanel viewPinSetterPanel = new JPanel();
		viewPinSetterPanel.setLayout(new FlowLayout());
		viewPinSetter.addActionListener(this);
		viewPinSetterPanel.add(viewPinSetter);

		maintenance = new JButton("     ");
		maintenance.setBackground(Color.GREEN);
		final JPanel maintenancePanel = new JPanel();
		maintenancePanel.setLayout(new FlowLayout());
		maintenance.addActionListener(this);
		maintenancePanel.add(maintenance);

		pause = new JButton("Pause/Resume");
		final JPanel pausePanel = new JPanel();
		pausePanel.setLayout(new FlowLayout());
		pause.addActionListener(this);
		pausePanel.add(pause);

		save = new JButton("Save");
		final JPanel savePanel = new JPanel();
		savePanel.setLayout(new FlowLayout());
		save.addActionListener(this);
		savePanel.add(save);

		viewLane.setEnabled(false);
		viewPinSetter.setEnabled(false);
		pause.setEnabled(false);
		save.setEnabled(false);

		buttonPanel.add(viewLanePanel);
		buttonPanel.add(viewPinSetterPanel);
		buttonPanel.add(maintenancePanel);
		buttonPanel.add(pausePanel);
		buttonPanel.add(savePanel);

		jp.add(cLabel);
		jp.add(curBowler);
		// jp.add( fLabel );
		// jp.add( foul );
		jp.add(pdLabel);
		jp.add(pinsDown);

		jp.add(buttonPanel);

	}

	public JPanel showLane() {
		return jp;
	}

	public void actionPerformed(final ActionEvent e) {
		firstEvent(e);
		if (e.getSource().equals(viewLane)) {
			if (lane.isPartyAssigned()) {
				if (!laneShowing) {
					lv.show();
					laneShowing = true;
				} else if (laneShowing) {
					lv.hide();
					laneShowing = false;
				}
			}
		}
		if (e.getSource().equals(maintenance)) {
			if (lane.isPartyAssigned()) {
					lane.unPauseGame();
					maintenance.setBackground(Color.GREEN);
			}
		}
		if(e.getSource().equals(pause)) {
			if (lane.isPartyAssigned()) {
				if(lane.isGameIsHalted()) lane.unPauseGame(); else lane.pauseGame();
			}
		}
		if(e.getSource().equals(save)){
			if(lane.isPartyAssigned()){
				try {
					lane.save();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private void firstEvent(final ActionEvent e) {
		if (lane.isPartyAssigned()) {
			if (e.getSource().equals(viewPinSetter)) {
				if (!psShowing) {
					psv.show();
					psShowing = true;
				} else if (psShowing) {
					psv.hide();
					psShowing = false;
				}
			}
		}
	}

	public void receiveLaneEvent(final LaneEvent le) {
		curBowler.setText(le.getBowler().getNickName());
		if (le.isMechanicalProblem() || lane.isGameIsHalted()) {
			maintenance.setBackground(Color.RED);
		}
		else{
			maintenance.setBackground(Color.GREEN);
		}
		if (!lane.isPartyAssigned()) {
			viewLane.setEnabled(false);
			viewPinSetter.setEnabled(false);
			pause.setEnabled(false);
			save.setEnabled(false);
		} else {
			viewLane.setEnabled(true);
			viewPinSetter.setEnabled(true);
			pause.setEnabled(true);
			save.setEnabled(true);
		}
	}

	public void receivePinsetterEvent(final PinsetterEvent pe) {
		pinsDown.setText((new Integer(pe.totalPinsDown())).toString());
		// foul.setText( ( new Boolean(pe.isFoulCommited()) ).toString() );

	}

}
