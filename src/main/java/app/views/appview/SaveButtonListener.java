package main.java.app.views.appview;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author A.Mukhamedov
 */



public class SaveButtonListener implements ActionListener {
    private final ApplicationView applicationView;

    public SaveButtonListener(ApplicationView applicationView) {
        this.applicationView = applicationView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = applicationView.currentChat.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(applicationView.mainFrame, "Bitte gib links vom Button einen Namen ein.\n");
            return;
        }

        if (applicationView.manager.saveConversationAs(name)) {
            applicationView.currentChat.setText("Chat wurde gespeichert!");
            applicationView.dropdownSavedChats.addItem(name);
            applicationView.dropdownSavedChats.setSelectedItem(name);
        } else {
            JOptionPane.showMessageDialog(applicationView.mainFrame, "Bitte führe erst eine Konversation.\n");
        }
    }
}
