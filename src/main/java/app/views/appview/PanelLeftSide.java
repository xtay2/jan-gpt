package main.java.app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelLeftSide extends JPanel {
    public PanelLeftSide(DropdownSavedChats dropdownSavedChats, JButton deleteButton) {
        setLayout(new BorderLayout());
        add(dropdownSavedChats, BorderLayout.WEST);
        add(deleteButton, BorderLayout.EAST);
    }
}