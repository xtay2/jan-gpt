package app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelQuery extends JPanel {
    public PanelQuery(TextQueryArea queryArea, JLabel enterToSend,  JProgressBar progressBar) {
        setLayout(new BorderLayout());
        add(new JScrollPane(queryArea), BorderLayout.NORTH);
        add(enterToSend, BorderLayout.WEST);
        add(progressBar, BorderLayout.CENTER);
    }
}