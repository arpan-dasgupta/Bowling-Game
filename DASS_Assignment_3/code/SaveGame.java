import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

public class SaveGame {

    private static String SAVE_FILE_DAT = "SAVE.DAT";

    public static void saveGame(String nick, String date, String score)
            throws IOException {

        String data = nick + "\t" + date + "\t" + score + "\n";

        RandomAccessFile out = new RandomAccessFile(SAVE_FILE_DAT, "rw");
        out.skipBytes((int) out.length());
        out.writeBytes(data);
        out.close();
    }

    public static Vector<Score> loadGame(String game_name)
            throws IOException {
        Vector<Score> scores = new Vector<>();

        BufferedReader in =
                new BufferedReader(new FileReader(SAVE_FILE_DAT));
        String data = in.readLine();
        while ((data) != null) {
            // File format is nick\tfname\te-mail
            String[] scoredata = data.split("\t");
            //"Nick: scoredata[0] Date: scoredata[1] Score: scoredata[2]
            if (game_name.equals(scoredata[0])) {
                scores.add(new Score(scoredata[0], scoredata[1], scoredata[2]));
            }
            data = in.readLine();
        }
        return scores;
    }

}
