import javax.swing.*;
import java.awt.*;

public class Drawer {

    public void windowPos(JPanel colPanel, JFrame win) {
        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);
    }

    void addPartyPanels(JPanel colPanel, JPanel partyPanel, JPanel bowlerPanel, JPanel buttonPanel, JPanel addPatronPanel, JPanel remPatronPanel, JPanel newPatronPanel, JPanel finishedPanel) {
        buttonPanel.add(addPatronPanel);
        buttonPanel.add(remPatronPanel);
        buttonPanel.add(newPatronPanel);
        buttonPanel.add(finishedPanel);

        // Clean up main panel
        colPanel.add(partyPanel);
        colPanel.add(bowlerPanel);
        colPanel.add(buttonPanel);
    }
}
