package app.views.appview;
import app.records.GPTModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * @author A.Mukhamedov
 */



public class ListenerDropdownGPTModels implements ActionListener {
    private final ApplicationView applicationView;

    public ListenerDropdownGPTModels(ApplicationView applicationView) {
        this.applicationView = applicationView;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent e) {
        JComboBox<String> dropdownGPTModels = (JComboBox<String>) e.getSource();
        String modelName = (String) dropdownGPTModels.getSelectedItem();
        var model = GPTModel.valueOf(modelName);
        if (model.isEmpty()) return;
        applicationView.mainFrame.setTitle(modelName);
        applicationView.manager.setGPTModel(model.get());
        // Set focus on the query text field
        SwingUtilities.invokeLater(() -> applicationView.queryArea.requestFocusInWindow());
    }
}