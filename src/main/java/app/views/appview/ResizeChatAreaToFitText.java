package main.java.app.views.appview;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author A.Mukhamedov
 */
public class ResizeChatAreaToFitText implements DocumentListener {

    private final ApplicationView app;

    public ResizeChatAreaToFitText(ApplicationView app) {
        this.app = app;

    }



    @Override
    public void insertUpdate(DocumentEvent e) {
        adjustWindowSize();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        adjustWindowSize();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        adjustWindowSize();
    }

    private void adjustWindowSize() {
        app.chatArea.setPreferredSize(app.chatArea.getPreferredSize());
        app.mainFrame.pack();
    }
}
