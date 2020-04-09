import java.io.*;
import java.util.Arrays;
import java.util.Vector;

public class SaveGame{

    private static String SAVE_FILE_DAT = "SAVE.DAT";


    public static void saveGame(String gameName,Vector<? extends Object> bowlers, int[][] cumulScores, int[] curScores, int[][] finalScores, int gameNumber, int count, int frame)

            throws IOException {

        try{
            File file = new File(SAVE_FILE_DAT);
            boolean append = file.exists();
            FileOutputStream out = new FileOutputStream(file,append);
            AppendableObjectOutputStream obj = new AppendableObjectOutputStream(out,append);
            obj.writeObject(gameName);
            obj.writeObject(bowlers);
            obj.writeObject(cumulScores);
            obj.writeObject(curScores);
            obj.writeObject(finalScores);
            obj.writeObject(gameNumber);
            obj.writeObject(count);
            obj.writeObject(frame);
            obj.close();
            out.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

//    public static void saveGame(SaveData sd)
//            throws IOException {
//
//        try{
//            File file = new File(SAVE_FILE_DAT);
//            boolean append = file.exists();
//            FileOutputStream out = new FileOutputStream(new File(SAVE_FILE_DAT),append);
//            AppendableObjectOutputStream obj = new AppendableObjectOutputStream(out,append);
//            obj.writeObject(sd);
//            obj.close();
//            out.close();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    public static void saveGame(SaveData sd)
            throws IOException {

        try{
            File file = new File(sd.gameName+".DAT");
            boolean append = file.exists();
            FileOutputStream out = new FileOutputStream(file,append);
            AppendableObjectOutputStream obj = new AppendableObjectOutputStream(out,append);
            obj.writeObject(sd);
            obj.close();
            out.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static SaveData loadGames(String gameName)
            throws IOException, ClassNotFoundException {
        Vector<Score> scores = new Vector<>();
        SaveData curData = null;
        int fl=0;
        try{
            FileInputStream is = new FileInputStream(gameName+".DAT");
            ObjectInputStream ois = new ObjectInputStream(is);
            try{
                do {
                    curData = (SaveData) ois.readObject();
                    System.out.println(curData.gameName);
                }while (!gameName.equals(curData.gameName));
            }catch (Exception e){
                fl=1;
                System.out.println("Not found!");
            }
            ois.close();
            is.close();
        }
        catch (Exception e){
            fl=1;
            e.printStackTrace();
        }
        if (fl==1) return null;
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