package app.records.views.appview.textfields;

import app.records.views.appview.ApplicationView;
import app.records.views.appview.listeners.ListenerKeyPressedQuery;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * @author A.Mukhamedov
 */
public class TextPaneQuery extends JTextPane {

    public final ArrayList<String> prompts = new ArrayList<>();
    private final ApplicationView app;
    public StyledDocument document;
    public Style gray_color_Style;
    public Style black_color_Style;
    public String hint = "Schreibe hier deine Nachricht...";


    public TextPaneQuery(ApplicationView app) {
        super();
        this.app = app;
        setEditable(true);
        addKeyListener(new ListenerKeyPressedQuery(app));

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearHintColor();
                removeMouseListener(this);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                clearHintColor();
                removeMouseListener(this);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                clearHintColor();
                removeMouseListener(this);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        black_color_Style = addStyle("black", null);
        gray_color_Style = addStyle("gray", null);
        StyleConstants.setForeground(gray_color_Style, java.awt.Color.gray);
    }

    public void clearHintColor() {
        app.queryPane.setText("");
        app.queryPane.setCaretPosition(0);
        StyleConstants.setForeground(app.queryPane.gray_color_Style, Color.black);
    }

    public void initHint() {
        try {
            document = app.queryPane.getStyledDocument();
            document.insertString(document.getLength(), hint, gray_color_Style);

        } catch (BadLocationException ignored) {
        }
    }

    public void disableListener() {
        removeKeyListener(getKeyListeners()[0]);
    }

    public void enableListener() {
        addKeyListener(new ListenerKeyPressedQuery(app));
    }
}
