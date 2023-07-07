package main.java.app.views.appview;

import javax.swing.*;


/**
 * @author A.Mukhamedov
 */
public class QueryArea extends JTextArea {

    public QueryArea() {
        super();
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(true);
        setPreferredSize(new java.awt.Dimension(1000, 100));
    }
}
