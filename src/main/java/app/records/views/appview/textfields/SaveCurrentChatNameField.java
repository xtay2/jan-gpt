package app.records.views.appview.textfields;

import app.records.views.appview.ApplicationView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * @author A.Mukhamedov
 */
public class SaveCurrentChatNameField extends JTextField {
    private final ApplicationView app;

    public SaveCurrentChatNameField(ApplicationView app) {
        super();
        this.app = app;
        setEditable(true);
        setPreferredSize(new Dimension(100, 30));
    }

    public void saveChat() {
        String nameInBox = getText();

        if (nameInBox.isBlank() && app.currentHypenizedChatName.isBlank()) { // not saved chat and no name in box
            JOptionPane.showMessageDialog(app.mainFrame, "Bitte gib über dem Button einen Namen ein.");
            return;
        } else if (hasAccentedChars(nameInBox) || hasForbiddenChars(nameInBox)) { // not saved chat and name in box is "New Chat"
            JOptionPane.showMessageDialog(app.mainFrame, "Der Name darf keine Akzentuierungen oder Sonderzeichen enthalten.");
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
        } else if (!app.currentHypenizedChatName.isBlank() && app.savedChatsList.chats.contains(app.currentHypenizedChatName)) {
            app.manager.deleteConversation(app.currentHypenizedChatName);
        }

        if (app.manager.saveConversationAs(nameInBox)) {
            app.timeoutLabel.setText("Chat gespeichert!");
            app.currentChatNameField.setText(nameInBox);
            app.currentHypenizedChatName = nameInBox;
            String[] conversationsArray = app.manager.getConversations().orElse(new ArrayList<>(0)).toArray(new String[0]);
            Vector<String> conversationsVector = new Vector<>(Arrays.asList(conversationsArray));
            app.savedChatsList.setListData((conversationsVector)); // Update the list data
            app.savedChatsList.updateViewOfSavedChats();
            app.currentChatNameField.setText("");
            app.savedChatsList.setSelectedValue(app.currentHypenizedChatName, true); // Select the saved chat in the list

        } else if (nameInBox.isBlank()) {
            JOptionPane.showMessageDialog(app.mainFrame, "Bitte gib über dem Button einen Namen ein.");
        } else {
            JOptionPane.showMessageDialog(app.mainFrame, "Es gibt keine Nachrichten zum Speichern.");
        }
    }

    public static boolean hasAccentedChars(String name) {
        Pattern pattern = Pattern.compile("[^\\p{ASCII}äÄöÖüÜß]");
        return pattern.matcher(name).find();
    }


    public static boolean hasForbiddenChars(String name) {
        var forbiddenChars = new ArrayList<>(Arrays.asList('<', '>', ':', '"', '/', '\\', '|', '?', '*'));
        for (var forbiddenChar : forbiddenChars) if (name.contains(forbiddenChar.toString())) return true;
        return false;
    }
}