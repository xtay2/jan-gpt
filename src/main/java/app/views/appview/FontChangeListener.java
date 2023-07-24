package main.java.app.views.appview;
import java.awt.*;
import java.awt.event.*;
/**
 * This class is responsible for changing the font size of the chat area.
 * @author A.Mukhamedov
 */


public class FontChangeListener implements MouseWheelListener {

    private final ChatArea chatArea;

    public FontChangeListener(ChatArea chatArea) {
        this.chatArea = chatArea;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
            int fontSize = chatArea.getFont().getSize();
            int notches = e.getWheelRotation();
            int fontSizeStep = 2;
            int newFontSize = fontSize - (fontSizeStep * notches);
            chatArea.setFont(new Font(chatArea.getFont().getName(), chatArea.getFont().getStyle(), newFontSize));
        }
    }
}
