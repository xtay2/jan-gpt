package app.views.appview;


import app.managers.frontend.ViewManager;

import javax.swing.*;
import java.awt.*;


public class APIKeyFrame extends JFrame {

    public APIKeyFrame(ViewManager manager, Runnable createMainFrame) {

        setTitle("API Key eingeben");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new FlowLayout());

        var apiKeyField = new JTextField(20);
        var submitButton = new JButton("weiter");
        var errorLabel = new JLabel("Invalider API key");

        errorLabel.setVerticalAlignment(JLabel.BOTTOM);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        submitButton.addActionListener(e -> {

            String apiKey = apiKeyField.getText();
            System.out.println(apiKey);
            if (!apiKey.isEmpty()) {
                // Pass the API key to the main application or perform any other required actions
                // For this example, we simply print the API key

                    if (manager.setAPIKey(apiKey)) {
                        setVisible(false); // Hide the API key frame
                        createMainFrame.run();
                    } else {
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