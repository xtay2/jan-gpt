package main.java.app.views.appview;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * @author A.Mukhamedov
 */



public class DeleteChatsButtonListener implements ActionListener {
    private final ApplicationView app;

    public DeleteChatsButtonListener(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(app.mainFrame,
                "Sind Sie sicher, dass Sie alle Chats löschen möchten?", "Bestätigung", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (app.manager.getConversations().isPresent() && app.manager.getConversations().get().size() < 2) {
                JOptionPane.showMessageDialog(app.mainFrame, "Es gibt keine Chats zum Löschen.");
                return;
            }
            for (String name : app.manager.getConversations().get()) {
                app.manager.deleteConversation(name);
            }
            app.dropdownSavedChats.removeAllItems();
            app.chatArea.setText("");
            app.dropdownSavedChats.addItem(SavedChats.NEW_CHAT);
        }
    }
}
