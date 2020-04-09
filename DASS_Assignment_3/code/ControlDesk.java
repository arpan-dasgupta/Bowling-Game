/* ControlDesk.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log: ControlDesk.java,v $
 * 		Revision 1.13  2003/02/02 23:26:32  ???
 * 		ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 * 		
 * 		Revision 1.12  2003/02/02 20:46:13  ???
 * 		Added " 's Party" to party names.
 * 		
 * 		Revision 1.11  2003/02/02 20:43:25  ???
 * 		misc cleanup
 * 		
 * 		Revision 1.10  2003/02/02 17:49:10  ???
 * 		Fixed problem in getPartyQueue that was returning the first element as every element.
 * 		
 * 		Revision 1.9  2003/02/02 17:39:48  ???
 * 		Added accessor for lanes.
 * 		
 * 		Revision 1.8  2003/02/02 16:53:59  ???
 * 		Updated comments to match javadoc format.
 * 		
 * 		Revision 1.7  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 		Revision 1.6  2003/02/02 06:09:39  ???
 * 		Updated many classes to support the ControlDeskView.
 * 		
 * 		Revision 1.5  2003/01/26 23:16:10  ???
 * 		Improved thread handeling in lane/controldesk
 * 		
 * 
 */

/*
  Class that represents control desk

 */

import java.util.*;
import java.io.*;

class ControlDesk extends Thread {

	/** The collection of Lanes */
	private HashSet<Lane> lanes;

	/** The party wait queue */
	private Queue partyQueue;

	/** The number of lanes represented */
	public int numLanes;

	/** The collection of subscribers */
	private Vector<ControlDeskObserver> subscribers;

	public ControlDesk(int numLanes) {
		this.numLanes = numLanes;
		lanes = new HashSet<>(numLanes);
		partyQueue = new Queue();
		subscribers = new Vector<>();

		for (int i = 0; i < numLanes; i++) {
			lanes.add(new Lane());
		}
		this.start();
	}

	public void run() {
		while (true) {
			assignLane();
			try {
				sleep(250);
			} catch (Exception ignored) {
			}
		}
	}

	private Bowler registerPatron(String nickName) {
		Bowler patron = null;
		try {
			// only one patron / nick.... no dupes, no checks
			patron = BowlerFile.getBowlerInfo(nickName);

		} catch (IOException e) {
			System.err.println("Error..." + e);
		}

		return patron;
	}

	public void assignLane() {
		Iterator<Lane> it = lanes.iterator();

		while (it.hasNext() && partyQueue.hasMoreElements()) {
			Lane curLane = it.next();

			if (!curLane.isPartyAssigned()) {
				System.out.println("ok... assigning this party");
				curLane.assignParty((partyQueue.next()));
			}
		}
		publish(new ControlDeskEvent(getPartyQueue()));
	}

	public void assignLane(String gameName) throws IOException, ClassNotFoundException {
		Iterator<Lane> it = lanes.iterator();

		while (it.hasNext()) {
			Lane curLane = it.next();

			if (!curLane.isPartyAssigned()) {
				System.out.println("ok... assigning this party");
//				curLane.assignParty((partyQueue.next()));
				curLane.loadParty(gameName);
				break;
			}
		}
		publish(new ControlDeskEvent(getPartyQueue()));
	}

	public void addPartyQueue(Vector<String> partyNicks) {
		Vector<Bowler> partyBowlers = new Vector<>();
		for (String partyNick : partyNicks) {
			Bowler newBowler = registerPatron(partyNick);
			partyBowlers.add(newBowler);
		}
		Party newParty = new Party(partyBowlers);
		partyQueue.add(newParty);
		publish(new ControlDeskEvent(getPartyQueue()));
	}

	public Vector<String> getPartyQueue() {
		Vector<String> displayPartyQueue = new Vector<>();
		for (int i = 0; i < partyQueue.asVector().size(); i++) {
			String nextParty = ((Bowler) (partyQueue.asVector().get(i)).getMembers().get(0)).getNickName() + "'s Party";
			displayPartyQueue.addElement(nextParty);
		}
		return displayPartyQueue;
	}

//	public int getNumLanes() {
//		return numLanes;
//	}


	public void subscribe(ControlDeskObserver adding) {
		subscribers.add(adding);
	}


	public void publish(ControlDeskEvent event) {
		for (ControlDeskObserver subscriber : subscribers) {
			subscriber.receiveControlDeskEvent(event);
		}
	}

	public HashSet<? extends Lane> getLanes() {
		return (HashSet<Lane>) lanes.clone();
	}
}
