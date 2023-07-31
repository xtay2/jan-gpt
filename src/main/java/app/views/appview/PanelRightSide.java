package app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelRightSide extends JPanel{

        public PanelRightSide(PanelChat chatPanel, PanelQuery queryPanel) {
            setLayout(new BorderLayout());
            add(chatPanel, BorderLayout.CENTER);
            add(queryPanel, BorderLayout.SOUTH);
        }
}