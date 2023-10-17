package app.records.views.appview;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * @author A.Mukhamedov
 */


public class ListenerButtonSaveChat implements ActionListener {
    private final ApplicationView app;

    public ListenerButtonSaveChat(ApplicationView app) {
        this.app = app;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        app.currentChatNameField.saveChat();
    }
}