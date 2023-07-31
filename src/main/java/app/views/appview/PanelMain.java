package app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelMain extends JPanel {
    public PanelMain(SavedChatsList savedChatsList, PanelQuery queryPanel, PanelChat chatPanel) {
        setLayout(new BorderLayout());
        add(savedChatsList, BorderLayout.WEST);
        add(queryPanel, BorderLayout.SOUTH);
        add(chatPanel, BorderLayout.CENTER);
    }
}