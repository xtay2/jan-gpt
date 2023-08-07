package app.views.appview;

import app.records.Role;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * This class is used to show the conversation history.
 *
 * @author A.Mukhamedov
 */
public class TextAreaChat extends JTextArea {

    public TextAreaChat() {
        super();
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        writeMsg(Role.ASSISTANT, "Hallo, ich bin Ihr Assistent. Wie kann ich Ihnen helfen?");
        ListenerMouseScrollChat mouseScrollListener = new ListenerMouseScrollChat(this);
        addMouseWheelListener(mouseScrollListener);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setColumns(getSize().width / getColumnWidth());
            }
        });
    }

    public void writeMsg(Role role, String msg) {
        append(role.alias(false) + ":\n" + msg + "\n\n");
    }

}