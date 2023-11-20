package app.records.views.appview.listeners;

import app.records.views.appview.ApplicationView;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * @author A.Mukhamedov
 */
public class ListenerFocusTimeoutText implements FocusListener {

    private final ApplicationView app;

    public ListenerFocusTimeoutText(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void focusGained(FocusEvent e) {
        app.timeoutTextField.setText("");
    }

    @Override
    public void focusLost(FocusEvent e) {
        app.savedChatsList.setNewTimeout();
    }
}
