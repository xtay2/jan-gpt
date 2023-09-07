package app.records.views.appview;

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
    public StyledDocument document;
    public SimpleAttributeSet code;
    public SimpleAttributeSet copyButton;

    public Wrapper(ApplicationView app) {
        document = app.chatPane.getStyledDocument();
        code = new SimpleAttributeSet();
        copyButton = new SimpleAttributeSet();
        StyleConstants.setForeground(code, Color.BLUE);
        StyleConstants.setFontFamily(code, "Consolas");
        StyleConstants.setFontSize(code, 12);
    }


    // takes a response and formats code withing ``` ``` tags in blue and adds a button to copy the code to the clipboard
    public void formatCode(String response) {

        String codeRegex = "```\\w+\n(.*?)```";
        Pattern codePattern = Pattern.compile(codeRegex, Pattern.DOTALL);
        Matcher matcher = codePattern.matcher(response);

        int startPos = 0;
        try {
            while (matcher.find()) {
                int matchStart = matcher.start();
                int matchEnd = matcher.end();
                document.insertString(document.getLength(), response.substring(startPos, matchStart), null);
                document.insertString(document.getLength(), "\n", null);
                String code = matcher.group(1).trim();
                document.insertString(document.getLength(), code, this.code);

                JButton copyCodeButton = getjButton(code);
                StyleConstants.setComponent(copyButton, copyCodeButton);
                document.insertString(document.getLength(), "\n", null);
                document.insertString(document.getLength(), " ", copyButton);

                startPos = matchEnd;
            }
            if (startPos < response.length()) {
                document.insertString(document.getLength(), response.substring(startPos), null);
            }
            document.insertString(document.getLength(), "\n\n", null);

        } catch (BadLocationException e) {throw new AssertionError(e);}
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