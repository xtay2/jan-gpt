package app.records.views.appview.panels;

import app.records.views.appview.textfields.TextPaneChat;

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