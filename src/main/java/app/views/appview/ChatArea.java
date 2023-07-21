package main.java.app.views.appview;

import javax.swing.*;

/**
 * This class is used to show the conversation history.
 *
 * @author A.Mukhamedov
 */
public class ChatArea extends JTextArea {

    public ChatArea() {
        super();
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        setText("Jan-GPT: \nHallo! Was kann ich für dich tun? \n_______ \n");
    }
}
