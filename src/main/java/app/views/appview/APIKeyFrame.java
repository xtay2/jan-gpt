package main.java.app.views.appview;

import main.java.app.managers.frontend.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

/**
 * @author A.Mukhamedov
 */

public class APIKeyFrame extends JFrame {

    public APIKeyFrame(ViewManager manager, Runnable createMainFrame) {

        setTitle("Enter API Key");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new FlowLayout());

        var apiKeyField = new JTextField(20);
        var submitButton = new JButton("Submit");

        var errorLabel = new JLabel("Invalider API key");
        errorLabel.setVerticalAlignment(JLabel.BOTTOM);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(true);
        //TODO
//        errorLabel.setVisible(false);
        submitButton.addActionListener(e -> {

            String apiKey = apiKeyField.getText();
            System.out.println(apiKey);
            if (!apiKey.isEmpty()) {
                // Pass the API key to the main application or perform any other required actions
                // For this example, we simply print the API key

                while(!manager.hasAPIKey()) {
                    if (manager.setAPIKey(apiKey)) {
                        setVisible(false); // Hide the API key frame
                        createMainFrame.run();
                        return;
                    }
                    errorLabel.setVisible(true);
                }
            }
        });

        add(new JLabel("API Key:"));
        add(apiKeyField);
        add(submitButton);
        add(errorLabel);

        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }
}
