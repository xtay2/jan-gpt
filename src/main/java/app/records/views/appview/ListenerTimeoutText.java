package app.records.views.appview;

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
                app.timeoutLabel.setToolTipText("Timeout nach " + newTimeoutValue + " Sekunden");
                app.timeoutTextField.setText(String.valueOf(newTimeoutValue));

            } catch (NumberFormatException ex) {
                app.timeoutTextField.setText("");
            }
        }

}
