package main.java.app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class ButtonsPanel extends JPanel{
        public ButtonsPanel(JButton saveButton, JButton deleteButton) {
            setLayout(new BorderLayout());
            add(saveButton, BorderLayout.WEST);
            add(deleteButton, BorderLayout.EAST);
        }
}
