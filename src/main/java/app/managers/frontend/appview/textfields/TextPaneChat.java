package app.managers.frontend.appview.textfields;

import app.records.Role;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.regex.Pattern;


/**
 * This class is used to show the conversation history.
 *
 * @author A.Mukhamedov
 */
public class TextPaneChat extends JTextPane {

    private final StyledDocument document;
    private final SimpleAttributeSet codeStyleAttr = new SimpleAttributeSet();
    private final SimpleAttributeSet userStyleAttr = new SimpleAttributeSet();
    private final SimpleAttributeSet assistantStyleAttr = new SimpleAttributeSet();


    public TextPaneChat() {
        super();
        setEditable(false);
        document = getStyledDocument();
        StyleConstants.setForeground(codeStyleAttr, Color.BLUE);
        StyleConstants.setFontFamily(codeStyleAttr, "Consolas");

        StyleConstants.setForeground(userStyleAttr, new Color(255, 155, 0));
        StyleConstants.setFontFamily(userStyleAttr, "Arial");

        StyleConstants.setForeground(assistantStyleAttr, new Color(55, 155, 0));
        StyleConstants.setFontFamily(assistantStyleAttr, "Arial");
    }

    public void writeStreamedMsg(String responseToken) {
        appendToDoc(responseToken);
    }

    private void appendToDoc(String msg) {
        appendToDoc(msg, null);
    }

    private void appendToDoc(String msg, SimpleAttributeSet attributeSet) {
        try {
            document.insertString(document.getLength(), msg, attributeSet);
        } catch (BadLocationException e) {
            throw new AssertionError(e);
        }
    }

    public void writeMsg(String response, Role role) {
        SimpleAttributeSet roleAttributes = role == Role.USER ? userStyleAttr : assistantStyleAttr;

        appendToDoc(role.alias(false) + ":\n", roleAttributes);
        if (!response.contains("```"))
            appendToDoc(response + "\n\n");
        else {
            var codePattern = Pattern.compile("```\\w+\\s+(.*?)```", Pattern.DOTALL);
            var matcher = codePattern.matcher(response);
            for (int matchStart = 0, matchEnd = 0, lastMatchEnd = 0; matcher.find(); matchStart = matcher.start(), matchEnd = matcher.end()) {
                if (matchStart > lastMatchEnd) {
                    appendToDoc(response.substring(lastMatchEnd, matchStart - 1));
                    appendToDoc("\n");
                }
                var code = matcher.group(1).trim();
                appendToDoc(code, codeStyleAttr);
                appendToDoc("\n");

                var copyCodeButton = new JButton("Code kopieren");
                var copyButtonAttr = new SimpleAttributeSet();
                copyCodeButton.addActionListener(e -> {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    StringSelection selection = new StringSelection(code);
                    clipboard.setContents(selection, null);
                });
                StyleConstants.setComponent(copyButtonAttr, copyCodeButton);

                appendToDoc(" ", copyButtonAttr);
                appendToDoc("\n\n");
                lastMatchEnd = matchEnd;
            }
        }
    }

}