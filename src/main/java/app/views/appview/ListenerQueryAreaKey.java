package app.views.appview;

import javax.swing.*;

/**
 * Author: A.Mukhamedov
 */
public class ListenerQueryAreaKey extends java.awt.event.KeyAdapter {

    private final ApplicationView app;

    public ListenerQueryAreaKey(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {

        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && !e.isShiftDown() && app.queryArea.getText().trim().isEmpty()) {
            return;
        }

        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && e.isShiftDown() && !app.queryArea.getText().trim().isEmpty()) {
            app.queryArea.append("\n");
            return;
        }
        // If the user presses enter without text, do nothing
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && app.queryArea.getText().trim().isEmpty()) {
            app.enterToSend.setText("Gib erst eine Nachricht ein!");
            app.queryArea.setText("");
            return;
        }

        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            app.enterToSend.setVisible(false);
            e.consume(); // Prevents a new line from being added

            // Execute the sending of the message in a background thread
            SwingUtilities.invokeLater(app.sender::sendMessage);
        }
    }
}