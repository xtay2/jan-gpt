package app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelCenter extends JPanel{
    public PanelCenter(TextChatName newChatNameField, JButton saveButton) {
        setLayout(new BorderLayout());
        add(newChatNameField, BorderLayout.CENTER);
        add(saveButton, BorderLayout.EAST);
    }
}