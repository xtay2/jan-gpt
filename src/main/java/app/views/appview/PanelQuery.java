package app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelQuery extends JPanel {
    public PanelQuery(JScrollPane scrollPane, JProgressBar progressBar) {
        setLayout(new BorderLayout());
        add(new JScrollPane(scrollPane), BorderLayout.CENTER);
        add(progressBar, BorderLayout.NORTH);
    }
}