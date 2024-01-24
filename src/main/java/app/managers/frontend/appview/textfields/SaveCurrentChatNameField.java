package app.managers.frontend.appview.textfields;

import app.managers.frontend.appview.ApplicationView;

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

        if (nameInBox.isBlank() && app.currentHypenizedChatName.isBlank()) {
            JOptionPane.showMessageDialog(app.mainFrame, "Bitte gib über dem Button einen Namen ein.");
            return;
        } else if (hasAccentedChars(nameInBox) || hasForbiddenChars(nameInBox)) {
            JOptionPane.showMessageDialog(app.mainFrame, "Der Name darf keine Akzentuierungen oder Sonderzeichen enthalten. '+' und '_' sind erlaubt.");
            return;
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
            app.savedChatsList.setListData((conversationsVector));
            app.savedChatsList.updateViewOfSavedChats();
            app.currentChatNameField.setText("");
            app.savedChatsList.setSelectedValue(app.currentHypenizedChatName, true);

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
        var forbiddenChars = new ArrayList<>(Arrays.asList('.', '-', '<', '>', ':', '"', '/', '\\', '|', '?', '*'));
        for (var forbiddenChar : forbiddenChars) if (name.contains(forbiddenChar.toString())) return true;
        return false;
    }
}