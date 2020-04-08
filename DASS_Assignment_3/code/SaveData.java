import java.io.Serializable;
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

    public SaveData(String gameName, Party bowlers, int[][] cumulScores, int[] curScores, int[][] finalScores, int gameNumber, int count, int frame) {
        this.gameName = gameName;
        this.bowlers = bowlers;
        this.cumulScores = cumulScores;
        this.curScores = curScores;
        this.finalScores = finalScores;
        this.gameNumber = gameNumber;
        this.count = count;
        this.frame = frame;
    }
}
