import java.io.*;
import java.util.Arrays;
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


    public static void saveGame(String gameName,Vector<? extends Object> bowlers, int[][] cumulScores, int[] curScores, int[][] finalScores, int gameNumber, int count, int frame)
//    public static void saveGame(Lane l)
            throws IOException {

//            String data = ;

        try{
            File file = new File(SAVE_FILE_DAT);
            boolean append = file.exists();
            FileOutputStream out = new FileOutputStream(new File(SAVE_FILE_DAT),append);
//            ObjectOutputStream obj = new ObjectOutputStream(out);
            AppendableObjectOutputStream obj = new AppendableObjectOutputStream(out,append);
            obj.writeObject(gameName);
            obj.writeObject(bowlers);
            obj.writeObject(cumulScores);
            obj.writeObject(curScores);
            obj.writeObject(finalScores);
            obj.writeObject(gameNumber);
            obj.writeObject(count);
            obj.writeObject(frame);
//            obj.writeObject(bowlerIterator);
            obj.close();
            out.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveGame(SaveData sd)
//    public static void saveGame(Lane l)
            throws IOException {

//            String data = ;

        try{
            File file = new File(SAVE_FILE_DAT);
            boolean append = file.exists();
            FileOutputStream out = new FileOutputStream(new File(SAVE_FILE_DAT),append);
//            ObjectOutputStream obj = new ObjectOutputStream(out);
            AppendableObjectOutputStream obj = new AppendableObjectOutputStream(out,append);
            obj.writeObject(sd);
//            obj.writeObject(bowlerIterator);
            obj.close();
            out.close();
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


//    public static Vector<Score> loadGames(String gameName)
//            throws IOException, ClassNotFoundException {
//        Vector<Score> scores = new Vector<>();
//        String curName;
//        int[][] cumulScores;
//        int[] curScores;
//        int[][] finalScores;
//        int gameNumber;
//        int count;
//        int frame;
//        try{
//            FileInputStream is = new FileInputStream(SAVE_FILE_DAT);
//            ObjectInputStream ois = new ObjectInputStream(is);
//            try{
//                do {
//                    curName = (String) ois.readObject();
//                    System.out.println(curName);
//                    Vector<Bowler> members = (Vector<Bowler>) ois.readObject();
//                    cumulScores = (int[][]) ois.readObject();
//                    curScores = (int[]) ois.readObject();
//                    finalScores = (int[][]) ois.readObject();
//                    gameNumber = (int) ois.readObject();
//                    count = (int) ois.readObject();
//                    frame = (int) ois.readObject();
//                    System.out.println(members.get(0).getNickName());
//                    System.out.println(Arrays.deepToString(cumulScores));
//                }while (!gameName.equals(curName));
//
//            }catch (Exception e){
////                e.printStackTrace();
//                System.out.println("Not found!");
//            }
//            ois.close();
//            is.close();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
////        BufferedReader in =
////                new BufferedReader(new FileReader(SAVE_FILE_DAT));
////        String data = in.readLine();
////        while ((data) != null) {
////            // File format is nick\tfname\te-mail
////            String[] scoredata = data.split("\t");
////            //"Nick: scoredata[0] Date: scoredata[1] Score: scoredata[2]
////            if (game_name.equals(scoredata[0])) {
////                scores.add(new Score(scoredata[0], scoredata[1], scoredata[2]));
////            }
////            data = in.readLine();
////        }
//        return scores;
//    }
//}

    public static SaveData loadGames(String gameName)
            throws IOException, ClassNotFoundException {
        Vector<Score> scores = new Vector<>();
        SaveData curData = null;
        try{
            FileInputStream is = new FileInputStream(SAVE_FILE_DAT);
            ObjectInputStream ois = new ObjectInputStream(is);
            try{
                do {
                    curData = (SaveData) ois.readObject();
                    System.out.println(curData.gameName);
                }while (!gameName.equals(curData.gameName));
            }catch (Exception e){
//                e.printStackTrace();
                System.out.println("Not found!");
            }
            ois.close();
            is.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return curData;
    }
}


class AppendableObjectOutputStream extends ObjectOutputStream {

    private boolean append;
    private boolean initialized;
    private DataOutputStream dout;

    protected AppendableObjectOutputStream(boolean append) throws IOException, SecurityException {
        super();
        this.append = append;
        this.initialized = true;
    }

    public AppendableObjectOutputStream(OutputStream out, boolean append) throws IOException {
        super(out);
        this.append = append;
        this.initialized = true;
        this.dout = new DataOutputStream(out);
        this.writeStreamHeader();
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        if (!this.initialized || this.append) return;
        if (dout != null) {
            dout.writeShort(STREAM_MAGIC);
            dout.writeShort(STREAM_VERSION);
        }
    }

}