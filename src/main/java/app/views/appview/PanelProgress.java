package app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelProgress  extends JPanel {
    public PanelProgress(JButton copyButton, JProgressBar progressBar) {
        setLayout(new BorderLayout());
        add(progressBar, BorderLayout.CENTER);
        add(copyButton, BorderLayout.WEST);


    }
}
