package main.java.app.views.appview;

import javax.swing.*;


/**
 * @author A.Mukhamedov
 */
public class QueryArea extends JTextArea {

    public ApplicationView app = new ApplicationView();

    public QueryArea(ApplicationView app) {
        super();
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(true);
        setPreferredSize(new java.awt.Dimension(1000, 100));
        addKeyListener(new EnterKeyListener(app));
    }
}
