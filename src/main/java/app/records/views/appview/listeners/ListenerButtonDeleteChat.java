package app.records.views.appview.listeners;

import app.records.views.appview.ApplicationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author A.Mukhamedov
 */
public class ListenerButtonDeleteChat implements ActionListener {

    private final ApplicationView app;

    public ListenerButtonDeleteChat(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        app.savedChatsList.flagChatsAsDeleted(app.savedChatsList.getSelectedValuesList());
    }

}
