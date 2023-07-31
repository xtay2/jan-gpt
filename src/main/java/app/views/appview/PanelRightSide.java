package app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelRightSide extends JPanel{
        public PanelRightSide(DropdownGPTModels dropdownGPTModels) {
            setLayout(new BorderLayout());
            add(dropdownGPTModels, BorderLayout.CENTER);
        }
}