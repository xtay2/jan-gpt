package app.views.appview;

import javax.swing.*;

/**
 * Author: A.Mukhamedov
 */
public class ListenerKeyPressedQuery extends java.awt.event.KeyAdapter {

    private final ApplicationView app;

    public ListenerKeyPressedQuery(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        var str = app.queryArea.getText().replaceAll("\n", "").replaceAll("\t", "");

        // If the user presses enter without text, do nothing
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            if (str.isBlank()) {
                e.consume();
                return;
            }
            if (e.isShiftDown()) // If the user presses enter with shift, add a new line
                app.queryArea.insert("\n", app.queryArea.getCaretPosition());
            else // Execute the sending of the message in a background thread
                SwingUtilities.invokeLater(app.sender::sendMessage);
            // Prevents a new line from being added
            e.consume();
        }
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) // Clear the text area
            app.queryArea.setText("");



    }
}