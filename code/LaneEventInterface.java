import java.util.HashMap;

public interface LaneEventInterface extends java.rmi.Remote {
	int getFrameNum() throws java.rmi.RemoteException;

	HashMap getScore() throws java.rmi.RemoteException;

	int[] getCurScores() throws java.rmi.RemoteException;

	int getIndex() throws java.rmi.RemoteException;

	int getBall() throws java.rmi.RemoteException;

	int[][] getCumulScore() throws java.rmi.RemoteException;

	public Party getParty() throws java.rmi.RemoteException;

	Bowler getBowler() throws java.rmi.RemoteException;

}
