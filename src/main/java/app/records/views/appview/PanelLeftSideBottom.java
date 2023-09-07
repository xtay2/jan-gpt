package app.records.views.appview;

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
