package app.records.views.appview;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import java.awt.*;
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

    public void startCycle(){
        StyleConstants.setForeground(app.queryPane.color, Color.gray);
        arrowMode = true;
    }
    public void stopCycle(){
        StyleConstants.setForeground(app.queryPane.color, Color.black);
        arrowMode = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        var str = app.queryPane.getText().replaceAll("\n", "").replaceAll("\t", "");

        if (app.queryPane.getText().equals(app.queryPane.hint)) app.queryPane.clear();

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
                } catch (BadLocationException ignored) {}
            }
            else {
                // Execute the sending of the message in a background thread
                app.queryPane.prompts.add(app.queryPane.getText().trim());
                SwingUtilities.invokeLater(app.senderAndReceiver::sendMessage);
            }
            // Prevents a new line from being added
            e.consume();
        }

        // continue to write in black color after cycling through prompts
        if(e.getKeyCode() == KeyEvent.VK_LEFT || (app.queryPane.prompts.contains(app.queryPane.getText()) && e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN)){
            var txt = app.queryPane.getText();
            stopCycle();
            app.queryPane.setText(txt);
        }

        //clear query
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) app.queryPane.setText("");


        // if the user presses ctrl + s, save the current line to prompts
        if(e.getKeyCode() == 83 && e.isControlDown()) {
            if (!app.queryPane.prompts.contains(app.queryPane.getText().trim()))
                app.queryPane.prompts.add(app.queryPane.getText().trim());
        }

        // if the user presses ctrl + d, delete the current line from prompts if present
        if(e.getKeyCode() == 68 && e.isControlDown()) {
            app.queryPane.prompts.remove(app.queryPane.getText());
            app.queryPane.setText("");
        }

        try {
            // cycling up through the prompts of current session
            if(app.queryPane.getText().isBlank() || app.queryPane.prompts.contains(app.queryPane.getText())) {
                startCycle();
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (arrowMode && (app.queryPane.getText().isBlank() || app.queryPane.prompts.contains(app.queryPane.getText()))) {
                    startCycle();
                    if (app.queryPane.getText().isBlank())
                        app.queryPane.setText(app.queryPane.prompts.get(app.queryPane.prompts.size() - 1));
                    else if (app.queryPane.prompts.contains(app.queryPane.getText()))
                        app.queryPane.setText(app.queryPane.prompts.get(app.queryPane.prompts.indexOf(app.queryPane.getText()) - 1));
                }
            }
        } catch (Exception ignored) {}

        try {
            // cycling down through the prompts of current session
            if (arrowMode && (e.getKeyCode() == KeyEvent.VK_DOWN && !app.queryPane.prompts.isEmpty())) {
                startCycle();
                if (app.queryPane.prompts.contains(app.queryPane.getText()))
                    app.queryPane.setText(app.queryPane.prompts.get(app.queryPane.prompts.indexOf(app.queryPane.getText()) + 1));
            }
        } catch (Exception ignored) {}
    }
}
