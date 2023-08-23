package app.records.views.appview;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

/**
 * @author A.Mukhamedov
 */

public class ListenerMouseScrollChat implements MouseWheelListener {
    private final JTextPane textPane;

    public ListenerMouseScrollChat(JTextPane textPane) {
        this.textPane = textPane;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
            adjustFontSize(e.getWheelRotation());
        }
    }

    private void adjustFontSize(int wheelRotation) {
        Style style = textPane.addStyle("fontSizeStyle", null);
        int fontSize = StyleConstants.getFontSize(style);
        fontSize -= wheelRotation;

        if (fontSize > 0) {
            StyleConstants.setFontSize(style, fontSize);
            textPane.getStyledDocument().setCharacterAttributes(0, textPane.getDocument().getLength(), style, false);
        }
    }
}
