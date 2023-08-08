package app.views.appview;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Author: A.Mukhamedov
 */
public class ListenerKeyPressedQuery extends java.awt.event.KeyAdapter {

    private final ApplicationView app;
    private final ArrayList<String> prompts = new ArrayList<>();
    private boolean arrowMode = false;
    public ListenerKeyPressedQuery(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        var str = app.queryArea.getText().replaceAll("\n", "").replaceAll("\t", "");

        // If the user presses enter without text, do nothing
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (str.isBlank()) {
                e.consume();
                return;
            }
            if (e.isShiftDown()) // If the user presses enter with shift, add a new line
                app.queryArea.insert("\n", app.queryArea.getCaretPosition());
            else { // Execute the sending of the message in a background thread
                prompts.add(app.queryArea.getText());
                SwingUtilities.invokeLater(app.senderAndReceiver::sendMessage);
            }
            // Prevents a new line from being added
            e.consume();
        }
        if(e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN) arrowMode = false;
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) app.queryArea.setText(""); // Clear the text area
        if(e.getKeyCode() == 83 && e.isControlDown()) prompts.add(app.queryArea.getText()); // save the query
        try { // cycling up through the prompts of current session
            if(app.queryArea.getText().isBlank()) arrowMode = true;
            if (e.getKeyCode() == KeyEvent.VK_UP && !prompts.isEmpty()) {
                if (arrowMode && (app.queryArea.getText().isBlank() || prompts.contains(app.queryArea.getText()))) {
                    if (app.queryArea.getText().isBlank()) app.queryArea.setText(prompts.get(prompts.size() - 1));
                    else if (prompts.contains(app.queryArea.getText())) app.queryArea.setText(prompts.get(prompts.indexOf(app.queryArea.getText()) - 1));}
            }
        } catch (Exception ignored) {}
        try { // cycling down through the prompts of current session
            if (arrowMode && (e.getKeyCode() == KeyEvent.VK_DOWN && !prompts.isEmpty())) {
                if (prompts.contains(app.queryArea.getText())) app.queryArea.setText(prompts.get(prompts.indexOf(app.queryArea.getText()) + 1));}
        } catch (Exception ignored) {}
    }
}