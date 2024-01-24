package app.managers.frontend.appview.listeners;

import app.managers.frontend.appview.ApplicationView;

import java.awt.event.KeyEvent;

/**
 * @author A.Mukhamedov
 */
public class ListenerKeyPressedNameField extends java.awt.event.KeyAdapter {

    private final ApplicationView app;

    public ListenerKeyPressedNameField(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            app.currentChatNameField.saveChat();
        }
    }
}
