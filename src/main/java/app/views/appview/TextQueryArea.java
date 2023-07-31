package app.views.appview;

import javax.swing.*;

/**
 * This class is used to show the query.
 * @author A.Mukhamedov
 */
public class TextQueryArea extends JTextArea {
    public TextQueryArea(ApplicationView app) {
        super();
        setEditable(true);
        setLineWrap(true);
        setWrapStyleWord(true);
        addKeyListener(new ListenerKeyPressedQuery(app));
    }
}