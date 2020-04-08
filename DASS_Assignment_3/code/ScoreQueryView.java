import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

public class ScoreQueryView implements ListSelectionListener, ActionListener {
    private JFrame win;
    private JPanel scoreDisplayPanel,colPanel;
    private JButton showScore, newPatron, showBest, finished;
    private JList<String> partyList;
    private JList<Object> allBowlers;
    private Vector<String> party;
    private Vector<Object> bowlerDB;
    private TextArea text;


    private ControlDeskView controlDesk;

    private String selectedNick, selectedMember;
    public ScoreQueryView() throws IOException {

        win = new JFrame("Add Party");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 3));

        scoreDisplayPanel = new JPanel();
        scoreDisplayPanel.setLayout(new FlowLayout());
        text = new TextArea("Nothing Selected\n NO");
        scoreDisplayPanel.add(text);

//        scoreDisplayPanel.setBorder(new TitledBorder("Player Stats"));
//        final JLabel cLabel1 = new JLabel("Player Name: \n");
//        scoreDisplayPanel.add(cLabel1);
//        final JLabel cLabel2 = new JLabel("Max Score: \n");
//        scoreDisplayPanel.add(cLabel2);
//        final JLabel cLabel3 = new JLabel("Average Score: \n");
//        scoreDisplayPanel.add(cLabel3);
//        final JLabel cLabel4 = new JLabel("Now Bowling: ");
//        scoreDisplayPanel.add(cLabel4);

        // Bowler Database
        JPanel bowlerPanel = new JPanel();
        bowlerPanel.setLayout(new FlowLayout());
        bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

        try {
            bowlerDB = new Vector<>(BowlerFile.getBowlers());
        } catch (Exception e) {
            System.err.println("File Error");
            bowlerDB = new Vector();
        }
        allBowlers = new JList(bowlerDB);
        allBowlers.setVisibleRowCount(8);
        allBowlers.setFixedCellWidth(120);
        JScrollPane bowlerPane = new JScrollPane(allBowlers);
        bowlerPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        allBowlers.addListSelectionListener(this);
        bowlerPanel.add(bowlerPane);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

        new Insets(4, 4, 4, 4);

        showScore = new JButton("Show Player Best");
        JPanel showScorePanel = new JPanel();
        showScorePanel.setLayout(new FlowLayout());
        showScore.addActionListener(this);
        showScorePanel.add(showScore);

        showBest = new JButton("Show All-time Best");
        JPanel showBestPanel = new JPanel();
        showBestPanel.setLayout(new FlowLayout());
        showBest.addActionListener(this);
        showBestPanel.add(showBest);

        finished = new JButton("Finished");
        JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        finished.addActionListener(this);
        finishedPanel.add(finished);

        buttonPanel.add(showScorePanel);
        buttonPanel.add(showBestPanel);
        buttonPanel.add(finishedPanel);

        // Clean up main panel
        colPanel.add(scoreDisplayPanel);
        colPanel.add(bowlerPanel);
        colPanel.add(buttonPanel);

        Drawer d = new Drawer();
        d.windowPos(colPanel,win);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(showScore)) {
            Vector<Score> scc = null;
            try {
                scc = ScoreHistoryFile.getScores(selectedNick);
            } catch (IOException ex) {
                ex.printStackTrace();
//                System.out.println("No player selected");
                return;
            }
            int sum = 0;
            int maxScore = 0;
            int numGames = 0;
            for (Score curr: scc){
                int cs = Integer.parseInt(curr.getScore());
                sum += cs;
                numGames++;
                if( cs> maxScore){
                    maxScore = cs;
                }
            }
//            System.out.println(sum/numGames);
//            System.out.println(maxScore);
            text.setText("Average - "+ sum/numGames  + "\nMaximum Score - "+maxScore);
        }
        if(e.getSource().equals(showBest)){
            showBestHandler();
        }
        if (e.getSource().equals(finished)) {
            win.setVisible(false);
        }
    }

    private void showBestHandler() {
        try {
            bowlerDB = new Vector<>(BowlerFile.getBowlers());
        } catch (Exception er) {
            System.err.println("File Error");
            bowlerDB = new Vector();
        }
        String topScorer = "";
        int maxScore = 0;
        for (Object sc :
                bowlerDB) {
            Vector<Score> scc = null;
            try {
                scc = ScoreHistoryFile.getScores(sc.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
//                System.out.println(scc.toString());
            for (Score curr: scc){
                int cs = Integer.parseInt(curr.getScore());
                if( cs> maxScore){
                    maxScore = cs;
                    topScorer = curr.getNickName();
                }
            }
        }
//            System.out.println(topScorer);
//            System.out.println(maxScore);
        text.setText("Top Scorer - "+ topScorer+"\nMax Score - "+maxScore);
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getSource().equals(allBowlers)){
            selectedNick = ((String) ((JList) listSelectionEvent.getSource()).getSelectedValue());
        }
    }
}
