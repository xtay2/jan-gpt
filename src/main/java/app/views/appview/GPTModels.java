package main.java.app.views.appview;

import main.java.app.records.GPTModel;

import javax.swing.*;

/**
 * @author A.Mukhamedov
 */
public class GPTModels extends JComboBox<String> {

    public GPTModels(ApplicationView app) {
        super();
        String[] models = GPTModel.values().toArray(String[]::new);
        setModel(new DefaultComboBoxModel<>(models));
        app.manager.getGPTModel().ifPresent(model -> this.setSelectedItem(model.modelName));

    }
}
