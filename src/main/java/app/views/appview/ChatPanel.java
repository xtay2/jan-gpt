package main.java.app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class ChatPanel extends JPanel {
    public ChatPanel(ChatArea chatArea, OldChatsPanel oldChatsPanel) {
        setLayout(new BorderLayout());
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(oldChatsPanel, BorderLayout.SOUTH);
    }
}