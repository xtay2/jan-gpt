package main.java.app.views.appview;
import javax.swing.*;
import java.awt.*;
/**
 * @author A.Mukhamedov
 */
public class OldChatsPanel extends JPanel {
    public OldChatsPanel(ChatName newChatNameField, ButtonsPanel buttons, SavedChats dropdownSavedChats) {
        setLayout(new BorderLayout());
        add(newChatNameField, BorderLayout.CENTER);
        add(buttons, BorderLayout.EAST);
        add(dropdownSavedChats, BorderLayout.WEST);
    }
}