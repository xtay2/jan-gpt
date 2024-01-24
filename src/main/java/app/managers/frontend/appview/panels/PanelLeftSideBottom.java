package app.managers.frontend.appview.panels;

import app.managers.frontend.appview.DropdownGPTModels;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelLeftSideBottom extends JPanel {
    public PanelLeftSideBottom(JLabel tooltip, DropdownGPTModels dropdownGPTModels) {
        setLayout(new BorderLayout());
        add(dropdownGPTModels, BorderLayout.CENTER);
        add(tooltip, BorderLayout.EAST);
    }
}
