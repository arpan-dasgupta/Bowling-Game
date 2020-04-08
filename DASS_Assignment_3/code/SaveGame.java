import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

public class SaveGame {

    private static String SAVE_FILE_DAT = "SAVE.DAT";

//    public static void saveGame(Party p, int[][] cumulScores, int[][] curScores, int[][] finalScores, int gameNumber, Iterator bowlerIterator)
//            throws IOException {
//
//        String data = "2";
//
//        RandomAccessFile out = new RandomAccessFile(SAVE_FILE_DAT, "rw");
//        out.skipBytes((int) out.length());
//        out.writeBytes(data);
//        out.close();
//    }


    public static void saveGame(Party p, int[][] cumulScores, int[] curScores, int[][] finalScores, int gameNumber, Iterator bowlerIterator)
//    public static void saveGame(Lane l)
            throws IOException {

//            String data = ;

        try{
            FileOutputStream out = new FileOutputStream(SAVE_FILE_DAT);
            ObjectOutputStream obj = new ObjectOutputStream(out);
            obj.writeObject(p);
            obj.writeObject(cumulScores);
//            obj.writeObject(bowlerIterator);
            obj.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

//    public static Vector<Score> loadGames(String game_name)
//            throws IOException {
//        Vector<Score> scores = new Vector<>();
//
//        BufferedReader in =
//                new BufferedReader(new FileReader(SAVE_FILE_DAT));
//        String data = in.readLine();
//        while ((data) != null) {
//            // File format is nick\tfname\te-mail
//            String[] scoredata = data.split("\t");
//            //"Nick: scoredata[0] Date: scoredata[1] Score: scoredata[2]
//            if (game_name.equals(scoredata[0])) {
//                scores.add(new Score(scoredata[0], scoredata[1], scoredata[2]));
//            }
//            data = in.readLine();
//        }
//        return scores;
//    }


    public static Vector<Score> loadGames()
            throws IOException, ClassNotFoundException {
        Vector<Score> scores = new Vector<>();

        try{
            FileInputStream is = new FileInputStream(SAVE_FILE_DAT);
            ObjectInputStream ois = new ObjectInputStream(is);
            Party p = (Party) ois.readObject();
            int[][] cumulScores = (int[][]) ois.readObject();
            System.out.println(p.toString());
            System.out.println(Arrays.deepToString(cumulScores));
            ois.close();
            is.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
//        BufferedReader in =
//                new BufferedReader(new FileReader(SAVE_FILE_DAT));
//        String data = in.readLine();
//        while ((data) != null) {
//            // File format is nick\tfname\te-mail
//            String[] scoredata = data.split("\t");
//            //"Nick: scoredata[0] Date: scoredata[1] Score: scoredata[2]
//            if (game_name.equals(scoredata[0])) {
//                scores.add(new Score(scoredata[0], scoredata[1], scoredata[2]));
//            }
//            data = in.readLine();
//        }
        return scores;
    }
}
