package app.records.views.appview;


import app.records.Role;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.stream.Collectors;

/**
 * @author A.Mukhamedov
 */
public class ListenerClickedSavedChatsList implements ListSelectionListener {
    private final ApplicationView app;

    public ListenerClickedSavedChatsList(ApplicationView applicationView) {
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
            app.chatPane.setText("");
            app.currentChatName = "";
            app.currentChatNameBox.setText("");
            app.chatPane.writeMsg(Role.ASSISTANT, "Hallo, ich bin Ihr Assistent. Wie kann ich Ihnen helfen?");
            app.currentChatNameBox.setText("");
        } else {
            app.mainFrame.setTitle(convName);
            app.currentChatName = convName;
            var conv = app.manager.loadConversation(convName);
            app.chatPane.setText(conv.map(messages -> messages
                        .stream()
                        .map(msg -> msg.role().alias(false) + ":</b><br>" + msg.content() + "<br>"+ "<br>")
                        .collect(Collectors.joining("\n"))
                                          ).orElse("Chat konnte nicht geladen werden \n")
                                );
        }
        SwingUtilities.invokeLater(() -> app.queryPane.requestFocusInWindow());
    }
}