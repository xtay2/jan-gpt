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

