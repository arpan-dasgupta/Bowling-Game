import java.util.HashMap;

public class LaneEvent {

	private final Party p;
	public final int ball;
	public final Bowler bowler;
	public final int[][] cumulScore;
	private final HashMap score;
	private final int index;
	public final int frameNum;
	private final boolean mechProb;
	public boolean isstart = false;

	public LaneEvent(Party pty, int theIndex, Bowler theBowler, int[][] theCumulScore, HashMap theScore,
			int theFrameNum, int[] theCurScores, int theBall, boolean mechProblem, boolean isstart) {
		p = pty;
		index = theIndex;
		bowler = theBowler;
		cumulScore = theCumulScore;
		score = theScore;
		frameNum = theFrameNum;
		ball = theBall;
		mechProb = mechProblem;
		this.isstart = isstart;
	}

	public boolean isMechanicalProblem() {
		return mechProb;
	}

	public int getFrameNum() {

		return frameNum;
	}

	public HashMap getScore() {

		return score;
	}

//	public int[][] getCumulScore() {
//		return cumulScore;
//	}

	public Party getParty() {

		return p;
	}

	public Bowler getBowler() {

		return bowler;
	}

};
