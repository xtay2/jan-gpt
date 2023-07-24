package main.java.app.views.appview;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.Collectors;
/**
 * @author A.Mukhamedov
 */


public class SavedChatsDropdownListener implements ActionListener {
    private final ApplicationView applicationView;

    public SavedChatsDropdownListener(ApplicationView applicationView) {
        this.applicationView = applicationView;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void actionPerformed(ActionEvent e) {
        JComboBox<String> dropdownSavedChats = (JComboBox<String>) e.getSource();
        String convName = (String) dropdownSavedChats.getSelectedItem();

        if (SavedChats.NEW_CHAT.equals(convName)) {
            applicationView.manager.newConversation();
            applicationView.chatArea.setText("Jan-GPT: \nHallo! Was kann ich für dich tun? \n_______ \n");
        } else {
            var conv = applicationView.manager.loadConversation(convName);
            applicationView.chatArea.setText(conv.map(messages ->
                    messages.stream().map(msg -> msg.role().alias(false) + ": " + msg.content() + "\n")
                            .collect(Collectors.joining())
            ).orElse("Chat konnte nicht geladen werden \n"));
        }

        // Set focus on the query text field
        SwingUtilities.invokeLater(() -> applicationView.queryArea.requestFocusInWindow());
    }


}
