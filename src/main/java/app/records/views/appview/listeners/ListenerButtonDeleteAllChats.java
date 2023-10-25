package app.records.views.appview.listeners;

import app.records.views.appview.ApplicationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author A.Mukhamedov
 */


public class ListenerButtonDeleteAllChats implements ActionListener {
    private final ApplicationView app;

    public ListenerButtonDeleteAllChats(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        app.savedChatsList.flagChatsAsDeleted(app.savedChatsList.chats);
    }


}