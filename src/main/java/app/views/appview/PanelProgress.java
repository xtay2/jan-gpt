package app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelProgress  extends JPanel {
    public PanelProgress(JProgressBar progressBar) {
        setLayout(new BorderLayout());
        add(progressBar, BorderLayout.CENTER);
    }
}
