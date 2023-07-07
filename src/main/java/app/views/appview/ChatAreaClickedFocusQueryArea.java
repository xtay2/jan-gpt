package main.java.app.views.appview;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author A.Mukhamedov
 */
public class ChatAreaClickedFocusQueryArea implements MouseListener {

    private final ApplicationView app;

    public ChatAreaClickedFocusQueryArea(ApplicationView app) {
        this.app = app;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        // Set focus on the query text field
        SwingUtilities.invokeLater(() -> app.queryArea.requestFocusInWindow());

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Set focus on the query text field
        SwingUtilities.invokeLater(() -> app.queryArea.requestFocusInWindow());

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Set focus on the query text field
        SwingUtilities.invokeLater(() -> app.queryArea.requestFocusInWindow());

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Set focus on the query text field
        SwingUtilities.invokeLater(() -> app.queryArea.requestFocusInWindow());

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Set focus on the query text field
        SwingUtilities.invokeLater(() -> app.queryArea.requestFocusInWindow());

    }
}
