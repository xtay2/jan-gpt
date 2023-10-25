package app.records.views.appview.panels;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelQuery extends JPanel {
    public PanelQuery(JScrollPane scrollPane, JProgressBar progressPanel) {
        setLayout(new BorderLayout());
        add(new JScrollPane(scrollPane), BorderLayout.CENTER);
        add(progressPanel, BorderLayout.NORTH);
    }
}