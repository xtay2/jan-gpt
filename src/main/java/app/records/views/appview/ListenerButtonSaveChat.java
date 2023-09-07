package app.records.views.appview;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * @author A.Mukhamedov
 */


public class ListenerButtonSaveChat implements ActionListener {
    private final ApplicationView app;

    public ListenerButtonSaveChat(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nameInBox = app.currentChatNameBox.getText();

        if (nameInBox.isBlank() && app.currentChatName.isBlank()) {
            JOptionPane.showMessageDialog(app.mainFrame, "Bitte gib über dem Button einen Namen ein.");
            return;
        } else if (nameInBox.equals(SavedChatsList.NEW_CHAT)) {
            JOptionPane.showMessageDialog(app.mainFrame, "Der Name \"<Neuer Chat>\" ist nicht erlaubt.");
            return;
            // if nameInBox already exists but new conversation is longer than old conversation then replace old conversation with new conversation
        } else if (app.savedChatsList.chats.contains(nameInBox)) {
            int choice = JOptionPane.showConfirmDialog(app.mainFrame,
                    "Es gibt bereits einen Chat mit diesem Namen. Möchtest du ihn überschreiben?", "Bestätigung", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.NO_OPTION) {
                return;
            } else {
                app.manager.deleteConversation(nameInBox);
            }
        } else if(!app.currentChatName.isBlank() && app.savedChatsList.chats.contains(app.currentChatName)) {
            app.manager.deleteConversation(app.currentChatName);
        }

        if (app.manager.saveConversationAs(nameInBox)) {
            app.currentChatNameBox.setText(nameInBox);
            app.currentChatName = nameInBox;
            String[] conversationsArray = app.manager.getConversations().orElse(new ArrayList<>(0)).toArray(new String[0]);
            Vector<String> conversationsVector = new Vector<>(Arrays.asList(conversationsArray));
            app.savedChatsList.setListData(conversationsVector); // Update the list data
            app.savedChatsList.setSelectedValue(nameInBox, true); // Select the saved chat in the list
            app.savedChatsList.updateList();
            app.currentChatNameBox.setText("");
        } else if(nameInBox.isBlank()) {
            JOptionPane.showMessageDialog(app.mainFrame, "Bitte gib über dem Button einen Namen ein.");
        } else {
            JOptionPane.showMessageDialog(app.mainFrame, "Es gibt keine Nachrichten zum Speichern.");
        }
    }
}