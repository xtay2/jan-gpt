package main.java.app.views.appview;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author A.Mukhamedov
 */



public class ListenerSaveButton implements ActionListener {
    private final ApplicationView app;

    public ListenerSaveButton(ApplicationView app) {
        this.app = app;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = app.saveNameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(app.mainFrame, "Bitte gib links vom Button einen Namen ein.\n");
            return;
        }

        if (app.manager.saveConversationAs(name)) {
            app.saveNameField.setText("Chat wurde gespeichert!");
            app.dropdownSavedChats.addItem(name);
            app.dropdownSavedChats.setSelectedItem(name);
        } else {
            JOptionPane.showMessageDialog(app.mainFrame, "Bitte führe erst eine Konversation.\n");
        }
    }
}
