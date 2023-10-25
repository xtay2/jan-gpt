package app.records.views.appview.listeners;

import app.records.views.appview.ApplicationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author A.Mukhamedov
 */


public class ListenerButtonSaveChat implements ActionListener {
    private final ApplicationView app;

    public ListenerButtonSaveChat(ApplicationView app) {
        this.app = app;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        app.currentChatNameField.saveChat();
    }
}