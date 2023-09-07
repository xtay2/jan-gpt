package app.views.appview;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.Collectors;
/**
 * @author A.Mukhamedov
 */


public class ListenerSavedChatsDropdown implements ActionListener {
    private final ApplicationView app;

    public ListenerSavedChatsDropdown(ApplicationView app) {
        this.app = app;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void actionPerformed(ActionEvent e) {
        JComboBox<String> dropdownSavedChats = (JComboBox<String>) e.getSource();
        String convName = (String) dropdownSavedChats.getSelectedItem();

        if (DropdownSavedChats.NEW_CHAT.equals(convName)) {
            app.manager.newConversation();
            app.chatArea.setText("Jan-GPT: \nHallo! Was kann ich für dich tun? \n_______ \n");
            app.saveNameField.setText("");

        } else {
            var conv = app.manager.loadConversation(convName);
            app.chatArea.setText(conv.map(messages ->
                    messages.stream().map(msg -> msg.role().alias(false) + ": " + msg.content() + "\n")
                            .collect(Collectors.joining())
            ).orElse("Chat konnte nicht geladen werden \n"));
        }

        // Set focus on the query text field
        SwingUtilities.invokeLater(() -> app.queryArea.requestFocusInWindow());
    }


}