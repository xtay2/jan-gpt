package main.java.app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * This class is responsible for the main window of the application.
 *
 * @author A.Mukhamedov
 */
public class MainFrame extends javax.swing.JFrame{

    public MainFrame(ApplicationView app) {
        super(app.manager.getGPTModel().map(m -> m.modelName).orElse("Kein Modell geladen"));
        setBounds(100, 100, WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        setResizable(true);
    }
}

//        JPanel savePanel = new JPanel(new BorderLayout());
//        savePanel.add(chatName, BorderLayout.CENTER);
//        savePanel.add(saveButton, BorderLayout.EAST);
//
//        // Panel for holding the font size slider
//        JPanel midPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        midPanel.add(dropdownSavedChats);
//        midPanel.add(savePanel, BorderLayout.WEST);
//
//        // Panel for holding the query panel and dropdown menu
//        JPanel bottomPanel = new JPanel(new BorderLayout());
//        bottomPanel.add(queryArea, BorderLayout.CENTER);
//        bottomPanel.add(enterToSend, BorderLayout.NORTH);
//
//        // Panel for holding the query panel and dropdown panel
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        mainPanel.add(bottomPanel, BorderLayout.CENTER);
//        mainPanel.add(dropdownGPTModels, BorderLayout.EAST);
//        mainPanel.add(progressBar, BorderLayout.SOUTH);
//        mainPanel.add(midPanel, BorderLayout.NORTH);

