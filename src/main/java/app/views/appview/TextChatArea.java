package app.views.appview;

import javax.swing.*;

/**
 * This class is used to show the conversation history.
 *
 * @author A.Mukhamedov
 */
public class TextChatArea extends JTextArea {
    public TextChatArea() {
        super();
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        setText("Jan-GPT: \nHallo! Was kann ich für dich tun? \n_______ \n");
        ListenerMouseScrollChat mouseScrollListener = new ListenerMouseScrollChat(this);
        addMouseWheelListener(mouseScrollListener);
    }
}