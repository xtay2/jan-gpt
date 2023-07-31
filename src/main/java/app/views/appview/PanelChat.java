package app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelChat extends JPanel {
    public PanelChat(TextChatArea chatArea, PanelNames centerPanel) {
        setLayout(new BorderLayout());
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.SOUTH);
    }
}