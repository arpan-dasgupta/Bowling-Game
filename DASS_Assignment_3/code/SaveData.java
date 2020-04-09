import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

public class SaveData implements Serializable {
    String gameName;
    Party bowlers;
    int[][] cumulScores;
    int[] curScores;
    int[][] finalScores;
    int gameNumber;
    int count;
    int frame;
    HashMap<String,Object> scores;
    int ball;
    Bowler current;
    int bowlIndex;
//    int real_frame;
//    Vector<LaneObserver> subscribers;

    public SaveData(String gameName, Party bowlers, int[][] cumulScores, int[] curScores, int[][] finalScores, int gameNumber, int count, int frame, HashMap<String,Object> scores, int ball,Bowler curr, int bowlIndex) {
        this.gameName = gameName;
        this.bowlers = bowlers;
        this.cumulScores = cumulScores;
        this.curScores = curScores;
        this.finalScores = finalScores;
        this.gameNumber = gameNumber;
        this.count = count;
        this.frame = frame;
        this.scores = scores;
        this.ball = ball;
        this.bowlIndex =bowlIndex;
        this.current =curr;
//        this.real_frame = rf;
//        this.subscribers = subscribers;
    }
}
