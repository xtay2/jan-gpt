package app.managers.frontend.appview;

import app.managers.frontend.appview.listeners.ListenerDropdownGPTModels;
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
        addActionListener(new ListenerDropdownGPTModels(app));
        app.appPreferenceManager.getPreferredModel().ifPresent(gptModel -> setSelectedItem(gptModel.modelName));
        setToolTipText("<html>" +
                "Defaultmäßig wird das neuste Modell verwendet.<br>" +
                "Sobald du ein anderes Modell auswählst, merke ich es mir.<br>" +
                "</html>"
        );
    }
}