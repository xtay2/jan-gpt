package app.records.views.appview;

import app.records.GPTModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author A.Mukhamedov
 */
public class DropdownGPTModels extends JComboBox<String> {

    public DropdownGPTModels(ApplicationView app) {
        super();
        String[] allModels = GPTModel.values().toArray(String[]::new);
        List<String> models = new ArrayList<>();
        for (String model : allModels) {
            if (!model.contains("instruct")) {
                models.add(model);
            }
        }

        setModel(new DefaultComboBoxModel<>(models.toArray(new String[0])));
        app.manager.getGPTModel().ifPresent(model -> this.setSelectedItem(model.modelName));
        ListenerDropdownGPTModels gptModelsListener = new ListenerDropdownGPTModels(app);
        addActionListener(gptModelsListener);
    }
}