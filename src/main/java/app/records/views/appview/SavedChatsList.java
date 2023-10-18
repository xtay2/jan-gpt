package app.records.views.appview;

import app.records.Role;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

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
        setListData(chats);
    }

    public void concurrentlyUpdateList() {
        if (chats.contains(app.currentChatName)) {
            app.manager.deleteConversation(app.currentChatName);
            app.manager.saveConversationAs(app.currentChatName);
        } else if (app.currentChatNameField.getText().isBlank() && app.currentChatName.isEmpty()) {
            var namelessChat = "Datum " + new SimpleDateFormat("MM-dd").format(new Date()) + " Uhrzeit " + new SimpleDateFormat("HH-mm-ss").format(new Date());
            app.manager.saveConversationAs(namelessChat);
            updateViewOfSavedChats();
            app.currentChatName = namelessChat;
        }
    }

    public void updateViewOfSavedChats() {
        chats = new Vector<>(app.manager.getConversations().orElse(Collections.emptyList()));
        if (!chats.contains(NEW_CHAT)) chats.add(0, NEW_CHAT);
        chats.removeAll(flaggedAsDeleted);
        setListData(chats);
    }

    public void flagChatsAsDeleted() {
        if (app.currentChatName.isBlank()) return;
        var selectedNames = getSelectedValuesList();
        var choice = 0;
        if (selectedNames.size() > 1)
            choice = JOptionPane.showConfirmDialog(app.mainFrame, "Sicher, dass du die ausgewählten Chats löschen möchtest?", "Bestätigung", JOptionPane.YES_NO_OPTION);
        else
            choice = JOptionPane.showConfirmDialog(app.mainFrame, "Sicher, dass du den ausgewählten Chat löschen möchtest?", "Bestätigung", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            selectedNames.removeIf(name -> name.equals(NEW_CHAT));
            selectedNames.forEach(flaggedAsDeleted::push);
            System.out.println("flaggedAsDeleted:");
            selectedNames.forEach(System.out::println);
            updateViewOfSavedChats();
            setNewChat();
        }
    }

    public void setNewChat() {
        app.savedChatsList.setSelectedValue(SavedChatsList.NEW_CHAT, true);
        app.chatPane.setText("");
        app.chatPane.writeMsg("Hallo, ich bin Ihr Assistent. Wie kann ich Ihnen helfen?", Role.ASSISTANT);
    }

    public void undoDelete() {
        if (flaggedAsDeleted.isEmpty()) return;
        System.out.println("undoDeleted:");
        System.out.println(flaggedAsDeleted.peek());
        setSelectedValue(flaggedAsDeleted.peek(), true);
        openNewChatAndUpdateChatPane(flaggedAsDeleted.pop());
        updateViewOfSavedChats();
    }

    // loads a conversation of type Optional<List<Message>> from the manager and displays it in the chat pane
    public void openNewChatAndUpdateChatPane(String convName) {
        if (convName.equals(SavedChatsList.NEW_CHAT)) {
            app.manager.newConversation();
            app.chatPane.setText("");
            app.currentChatName = "";
            app.currentChatNameField.setText("");
            app.chatPane.writeMsg("Hallo, ich bin Ihr Assistent. Wie kann ich Ihnen helfen?", Role.ASSISTANT);
        } else {
            app.mainFrame.setTitle(convName);
            app.currentChatName = convName;
            app.chatPane.setText("");
            app.manager.loadConversation(convName).ifPresent(
                    msgs -> msgs.forEach(msg -> app.chatPane.writeMsg(msg.content(), msg.role()))
            );
        }
        SwingUtilities.invokeLater(() -> app.queryPane.requestFocusInWindow());
    }

    public void permaDeleteFlaggedChats() {
        for (var chat : flaggedAsDeleted) app.manager.deleteConversation(chat);
    }
}