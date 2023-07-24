package main.java.app.views.appview;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * This class is responsible for changing the font size of the chat area.
 * @author A.Mukhamedov
 */


public class MouseScrollListener implements MouseWheelListener {

    private final ChatArea chatArea;
    private final QueryArea queryArea;

    public MouseScrollListener(ChatArea chatArea, QueryArea queryArea) {
        this.chatArea = chatArea;
        this.queryArea = queryArea;
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
            chatArea.setFont(new Font(chatArea.getFont().getName(), chatArea.getFont().getStyle(), chatArea.getFont().getSize() - (2 * e.getWheelRotation())));
        } else {
            // Scroll the ChatArea when control key is not pressed
            JScrollPane scrollPane = getScrollPane(chatArea);
            if (scrollPane != null) {
                int scrollAmount = e.getWheelRotation() * 15; // You can adjust the scrolling speed here
                int currentValue = scrollPane.getVerticalScrollBar().getValue();
                scrollPane.getVerticalScrollBar().setValue(currentValue + scrollAmount);
            }
        }
    }

    // Helper method to get the enclosing JScrollPane of the ChatArea
    private JScrollPane getScrollPane(ChatArea chatArea) {
        if (chatArea.getParent() instanceof JViewport && chatArea.getParent().getParent() instanceof JScrollPane) {
            return (JScrollPane) chatArea.getParent().getParent();
        }
        return null;
    }
}
