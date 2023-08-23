package app.records.views.appview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * This class is responsible for changing the font size of the chat area.
 *
 * @author A.Mukhamedov
 */

public class ListenerMouseScrollChatOLD implements MouseWheelListener {

    private final TextPaneChat chatArea;
    private static final int MIN_FONT_SIZE = 10;
    private static final int MAX_FONT_SIZE = 40;

    public ListenerMouseScrollChatOLD(TextPaneChat chatArea) {
        this.chatArea = chatArea;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
            int newFontSize = chatArea.getFont().getSize() - (2 * e.getWheelRotation());
            newFontSize = Math.max(MIN_FONT_SIZE, Math.min(newFontSize, MAX_FONT_SIZE));
            chatArea.setFont(new Font(chatArea.getFont().getName(), chatArea.getFont().getStyle(), newFontSize));
        } else {
            // Scroll the ChatArea when control key is not pressed
            JScrollPane scrollPane = getScrollPane(chatArea);
            if (scrollPane != null) {
                int scrollAmount = e.getWheelRotation() * 45; // You can adjust the scrolling speed here
                int currentValue = scrollPane.getVerticalScrollBar().getValue();
                scrollPane.getVerticalScrollBar().setValue(currentValue + scrollAmount);
            }
        }
    }

    // Helper method to get the enclosing JScrollPane of the ChatArea
    private JScrollPane getScrollPane(TextPaneChat chatArea) {
        if (chatArea.getParent() instanceof JViewport && chatArea.getParent().getParent() instanceof JScrollPane) {
            return (JScrollPane) chatArea.getParent().getParent();
        }
        return null;
    }
}
