package app.records.views.appview.textfields;

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
    private final SimpleAttributeSet codeStyleAttbs = new SimpleAttributeSet();

    public TextPaneChat() {
        super();
        setEditable(false);
        document = getStyledDocument();
        StyleConstants.setForeground(codeStyleAttbs, Color.BLUE);
        StyleConstants.setFontFamily(codeStyleAttbs, "Consolas");
        StyleConstants.setFontSize(codeStyleAttbs, 12);
        writeMsg("Hallo, ich bin dein Assistent. Wie kann ich dir helfen?", Role.ASSISTANT);
    }

    public void writeMsg(String response, Role role) {
        appendToDoc(role.alias(false) + ":\n");
        if (!response.contains("```"))
            appendToDoc(response + "\n\n");
        else {
            var codePattern = Pattern.compile("```(.*?)```", Pattern.DOTALL);
            var matcher = codePattern.matcher(response);
            for (int matchStart = 0, matchEnd = 0, lastMatchEnd = 0; matcher.find(); matchStart = matcher.start(), matchEnd = matcher.end()) {
                if (matchStart > lastMatchEnd) {
                    appendToDoc(response.substring(lastMatchEnd, matchStart - 1));
                    appendToDoc("\n");
                }
                var code = matcher.group(1).trim();
                appendToDoc(code, codeStyleAttbs);
                appendToDoc("\n");

                var copyCodeButton = new JButton("Code kopieren");
                var copyButtonAttb = new SimpleAttributeSet();
                copyCodeButton.addActionListener(e -> {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    StringSelection selection = new StringSelection(code);
                    clipboard.setContents(selection, null);
                });
                StyleConstants.setComponent(copyButtonAttb, copyCodeButton);

                appendToDoc(" ", copyButtonAttb);
                appendToDoc("\n\n");
                lastMatchEnd = matchEnd;
            }
        }
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

}