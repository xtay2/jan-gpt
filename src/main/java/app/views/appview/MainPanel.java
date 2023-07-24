package main.java.app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class MainPanel extends JPanel {
    public MainPanel(QueryPanel queryPanel, ChatPanel chatPanel) {
        setLayout(new BorderLayout());
        add(queryPanel, BorderLayout.SOUTH);
        add(chatPanel, BorderLayout.CENTER);
    }
}