package app.records.views.appview;

import javax.swing.*;

/**
 * @author A.Mukhamedov
 */


public class WebCheckBox extends JCheckBox {
    public WebCheckBox(ApplicationView app) {
        super("Web-Suche ausgeschaltet");
        setToolTipText("<html>" +
                "TESTVERSION! <br/>" +
                "Web-Suche nimmt mehr Zeit in Anspruch als eine normale Anfrage!<br/>" +
                "</html>");
        addActionListener(e -> {
            if (isSelected()) {
                setText("Web-Suche eingeschaltet!");
                SwingUtilities.invokeLater(() -> app.queryPane.requestFocusInWindow());
                app.webSearchEnabled = true;
            } else {
                setText("Web-Suche ausgeschaltet");
                SwingUtilities.invokeLater(() -> app.queryPane.requestFocusInWindow());
                app.webSearchEnabled = false;
            }
        });
    }
}