package app.records.views.appview.panels;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */

public class PanelTimeout extends JPanel {
    public PanelTimeout(JLabel timeoutLabel, JTextField timeoutTextField) {
        setLayout(new BorderLayout());
        add(timeoutTextField, BorderLayout.EAST);
        timeoutTextField.setMinimumSize(new Dimension(27, 20));
        timeoutTextField.setPreferredSize(new Dimension(27, 20));
        add(timeoutLabel, BorderLayout.CENTER);
    }
}
