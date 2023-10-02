package app.records.views.appview;

import app.records.GPTModel;

import javax.swing.*;

/**
 * @author A.Mukhamedov
 */
public class DropdownGPTModels extends JComboBox<String> {

    public DropdownGPTModels(ApplicationView app) {
        super();
        String[] models = GPTModel.values().toArray(String[]::new);
        setModel(new DefaultComboBoxModel<>(models));
        setSelectedItem(app.getPreferredModel());
//        app.manager.getGPTModel().ifPresent(model -> this.setSelectedItem(model.modelName));

        addActionListener(new ListenerDropdownGPTModels(app));
    }
}