package main.java.app.views.appview;
import javax.swing.*;
import java.awt.*;
/**
 * @author A.Mukhamedov
 */
public class OldChatsPanel extends JPanel {
    public OldChatsPanel(ChatName newChatNameField, JButton saveButton, SavedChats dropdownSavedChats) {
        setLayout(new BorderLayout());
        add(newChatNameField, BorderLayout.CENTER);
        add(saveButton, BorderLayout.EAST);
        add(dropdownSavedChats, BorderLayout.WEST);
    }
}