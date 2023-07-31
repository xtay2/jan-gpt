package app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelLeftSide extends JPanel {
    public PanelLeftSide(JButton deleteButton) {
        setLayout(new BorderLayout());
        add(deleteButton);
    }
}