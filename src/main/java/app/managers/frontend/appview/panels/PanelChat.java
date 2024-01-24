package app.managers.frontend.appview.panels;

import app.managers.frontend.appview.textfields.TextPaneChat;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class PanelChat extends JPanel {
    public PanelChat(TextPaneChat chatArea) {
        setLayout(new BorderLayout());
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
    }
}