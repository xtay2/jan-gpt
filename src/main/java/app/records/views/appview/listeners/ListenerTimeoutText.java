package app.records.views.appview.listeners;

import app.records.views.appview.ApplicationView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author A.Mukhamedov
 */
public class ListenerTimeoutText implements ActionListener {

    private final ApplicationView app;

    public ListenerTimeoutText(ApplicationView app) {
        this.app = app;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the text entered by the user
        String input = app.timeoutTextField.getText();

        try {
            // Parse the input as an integer
            int newTimeoutValue = Integer.parseInt(input);
            // Update the timeout value
            app.setTimeoutSec(newTimeoutValue);
            app.timeoutValue = newTimeoutValue;
            app.rememberPreferredTimeout(newTimeoutValue);
            app.timeoutLabel.setText("maximale Wartezeit: " + app.timeoutValue + "s");
            app.timeoutLabel.setToolTipText("Timeout nach " + app.timeoutValue + " Sekunden");
            app.timeoutTextField.setText(String.valueOf(newTimeoutValue));
            app.timeoutTextField.setText("");
            SwingUtilities.invokeLater(() -> app.queryPane.requestFocusInWindow());

        } catch (NumberFormatException ex) {
            app.timeoutTextField.setText("");
            app.timeoutLabel.setText("Nur ganze Zahlen bitte!");
        }
    }

}
