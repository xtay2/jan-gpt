package app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class TextChatName extends JTextField {
    public TextChatName() {
        super();
        setEditable(true);
        setPreferredSize(new Dimension(100, 30));
    }
}