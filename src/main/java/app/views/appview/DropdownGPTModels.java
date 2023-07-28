package main.java.app.views.appview;

import main.java.app.records.GPTModel;

import javax.swing.*;

/**
 * @author A.Mukhamedov
 */
public class DropdownGPTModels extends JComboBox<String> {

    public DropdownGPTModels(ApplicationView app) {
        super();
        String[] models = GPTModel.values().toArray(String[]::new);
        setModel(new DefaultComboBoxModel<>(models));
        app.manager.getGPTModel().ifPresent(model -> this.setSelectedItem(model.modelName));

    }
}
