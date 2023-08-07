package app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class TextFieldChatName extends JTextField {
    public TextFieldChatName() {
        super();
        setEditable(true);
        setPreferredSize(new Dimension(100, 30));
    }
}