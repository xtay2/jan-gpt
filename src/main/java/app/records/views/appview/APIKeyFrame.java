package app.records.views.appview;


import app.managers.frontend.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class APIKeyFrame extends JFrame {
    JTextField apiKeyField = new JTextField(20);
    JButton submitButton = new JButton("weiter");
    JLabel errorLabel = new JLabel("");

    public APIKeyFrame(ViewManager manager, Runnable createMainFrame) {

        setTitle("API Key");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        setResizable(true);
        setPreferredSize(new Dimension(400, 100));
        submitButton.setPreferredSize(new Dimension(100, 80));
        add(new JLabel("API Key:"), BorderLayout.NORTH);
        add(apiKeyField, BorderLayout.CENTER);
        add(submitButton, BorderLayout.EAST);
        add(errorLabel, BorderLayout.SOUTH);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(true);

        submitButton.addActionListener(e -> {
            String apiKey = apiKeyField.getText();
            if (!apiKey.isEmpty()) {
                    if (manager.setAPIKey(apiKey)) {
                        setVisible(false);
                        createMainFrame.run();
                    } else {
                        System.out.println("Invalid API key");
                        errorLabel.setText("  Invalider API key");
                    }
            }
        });

        apiKeyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)     System.exit(0);

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String apiKey = apiKeyField.getText();

                    if (!apiKey.isEmpty()) {
                        if (manager.setAPIKey(apiKey)) {
                            setVisible(false);
                            createMainFrame.run();
                        } else {
                            System.out.println("Invalid API key");
                            errorLabel.setText("  Invalider API key");
                        }
                    }
                }
            }
        });


        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }
}