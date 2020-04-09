
import javax.swing.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Lane extends Thread implements Observer, Serializable {
	private Party party;
	private Pinsetter setter;
	public HashMap scores;
	public Vector<LaneObserver> subscribers;

	public boolean gameIsHalted;

	private boolean partyAssigned;
	private boolean gameFinished;
	// private Iterator bowlerIterator;
	public int ball;
	private int count;
	public int bowlIndex;
	private int frameNumber;
	private boolean tenthFrameStrike;

	private int[] curScores;
	public int[][] cumulScores;
	private boolean canThrowAgain;

	private int[][] finalScores;
	private int gameNumber;

	public int isStart;
	Scorer sc;

	private Bowler currentThrower; // = the thrower who just took a throw
	// private Scorer sc;

	/**
	 * Lane()
	 * 
	 * Constructs a new lane and starts its thread
	 * 
	 * @pre none
	 * @post a new lane has been created and its thered is executing
	 */
	public Lane() {
		setter = new Pinsetter();
		scores = new HashMap();
		subscribers = new Vector<LaneObserver>();

		gameIsHalted = false;
		partyAssigned = false;

		gameNumber = 0;
		count = 0;

		setter.subscribe(this);
		sc = new Scorer(this);

		this.start();
	}

	/**
	 * run()
	 * 
	 * entry point for execution of this lane
	 */
	public void run() {

		while (true) {
			if (partyAssigned && !gameFinished) { // we have a party on this lane,
				Vector<Bowler> members = party.getMembers();
				// so next bower can take a throw

				// if (bowlerIterator.hasNext()) {
				// currentThrower = (Bowler) bowlerIterator.next();
				if (count < members.size()) {

					currentThrower = members.get(count);
					count++;

					canThrowAgain = true;
					tenthFrameStrike = false;
					ball = 0;
					while (canThrowAgain) {
						setter.ballThrown(); // simulate the thrower's ball hiting
						ball++;
					}

					if (frameNumber == 9) {
						finalScores[bowlIndex][gameNumber] = cumulScores[bowlIndex][9];
						try {
							Date date = new Date();
							String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth()
									+ "/" + date.getDay() + "/" + (date.getYear() + 1900);
							ScoreHistoryFile.addScore(currentThrower.getNickName(), dateString,
									new Integer(cumulScores[bowlIndex][9]).toString());
						} catch (Exception e) {
							System.err.println("Exception in addScore. " + e);
						}
					}

					setter.reset();
					bowlIndex++;

				} else {
					frameNumber++;
					resetBowlerIterator();
					bowlIndex = 0;
					// count = 0 ;
					if (frameNumber > 9) {
						gameFinished = true;
						gameNumber++;
					}
					while (gameIsHalted) {
						try {
							sleep(10);
						} catch (Exception e) {
						}
					}
				}
			} else if (partyAssigned && gameFinished) {
				Vector<Bowler> members = party.getMembers();
				EndGamePrompt egp = new EndGamePrompt((members.get(0)).getNickName() + "'s Party");
				int result = egp.getResult();
				egp.destroy();
				egp = null;

				System.out.println("result was: " + result);

				if (result == 1) { // yes, want to play again
					resetScores();
					resetBowlerIterator();

				} else if (result == 2) {// no, dont want to play another game
					Vector<String> printVector;
					EndGameReport egr = new EndGameReport((members.get(0)).getNickName() + "'s Party", party);
					printVector = egr.getResult();
					partyAssigned = false;
					Iterator<Bowler> scoreIt = members.iterator();
					party = null;
					partyAssigned = false;

					sc.publish(lanePublish());

					int myIndex = 0;
					while (scoreIt.hasNext()) {
						Bowler thisBowler = scoreIt.next();
						ScoreReport sr = new ScoreReport(thisBowler, finalScores[myIndex++], gameNumber);
						// sr.sendEmail(thisBowler.getEmail());
						Iterator<String> printIt = printVector.iterator();
						while (printIt.hasNext()) {
							if (thisBowler.getNickName() == printIt.next()) {
								System.out.println("Printing " + thisBowler.getNickName());
								sr.sendPrintout();
							}
						}

					}
				}
			}

			try {
				sleep(10);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * recievePinsetterEvent()
	 * 
	 * recieves the thrown event from the pinsetter
	 *
	 * @pre none
	 * @post the event has been acted upon if desiered
	 * 
	 * @param pe The pinsetter event that has been received.
	 */
	public void receivePinsetterEvent(PinsetterEvent pe) {

		if (pe.pinsDownOnThisThrow() >= 0) { // this is a real throw
			System.out.println(pe.getThrowNumber() + " + ");
			sc.markScore(currentThrower, frameNumber + 1, pe.getThrowNumber(), pe.pinsDownOnThisThrow());

			// next logic handles the ?: what conditions dont allow them another throw?
			// handle the case of 10th frame first
			if (frameNumber == 9) {
				if (pe.totalPinsDown() == 10) {
					setter.resetPins();
					if (pe.getThrowNumber() == 1) {
						tenthFrameStrike = true;
					}
				}

				if ((pe.totalPinsDown() != 10) && (pe.getThrowNumber() == 2 && tenthFrameStrike == false)) {
					canThrowAgain = false;
					// publish( lanePublish() );
				}

				if (pe.getThrowNumber() == 3) {
					canThrowAgain = false;
					// publish( lanePublish() );
				}
			} else { // its not the 10th frame

				if (pe.pinsDownOnThisThrow() == 10) { // threw a strike
					canThrowAgain = false;
					// publish( lanePublish() );
				} else if (pe.getThrowNumber() == 2) {
					canThrowAgain = false;
					// publish( lanePublish() );
				} else if (pe.getThrowNumber() == 3)
					System.out.println("I'm here...");
			}
		} else { // this is not a real throw, probably a reset
		}
	}
	private void resetBowlerIterator() {
		count = 0;
		// bowlerIterator = (party.getMembers()).iterator();
	}

	private void resetScores() {

		for (Object o : party.getMembers()) {
			int[] toPut = new int[25];
			for (int i = 0; i != 25; i++) {
				toPut[i] = -1;
			}
			scores.put(o, toPut);
		}

		gameFinished = false;
		frameNumber = 0;
	}

	public void assignParty(Party theParty) {
		party = theParty;
		resetBowlerIterator();
		// count = 0;
		partyAssigned = true;

		curScores = new int[party.getMembers().size()];
		cumulScores = new int[party.getMembers().size()][10];
		finalScores = new int[party.getMembers().size()][128];
		gameNumber = 0;
		isStart = 0;

		resetScores();
	}

	public void loadParty(String gameName) throws IOException, ClassNotFoundException {
		SaveData sd;
		sd = SaveGame.loadGames(gameName);
		if (sd == null)
			return;
		resetBowlerIterator();

		party = sd.bowlers;
		cumulScores = sd.cumulScores;
		finalScores = sd.finalScores;
		gameNumber = sd.gameNumber;
		curScores = sd.curScores;
		ball = sd.ball;
		bowlIndex = 0;

		currentThrower = sd.current;
		System.out.println(Arrays.deepToString(cumulScores));
		System.out.println(bowlIndex);
		System.out.println(sd.bowlers.getMembers().get(0).getNickName());
		System.out.println(sd.bowlers.getMembers().get(1).getNickName());
		System.out.println(sd.scores.toString() + " poo ");
		gameFinished = false;
		for (Bowler o : party.getMembers()) {
			int[] toPut = new int[25];
			for (int i = 0; i != 25; i++) {
				toPut[i] = -1;
			}
			System.out.println(o.getNickName());
			scores.put(o, sd.scores.get(o.getNickName()));
		}
		System.out.println(scores.toString() + " poo ");

		frameNumber = sd.frame;
		isStart = 0;
		partyAssigned = true;
	}

	LaneEvent lanePublish() {
		LaneEvent laneEvent = new LaneEvent(party, bowlIndex, currentThrower, cumulScores, scores, frameNumber + 1,
				curScores, ball, gameIsHalted, isStart);
		if(isStart==0)
			isStart=1;;
		return laneEvent;
	}

	public Pinsetter getPinsetter() {
		return setter;
	}

	public boolean isGameIsHalted() {
		return gameIsHalted;
	}

	public boolean isPartyAssigned() {
		return partyAssigned;
	}

	public void pauseGame() {
		gameIsHalted = true;
		sc.publish(lanePublish());
	}

	/**
	 * Resume the execution of this game
	 */
	public void unPauseGame() {
		gameIsHalted = false;
		sc.publish(lanePublish());
	}

	public void save() throws IOException {
//		System.out.println(bowlIndex);
		gameIsHalted = true;
		sc.publish(lanePublish());
		String saveName = JOptionPane.showInputDialog("Enter Name of Save");

		HashMap<String,Object> newh = new HashMap<String, Object>();
		for (Bowler o : party.getMembers()) {
//			System.out.println(o.getNickName());
			newh.put(o.getNickName(), scores.get(o));
		}
//		System.out.println(party.getMembers().get(0).getNickName());
//		System.out.println(party.getMembers().get(1).getNickName());
//		System.out.println(newh + " oof ");

		SaveData ss = new SaveData(saveName, party, cumulScores, curScores, finalScores, gameNumber, count, frameNumber,
				newh, ball, currentThrower, bowlIndex);
		System.out.println("Ok I'll save");
		SaveGame.saveGame(ss);
	}

}