package app.records.views.appview;

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
    public Style color;
    public String hint = "Schreibe hier deine Nachricht...";

    public TextPaneQuery(ApplicationView app) {
        super();
        this.app = app;
        setEditable(true);
        addKeyListener(new ListenerKeyPressedQuery(app));
        addKeyListener(new ListenerKeyPressedChats(app));

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clear();
                removeMouseListener(this);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                clear();
                removeMouseListener(this);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                clear();
                removeMouseListener(this);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        color = addStyle("gray", null);
        StyleConstants.setForeground(color, java.awt.Color.gray);
    }

    public void clear() {
        app.queryPane.setText("");
        app.queryPane.setCaretPosition(0);
        StyleConstants.setForeground(app.queryPane.color, Color.black);
    }

    public void initHint() {
        try {
            document = app.queryPane.getStyledDocument();
            document.insertString(document.getLength(), hint, color);

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
