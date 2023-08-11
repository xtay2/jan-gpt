package app.views.appview;

import app.records.Role;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;



/**
 * This class is used to show the conversation history.
 *
 * @author A.Mukhamedov
 */
public class TextPaneChat extends JTextPane  {

    private final StyledDocument document;
    private final Style userStyle;
    private final Style assistantStyle;

    public TextPaneChat() {
        super();
        setEditable(false);
        setContentType("text/html"); // Set content type to HTML
        document = getStyledDocument();
        userStyle = document.addStyle("userStyle", null);
        assistantStyle = document.addStyle("assistantStyle", null);
        StyleConstants.setFontFamily(userStyle, getFont().getFamily());
        StyleConstants.setFontFamily(assistantStyle, getFont().getFamily());



        writeMsg(Role.ASSISTANT, "Hallo, ich bin Ihr Assistent. Wie kann ich Ihnen helfen?");
        ListenerMouseScrollChat mouseScrollListener = new ListenerMouseScrollChat(this);
        addMouseWheelListener(mouseScrollListener);


    }

    public void writeMsg(Role role, String msg) {
        // Use styles to format text
        Style style = (role == Role.USER) ? userStyle : assistantStyle;
        try {
            document.insertString(document.getLength(), role.alias(false) + ":\n" + msg + "\n\n", style);
        } catch (BadLocationException ignored) {
        }
    }
}