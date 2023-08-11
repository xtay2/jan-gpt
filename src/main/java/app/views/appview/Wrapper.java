package app.views.appview;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author A.Mukhamedov
 *
 * This class is used to wrap text in the message area.
 */
public class Wrapper {
    public StyledDocument styledDocument;
    public SimpleAttributeSet codeStyle;
    public SimpleAttributeSet copyButton;

    public Wrapper(ApplicationView app) {
        styledDocument = app.chatPane.getStyledDocument();
        codeStyle = new SimpleAttributeSet();
        copyButton = new SimpleAttributeSet();
        StyleConstants.setForeground(codeStyle, Color.BLUE);
    }
    
    public void format(String response) {

        String codeRegex = "```\\w+\n(.*?)```";
        Pattern codePattern = Pattern.compile(codeRegex, Pattern.DOTALL);
        Matcher matcher = codePattern.matcher(response);

        int startPos = 0;
        try {
            styledDocument.insertString(styledDocument.getLength(), "Jan-GPT:\n", null);
            while (matcher.find()) {
                int matchStart = matcher.start();
                int matchEnd = matcher.end();
                styledDocument.insertString(styledDocument.getLength(), response.substring(startPos, matchStart), null);
                styledDocument.insertString(styledDocument.getLength(), "\n", null);
                String code = matcher.group(1).trim();
                styledDocument.insertString(styledDocument.getLength(), code, codeStyle);

                JButton copyCodeButton = getjButton(code);
                StyleConstants.setComponent(copyButton, copyCodeButton);
                styledDocument.insertString(styledDocument.getLength(), "\n", null);
                styledDocument.insertString(styledDocument.getLength(), " ", copyButton);

                startPos = matchEnd;
            }
            if (startPos < response.length()) {
                styledDocument.insertString(styledDocument.getLength(), response.substring(startPos), null);
            }
            styledDocument.insertString(styledDocument.getLength(), "\n\n", null);

        } catch (BadLocationException ignored) {
        }
    }

    private static JButton getjButton(String code) {
        JButton copyCodeButton = new JButton("Code kopieren");
        copyCodeButton.addActionListener(e -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(code);
            clipboard.setContents(selection, null);
        });
        return copyCodeButton;
    }

}