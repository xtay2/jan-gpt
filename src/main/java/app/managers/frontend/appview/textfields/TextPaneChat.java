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

    public final StyledDocument document;
    public final SimpleAttributeSet baseColor, codeColor, userColor, assistantColor;

    public TextPaneChat() {
        super();
        setEditable(false);
        document = getStyledDocument();

        baseColor = new SimpleAttributeSet();
        StyleConstants.setForeground(baseColor, new Color(0, 0, 0));
        StyleConstants.setFontFamily(baseColor, "Arial");

        codeColor = new SimpleAttributeSet();
        StyleConstants.setForeground(codeColor, new Color(0, 0, 235));
        StyleConstants.setFontFamily(codeColor, "Consolas");

        userColor = new SimpleAttributeSet();
        StyleConstants.setForeground(userColor, new Color(255, 155, 0));
        StyleConstants.setFontFamily(userColor, "Arial");

        assistantColor = new SimpleAttributeSet();
        StyleConstants.setForeground(assistantColor, new Color(55, 155, 0));
        StyleConstants.setFontFamily(assistantColor, "Arial");
    }

    public void writeStreamedMsg(String responseToken) {
        appendToDoc(responseToken);
    }

    public void appendToDoc(String msg) {
        appendToDoc(msg, null);
    }

    public void appendToDoc(String msg, SimpleAttributeSet attributeSet) {
        try {
            document.insertString(document.getLength(), msg, attributeSet);
        } catch (BadLocationException e) {
            throw new AssertionError(e);
        }
    }

    public SimpleAttributeSet getRoleColor(Role role) {
        return role == Role.USER ? userColor : assistantColor;
    }


    public void reformatChat() {
        var fullChat = getText();
        setText("");
        var userPattern = Pattern.compile("User: (.*?)\n", Pattern.DOTALL);
        var assistantPattern = Pattern.compile("Assistant: (.*?)\n", Pattern.DOTALL);
        var codePattern = Pattern.compile("```\\w+\\s+(.*?)```", Pattern.DOTALL);
        var matcher = codePattern.matcher(fullChat);
        for (int matchStart = 0, matchEnd = 0, lastMatchEnd = 0; matcher.find(); matchStart = matcher.start(), matchEnd = matcher.end()) {
            if (matchStart > lastMatchEnd) {
                appendToDoc(fullChat.substring(lastMatchEnd, matchStart - 1));
            }
            var code = matcher.group(1).trim();
            appendToDoc(code + "\n", codeColor);

            var copyCodeButton = new JButton("Code kopieren");
            var copyButtonAttr = new SimpleAttributeSet();
            copyCodeButton.addActionListener(e -> {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(code);
                clipboard.setContents(selection, null);
            });
            StyleConstants.setComponent(copyButtonAttr, copyCodeButton);

            appendToDoc(" ", copyButtonAttr);
            lastMatchEnd = matchEnd;
        }
        appendToDoc(fullChat);
    }


}