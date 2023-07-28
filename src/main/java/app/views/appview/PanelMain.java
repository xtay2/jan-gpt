package main.java.app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelMain extends JPanel {
    public PanelMain(PanelQuery queryPanel, PanelChat chatPanel) {
        setLayout(new BorderLayout());
        add(queryPanel, BorderLayout.SOUTH);
        add(chatPanel, BorderLayout.CENTER);
    }
}