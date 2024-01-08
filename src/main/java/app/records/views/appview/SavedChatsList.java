package app.records.views.appview;

import app.records.Role;
import app.records.views.appview.listeners.ListenerClickedSavedChatsList;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * This class is used to show the saved conversations in a list.
 * Author: A.Mukhamedov
 */
public class SavedChatsList extends JList<String> {
    public static final String NEW_CHAT = "<Neuer Chat>";
    private final ApplicationView app;
    private final Stack<String> flaggedAsDeleted = new Stack<>();
    public Vector<String> chats;

    public SavedChatsList(ApplicationView app) {
        super();
        this.app = app;
        addNewChat();
        Dimension listSize = new Dimension(150, 100);
        setPreferredSize(listSize);
        addListSelectionListener(new ListenerClickedSavedChatsList(app));
    }

    private void addNewChat() {
        chats = new Vector<>(app.manager.getConversations().orElse(Collections.emptyList()));
        if (!chats.contains(NEW_CHAT))
            chats.add(0, NEW_CHAT);
        chats.removeIf(name -> name.equals("u0308"));
        setListData(formatVectorForView(chats));
        setSelectedValue(NEW_CHAT, true);
    }

    public Vector<String> formatVectorForView(Vector<String> names) {
        var temp1 = new Vector<String>();
        names.forEach(name -> temp1.add(name.replaceFirst("-", ".")));
        var temp2 = new Vector<String>();
        temp1.forEach(name -> temp2.add(name.replaceFirst("-", ".")));
        var temp3 = new Vector<String>();
        temp2.forEach(name -> temp3.add(name.replaceAll("-", ":")));
        return temp3;
    }

    public void concurrentlyUpdateList() {
        if (chats.contains(app.currentHypenizedChatName)) {
            app.manager.deleteConversation(formatToSave(app.currentHypenizedChatName));
            app.manager.saveConversationAs(formatToSave(app.currentHypenizedChatName));
        } else if (app.currentChatNameField.getText().isBlank() && app.currentHypenizedChatName.isEmpty()) {
            var namelessChat = new SimpleDateFormat("yyyy.MM.dd").format(new Date()) + " um " + new SimpleDateFormat("HH:mm:ss").format(new Date());
            app.manager.saveConversationAs(formatToSave(namelessChat));
            app.currentHypenizedChatName = namelessChat;
            updateViewOfSavedChats();
        }
    }

    public String formatToSave(String name) {
        return name
                .replaceAll("\\.", "-")
                .replaceAll(":", "-")
                .replaceAll("ä", "\u00E4")
                .replaceAll("ö", "\u00F6")
                .replaceAll("ü", "\u00FC")
                .replaceAll("Ä", "\u00C4")
                .replaceAll("Ö", "\u00D6")
                .replaceAll("Ü", "\u00DC")
                .replaceAll("ß", "\u00DF");
    }

    public void updateViewOfSavedChats() {
        chats = new Vector<>(app.manager.getConversations().orElse(Collections.emptyList()));
        if (!chats.contains(NEW_CHAT)) chats.add(0, NEW_CHAT);
        chats.removeAll(flaggedAsDeleted);
        chats.removeIf(name -> name.equals("u0308"));
        setListData(formatVectorForView(chats));

        setSelectedValue(formatToShow(app.currentHypenizedChatName), true);
    }

    public String formatToShow(String name) {
        return name
                .replaceFirst("-", ".")
                .replaceFirst("-", ".")
                .replaceAll("-", ":");
    }

    public void flagChatsAsDeleted(List<String> selectedNames) {
        if (app.currentHypenizedChatName.isBlank()) return;
        var choice = 0;
        if (selectedNames.size() > 1)
            choice = JOptionPane.showConfirmDialog(app.mainFrame, "Sicher, dass du alle ausgewählten Chats löschen möchtest?", "Bestätigung", JOptionPane.YES_NO_OPTION);
        else
            choice = JOptionPane.showConfirmDialog(app.mainFrame, "Sicher, dass du den ausgewählten Chat löschen möchtest?", "Bestätigung", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.NO_OPTION) return;
        if (choice == JOptionPane.YES_OPTION) {
            selectedNames.removeIf(name -> name.equals(NEW_CHAT));
            var formatForSave = new Vector<String>();
            selectedNames.forEach(name -> formatForSave.add(formatToSave(name)));
            formatForSave.forEach(flaggedAsDeleted::push);
            updateViewOfSavedChats();
            setNewChat();
        }
    }

    public void setNewChat() {
        app.savedChatsList.setSelectedValue(SavedChatsList.NEW_CHAT, true);
        app.chatPane.setText("");
        intro();
    }

    private void intro() {
        app.chatPane.writeMsg("""
                Hallo, ich bin dein Assistent.
                Meine Antworten können aufgrund veralteter Informationen oder Missverständnissen ungenau oder falsch sein.
                Du solltest keine sensiblen oder persönlichen Daten teilen, da ich diese nicht schützen kann.""", Role.ASSISTANT);
    }

    public void undoDelete() {
        if (flaggedAsDeleted.isEmpty()) return;
        app.currentHypenizedChatName = flaggedAsDeleted.peek();
        openCurrentChatAndUpdateChatPane(flaggedAsDeleted.pop());
        updateViewOfSavedChats();
    }

    // loads a conversation of type Optional<List<Message>> from the manager and displays it in the chat pane
    public void openCurrentChatAndUpdateChatPane(String convName) {
        if (convName.equals(SavedChatsList.NEW_CHAT)) {
            app.manager.newConversation();
            app.chatPane.setText("");
            app.currentHypenizedChatName = "";
            app.currentChatNameField.setText("");
            intro();
        } else {
            app.currentHypenizedChatName = convName;
            app.chatPane.setText("");
            app.manager.loadConversation(convName).ifPresent(
                    msgs -> msgs.forEach(msg -> app.chatPane.writeMsg(msg.content(), msg.role()))
            );
        }
        SwingUtilities.invokeLater(() -> app.queryPane.requestFocusInWindow());

    }

    public void setNewTimeout() {
        String input = app.timeoutTextField.getText();

        try {
            int newTimeoutValue = Integer.parseInt(input);
            app.appPreferenceManager.setTimeoutSec(newTimeoutValue);
            app.timeoutValue = newTimeoutValue;
            app.appPreferenceManager.rememberPreferredTimeout(newTimeoutValue);
            app.timeoutLabel.setText("maximale Wartezeit: " + app.timeoutValue + "s");
            app.timeoutLabel.setToolTipText("Timeout nach " + app.timeoutValue + " Sekunden");
            app.timeoutTextField.setText(String.valueOf(newTimeoutValue));
            app.timeoutTextField.setText("");
            SwingUtilities.invokeLater(() -> app.queryPane.requestFocusInWindow());

        } catch (NumberFormatException ex) {
            app.timeoutTextField.setText("");
            app.timeoutLabel.setText("maximale Wartezeit: " + app.timeoutValue + "s");
        }
    }

    public void permaDeleteFlaggedChats() {
        for (var chat : flaggedAsDeleted) app.manager.deleteConversation(formatToSave(chat));
    }

    public void disableListener() {
        setEnabled(false);
    }

    public void enableListener() {
        setEnabled(true);
    }
}