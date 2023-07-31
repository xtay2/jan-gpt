package app.views.appview;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.stream.Collectors;

/**
 * @author A.Mukhamedov
 */
public class ListenerSavedChatsList implements ListSelectionListener {
    private final ApplicationView app;

    public ListenerSavedChatsList(ApplicationView applicationView) {
        app = applicationView;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList<String> savedChatsList = (JList<String>) e.getSource();
        String convName = savedChatsList.getSelectedValue();
        if (convName == null) return;

        if (convName.equals(SavedChatsList.NEW_CHAT)) {
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