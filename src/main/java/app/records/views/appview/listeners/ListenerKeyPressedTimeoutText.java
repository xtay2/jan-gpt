package app.records.views.appview.listeners;

import app.records.views.appview.ApplicationView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author A.Mukhamedov
 */
public class ListenerKeyPressedTimeoutText implements KeyListener {
    private final ApplicationView app;

    public ListenerKeyPressedTimeoutText(ApplicationView app) {
        this.app = app;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            app.savedChatsList.setNewTimeout();
            app.timeoutTextField.setText("");
            e.consume();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
