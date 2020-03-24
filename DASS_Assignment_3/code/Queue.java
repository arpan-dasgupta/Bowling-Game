/* Queue.java
 *
 *  Version
 *  $Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */
 
import java.util.Vector;
 
public class Queue {
	private Vector<Party> v;
	
	/** Queue()
	 * 
	 * creates a new queue
	 */
	public Queue() {
		v = new Vector<>();
	}
	
	public Party next() {
		return v.remove(0);
	}

	public void add(Party o) {
		v.addElement(o);
	}
	
	public boolean hasMoreElements() {
		return v.size() != 0;
	}

	public Vector<Party> asVector() {
		return (Vector<Party>) v.clone();
	}
	
}
