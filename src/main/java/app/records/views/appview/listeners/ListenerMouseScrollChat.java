package app.records.views.appview.listeners;

import app.records.views.appview.ApplicationView;
import app.records.views.appview.TextPaneChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * This class is responsible for changing the font size of the chat area.
 *
 * @author A.Mukhamedov
 */

public class ListenerMouseScrollChat implements MouseWheelListener {

    private static final int MIN_FONT_SIZE = 12;
    private static final int MAX_FONT_SIZE = 30;
    private final ApplicationView app;


    public ListenerMouseScrollChat(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {

            int newFontSize = app.chatPane.getFont().getSize() - (2 * e.getWheelRotation());
            newFontSize = Math.max(MIN_FONT_SIZE, Math.min(newFontSize, MAX_FONT_SIZE));
            var font = new Font(app.chatPane.getFont().getFamily(), app.chatPane.getFont().getStyle(), newFontSize);
            app.chatPane.setFont(font);
        } else {
            // Scroll the ChatArea when control key is not pressed
            JScrollPane scrollPane = getScrollPane(app.chatPane);
            if (scrollPane != null) {
                int scrollAmount = e.getWheelRotation() * 44; // You can adjust the scrolling speed here
                int currentValue = scrollPane.getVerticalScrollBar().getValue();
                scrollPane.getVerticalScrollBar().setValue(currentValue + scrollAmount);
            }
        }
    }

    // Helper method to get the enclosing JScrollPane of the ChatArea
    private JScrollPane getScrollPane(TextPaneChat chatPane) {
        if (chatPane.getParent() instanceof JViewport && chatPane.getParent().getParent() instanceof JScrollPane) {
            return (JScrollPane) chatPane.getParent().getParent();
        }
        return null;
    }
}
