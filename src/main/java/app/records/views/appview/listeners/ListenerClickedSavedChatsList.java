package app.records.views.appview.listeners;


import app.records.views.appview.ApplicationView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author A.Mukhamedov
 */
public class ListenerClickedSavedChatsList implements ListSelectionListener {
    private final ApplicationView app;

    public ListenerClickedSavedChatsList(ApplicationView app) {
        this.app = app;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList<String> savedChatsList = (JList<String>) e.getSource();

        String convName = savedChatsList.getSelectedValue();
        if (convName == null) return;

        app.savedChatsList.openCurrentChatAndUpdateChatPane(app.savedChatsList.formatToSave(convName));

    }


}