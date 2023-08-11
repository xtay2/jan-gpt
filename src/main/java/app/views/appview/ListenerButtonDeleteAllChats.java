package app.views.appview;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * @author A.Mukhamedov
 */



public class ListenerButtonDeleteAllChats implements ActionListener {
    private final ApplicationView app;

    public ListenerButtonDeleteAllChats(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(app.mainFrame,
                "Sicher, dass du alle Chats löschen möchtest?", "Bestätigung", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (app.manager.getConversations().isPresent() && app.manager.getConversations().get().isEmpty()) {
                JOptionPane.showMessageDialog(app.mainFrame, "Es gibt keine Chats zum Löschen.");
                return;
            }
            for (String name : app.manager.getConversations().get()) {
                app.manager.deleteConversation(name);
            }
            app.savedChatsList.setListData(new String[]{SavedChatsList.NEW_CHAT}); // Set data for the new list
            app.chatPane.setText("");
            app.savedChatsList.setSelectedValue(SavedChatsList.NEW_CHAT, true); // Select the "New Chat" option
        }
    }


}