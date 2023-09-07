package app.records.views.appview;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Vector;

/**
 * @author A.Mukhamedov
 */
public class ListenerButtonDeleteChat implements ActionListener {

    private final ApplicationView app;

    public ListenerButtonDeleteChat(ApplicationView app) {
        this.app = app;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(app.mainFrame,
                "Sicher, dass du den ausgewählten Chat löschen möchtest?", "Bestätigung", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            app.manager.deleteConversation(app.savedChatsList.getSelectedValue());
            var chats = new Vector<>(app.manager.getConversations().orElse(Collections.emptyList()));
            if(!chats.contains(SavedChatsList.NEW_CHAT))
                chats.add(0, SavedChatsList.NEW_CHAT);
            app.savedChatsList.setListData(chats);
            app.savedChatsList.setSelectedValue(SavedChatsList.NEW_CHAT, true);
            app.chatPane.setText("");
        }
    }
}
