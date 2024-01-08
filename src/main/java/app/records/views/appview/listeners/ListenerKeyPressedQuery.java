package app.records.views.appview.listeners;

import app.records.views.appview.ApplicationView;
import app.records.views.appview.SavedChatsList;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.KeyEvent;


/**
 * Author: A.Mukhamedov
 */
public class ListenerKeyPressedQuery extends java.awt.event.KeyAdapter {

    private final ApplicationView app;
    private boolean arrowMode = false;

    public ListenerKeyPressedQuery(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        var str = app.queryPane.getText().replaceAll("\n", "").replaceAll("\t", "");

        if (app.queryPane.getText().equals(app.queryPane.hint))
            app.queryPane.clearHintColor();

        // if user presses ctrl + delete, delete the selected chat
        if (e.getKeyCode() == KeyEvent.VK_DELETE && e.isControlDown()) {
            app.savedChatsList.flagChatsAsDeleted(app.savedChatsList.getSelectedValuesList());
        }


        // if the user presses ctrl + t open new chat
        if (e.getKeyCode() == KeyEvent.VK_T && e.isControlDown()) {
            app.savedChatsList.setNewChat();
            app.savedChatsList.openCurrentChatAndUpdateChatPane(SavedChatsList.NEW_CHAT);
        }

        // if user presses ctrl + z, undo chat deletion

        if (e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
            app.savedChatsList.undoDelete();
            e.consume();
            return;
        }

        // If the user presses enter without text, do nothing
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (str.isBlank() || str.equals(app.queryPane.hint)) {
                e.consume();
                return;
            }
            if (e.isShiftDown()) {
                try {
                    // If the user presses enter with shift, add a new line
                    app.queryPane.document.insertString(app.queryPane.getCaretPosition(), "\n", null);
                    app.queryPane.setCaretPosition(app.queryPane.document.getLength());
                } catch (BadLocationException ignored) {
                }
            } else {
                // Execute the sending of the message in a background thread
                checkWebSearchAndSend();
            }
            // Prevents a new line from being added
            e.consume();
        }

        //clear query
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) app.queryPane.setText("");

        // stop arrow mode if any char was typed
        if (e.getKeyCode() != KeyEvent.CHAR_UNDEFINED) arrowMode = false;

        // if the user presses ctrl + s, save the current line to prompts
        if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
            if (!app.queryPane.prompts.contains(app.queryPane.getText().trim()))
                app.queryPane.prompts.add(app.queryPane.getText().trim());
        }

        // if the user presses ctrl + d, delete the current line from prompts if present
        if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
            app.queryPane.prompts.remove(app.queryPane.getText());
            app.queryPane.setText("");
        }

        try {
            // cycling up through the prompts of current session
            if (app.queryPane.getText().isBlank() || app.queryPane.prompts.contains(app.queryPane.getText())) {
                arrowMode = true;

            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (arrowMode && (app.queryPane.getText().isBlank() || app.queryPane.prompts.contains(app.queryPane.getText()))) {
                    arrowMode = true;
                    if (app.queryPane.getText().isBlank())
                        app.queryPane.setText(app.queryPane.prompts.get(app.queryPane.prompts.size() - 1));
                    else if (app.queryPane.prompts.contains(app.queryPane.getText()))
                        app.queryPane.setText(app.queryPane.prompts.get(app.queryPane.prompts.indexOf(app.queryPane.getText()) - 1));
                }
            }
        } catch (Exception ignored) {
        }

        try {
            // cycling down through the prompts of current session
            if (arrowMode && (e.getKeyCode() == KeyEvent.VK_DOWN && !app.queryPane.prompts.isEmpty())) {
                arrowMode = true;
                if (app.queryPane.prompts.contains(app.queryPane.getText()))
                    app.queryPane.setText(app.queryPane.prompts.get(app.queryPane.prompts.indexOf(app.queryPane.getText()) + 1));
            }
        } catch (Exception ignored) {
        }
    }

    private void checkWebSearchAndSend() {
        var query = app.queryPane.getText().trim();
        app.queryPane.prompts.add(query);
        SwingUtilities.invokeLater(app.senderReceiver::sendMessage);
    }
}

