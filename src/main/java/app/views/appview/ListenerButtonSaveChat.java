package app.views.appview;

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
        String name = app.saveNameField.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(app.mainFrame, "Bitte gib über dem Button einen Namen ein.\n");
            return;
        } else if (name.equals(SavedChatsList.NEW_CHAT)) {
            JOptionPane.showMessageDialog(app.mainFrame, "Der Name \"Neuer Chat\" ist nicht erlaubt.\n");
            return;
        }

        if (app.manager.saveConversationAs(name)) {
            app.saveNameField.setText(name);
            String[] conversationsArray = app.manager.getConversations().orElse(new ArrayList<>(0)).toArray(new String[0]);
            Vector<String> conversationsVector = new Vector<>(Arrays.asList(conversationsArray));
            conversationsVector.add(0, SavedChatsList.NEW_CHAT);
            app.savedChatsList.setListData(conversationsVector); // Update the list data
            app.savedChatsList.setSelectedValue(name, true); // Select the saved chat in the list
        } else {
            JOptionPane.showMessageDialog(app.mainFrame, "Sende erst deine Nachricht ab!");
        }
    }
}