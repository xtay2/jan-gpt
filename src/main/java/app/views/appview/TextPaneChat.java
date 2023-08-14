package app.views.appview;

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
        setContentType("text/html"); // Set content type to HTML
        document = getStyledDocument();
        writeMsg(Role.ASSISTANT, "Hallo, ich bin Ihr Assistent. Wie kann ich Ihnen helfen?");
        ListenerMouseScrollChat mouseScrollListener = new ListenerMouseScrollChat(this);
        addMouseWheelListener(mouseScrollListener);
    }

    public void writeMsg(Role role, String msg) {
        try {
            document.insertString(document.getLength(), role.alias(false) + ":\n" + msg + "\n\n", null);
        } catch (BadLocationException ignored) {}
    }
}
