package app.records.views.appview.panels;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelMain extends JPanel {
    public PanelMain(PanelLeftSide leftSidePanel, PanelRightSide rightSidePanel) {
        setLayout(new BorderLayout());
        add(leftSidePanel, BorderLayout.WEST);
        add(rightSidePanel, BorderLayout.CENTER);
    }
}