package app.views.appview;

import java.awt.*;

/**
 * @author A.Mukhamedov
 *
 * This class is used to wrap text in the message area.
 */
public class Wrapper {

    public Wrapper() {
    }

    public static String wrapText(String text, int width, FontMetrics fontMetrics) {
        StringBuilder wrappedText = new StringBuilder();
        int lineWidth = 0;

        for (String word : text.split(" ")) {
            int wordWidth = fontMetrics.stringWidth(word + " ");
            if (lineWidth + wordWidth > width) {
                wrappedText.append("\n");
                lineWidth = 0;
            }
            wrappedText.append(word).append(" ");
            lineWidth += wordWidth;
        }

        return wrappedText.toString();
    }
}