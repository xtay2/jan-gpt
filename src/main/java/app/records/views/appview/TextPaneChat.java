package app.records.views.appview;

import app.records.Role;

import javax.swing.*;
import javax.swing.text.*;


/**
 * This class is used to show the conversation history.
 *
 * @author A.Mukhamedov
 */
public class TextPaneChat extends JTextPane  {

    private final StyledDocument document;

    public TextPaneChat() {
        super();
        setEditable(false);
        document = getStyledDocument();
        writeMsg(Role.ASSISTANT, "Hallo, ich bin Ihr Assistent. Wie kann ich Ihnen helfen?");
    }

    public void writeMsg(Role role, String msg) {
        try {
            document.insertString(document.getLength(), role.alias(false) + ":\n" + msg + "\n\n", null);
        } catch (BadLocationException ignored) {}
    }

}
