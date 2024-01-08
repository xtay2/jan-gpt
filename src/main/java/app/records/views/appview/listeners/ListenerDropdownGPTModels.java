package app.records.views.appview.listeners;

import app.records.GPTModel;
import app.records.views.appview.ApplicationView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author A.Mukhamedov
 */


public class ListenerDropdownGPTModels implements ActionListener {
    private final ApplicationView app;

    public ListenerDropdownGPTModels(ApplicationView app) {
        this.app = app;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent e) {
        JComboBox<String> dropdownGPTModels = (JComboBox<String>) e.getSource();
        String modelName = (String) dropdownGPTModels.getSelectedItem();
        var model = GPTModel.valueOf(modelName);
        if (model.isEmpty()) return;

        app.appPreferenceManager.rememberPreferredModel(model.get());
        app.appPreferenceManager.rememberPreferredModel();
        app.savedChatsList.setNewChat();

        // Set focus on the query text field
        SwingUtilities.invokeLater(() -> app.queryPane.requestFocusInWindow());
    }
}