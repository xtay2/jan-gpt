package app.views.appview;

import javax.swing.*;
import java.awt.*;
/**
 * @author A.Mukhamedov
 */
public class PanelNames extends JPanel{
    public PanelNames(PanelLeftSide leftPanel, PanelCenter centerPanel, PanelRightSide rightPanel) {
        setLayout(new BorderLayout());
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
}