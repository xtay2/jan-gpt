package app.managers.frontend.appview.listeners;

import app.managers.frontend.appview.ApplicationView;
import app.managers.frontend.appview.SavedChatsList;

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

        if (e.getKeyCode() == KeyEvent.VK_DELETE && e.isControlDown()) {
            app.savedChatsList.flagChatsAsDeleted(app.savedChatsList.getSelectedValuesList());
        }

        if (e.getKeyCode() == KeyEvent.VK_T && e.isControlDown()) {
            app.savedChatsList.setNewChat();
            app.savedChatsList.openCurrentChatAndUpdateChatPane(SavedChatsList.NEW_CHAT);
        }

        if (e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
            app.savedChatsList.undoDelete();
            e.consume();
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (str.isBlank() || str.equals(app.queryPane.hint)) {
                e.consume();
                return;
            }
            if (e.isShiftDown()) {
                try {
                    app.queryPane.document.insertString(app.queryPane.getCaretPosition(), "\n", null);
                    app.queryPane.setCaretPosition(app.queryPane.document.getLength());
                } catch (BadLocationException ignored) {
                }
            } else {
                checkWebSearchAndSend();
            }
            e.consume();
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) app.queryPane.setText("");

        if (e.getKeyCode() != KeyEvent.CHAR_UNDEFINED) arrowMode = false;

        if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
            if (!app.queryPane.prompts.contains(app.queryPane.getText().trim()))
                app.queryPane.prompts.add(app.queryPane.getText().trim());
        }

        if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
            app.queryPane.prompts.remove(app.queryPane.getText());
            app.queryPane.setText("");
        }

        try {
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

