package app.records.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelLeftSide extends JPanel {
    public PanelLeftSide(JLabel savedChatsLabel, SavedChatsList savedChatsList, PanelButtons panelButtons) {
        setLayout(new BorderLayout());
        add(savedChatsLabel, BorderLayout.NORTH);
        add(savedChatsList, BorderLayout.CENTER);
        add(panelButtons, BorderLayout.SOUTH);
    }
}