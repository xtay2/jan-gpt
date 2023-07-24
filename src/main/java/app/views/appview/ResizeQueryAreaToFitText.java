package main.java.app.views.appview;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */


public class ResizeQueryAreaToFitText implements DocumentListener {


    private final ApplicationView app;

    public ResizeQueryAreaToFitText(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        resizeQueryArea();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        resizeQueryArea();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        resizeQueryArea();
    }

    private void resizeQueryArea() {
        int width = app.scrollableQueryArea.getWidth();
        int preferredHeight = app.queryArea.getPreferredSize().height;
        int actualHeight = app.scrollableQueryArea.getPreferredSize().height;

        if (preferredHeight != actualHeight) {
            // Adjust the preferred size of the queryArea based on its content
            app.queryArea.setPreferredSize(new Dimension(width, actualHeight));
            SwingUtilities.invokeLater(() -> app.scrollableQueryArea.revalidate());
        }
    }
}
