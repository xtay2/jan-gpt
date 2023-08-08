package app.views.appview;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author A.Mukhamedov
 *
 * This class is used to wrap text in the message area.
 */
public class Wrapper {
    private final ApplicationView app;
//    private boolean isCode = false;

    public Wrapper(ApplicationView app) {
        this.app = app;
    }

    public String wrapText(String text, int width, FontMetrics fontMetrics) {

        Pattern codeSnippetPattern = Pattern.compile("```\\w+\n(.*?)```", Pattern.DOTALL);
        Matcher matcher = codeSnippetPattern.matcher(text);

        StringBuilder wrappedText = new StringBuilder();

//        int lineWidth = 0;
//
//        for (String word : text.split(" ")) {
//            int wordWidth = fontMetrics.stringWidth(word + " ");
//            if (lineWidth + wordWidth > width) {
//                wrappedText.append("\n");
//                lineWidth = 0;
//            }
//            wrappedText.append(word).append(" ");
//            lineWidth += wordWidth;
//        }

        try {
            int currentIndex = 0;
            while (matcher.find()) {
                String normalText = text.substring(currentIndex, matcher.start());
                wrappedText.append(normalText);

                String codeSnippet = matcher.group(1);
                if (!codeSnippet.isEmpty()) {
                    app.copyCodeButton.setVisible(true);
                }
                wrappedText.append("\n").append(createCodeBox(codeSnippet)).append("\n");

                currentIndex = matcher.end();
            }

            String remainingText = text.substring(currentIndex);
            wrappedText.append(remainingText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        app.copyCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyToClipboard(getCodeFromText(text));
                //TODO: bug? copiert immer nurt den ersten codeblock
                //TODO: Jan möchte auch Tabelle kopieren. wie?
                app.copyCodeButton.setVisible(false);

            }
        });

        return wrappedText.toString();
    }

    private String getCodeFromText(String text) {
        Pattern codeSnippetPattern = Pattern.compile("```\\w+\n(.*?)```", Pattern.DOTALL);
        Matcher matcher = codeSnippetPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private static String createCodeBox(String codeSnippet) {
        String[] lines = codeSnippet.split("\n");
        StringBuilder codeBox = new StringBuilder();

        codeBox.append("┌────────────────────────────────────────────────────────────────────────────────────────────────────┐\n");
        for (String line : lines) {
            codeBox.append(" ").append(line).append("\n");
        }
        codeBox.append("└────────────────────────────────────────────────────────────────────────────────────────────────────┘");

        return codeBox.toString();
    }

    private static void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
    }
}