package app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */

public class PanelButtons extends JPanel {
    public PanelButtons(TextChatName saveNameField, JButton saveButton, JButton deleteSelectedButton, JButton deleteAllButton, DropdownGPTModels dropdownGPTModels){
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL; // Buttons will expand horizontally if there's extra space
        constraints.insets = new Insets(5, 5, 5, 5); // Add some padding between buttons

        // Add Save Name Field
        constraints.gridx = 0; // Column 0
        constraints.gridy = 0; // Row 0
        constraints.gridwidth = 1; // Span 1 columns
        add(saveNameField, constraints);

        // Add Save Button
        constraints.gridx = 0; // Column 0
        constraints.gridy = 1; // Row 0
        constraints.gridwidth = 1; // Span 1 column
        add(saveButton, constraints);

        // Add Delete Selected Button
        constraints.gridx = 0; // Column 0
        constraints.gridy = 2; // Row 1
        constraints.gridwidth = 1; // Span 1 column
        add(deleteSelectedButton, constraints);

        // Add Delete All Button
        constraints.gridx = 0; // Column 0
        constraints.gridy = 3; // Row 1
        constraints.gridwidth = 1; // Span 1 column
        add(deleteAllButton, constraints);

        // Add Dropdown GPT Models
        constraints.gridx = 0; // Column 0
        constraints.gridy = 4; // Row 1
        constraints.gridwidth = 1; // Span 1 column
        add(dropdownGPTModels, constraints);



    }
}