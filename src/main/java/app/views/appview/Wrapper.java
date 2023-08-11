package app.views.appview;

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
    private final ApplicationView app;
    private final String codeRegex = "```\\w+\n(.*?)```";
    private String lastCode ="";
    public Wrapper(ApplicationView app) {
        this.app = app;
    }

    public String wrapText(String response) {

        Pattern codePattern = Pattern.compile(codeRegex, Pattern.DOTALL);
        Matcher matcher = codePattern.matcher(response);

        StringBuilder wrappedText = new StringBuilder();

        try {
            int currentIndex = 0;
            while (matcher.find()) {
                String normalText = response.substring(currentIndex, matcher.start());
                wrappedText.append(normalText);

                String codeSnippet = matcher.group(1);
                if (!codeSnippet.isEmpty()) {
                    lastCode = codeSnippet;
                    app.copyButton.setVisible(true);
                }
                lastCode = codeSnippet;
                wrappedText.append("\n").append(createCodeBox(codeSnippet)).append("\n");

                currentIndex = matcher.end();
            }

            String remainingText = response.substring(currentIndex);
            wrappedText.append(remainingText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        app.copyButton.addActionListener(e -> {
            StringSelection selection = new StringSelection(lastCode);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
            app.copyButton.setVisible(false);
        });
        return wrappedText.toString();
    }

    private static String createCodeBox(String codeSnippet) {
        String[] lines = codeSnippet.split("\n");
        StringBuilder codeBox = new StringBuilder();
        codeBox.append("┌──────────────────────────────────────────────────────────────────────────────────────────────────────────┐\n");
        for (String line : lines) {
            codeBox.append(" ").append(line).append("\n");
        }
        codeBox.append("└──────────────────────────────────────────────────────────────────────────────────────────────────────────┘");
        return codeBox.toString();
    }
}