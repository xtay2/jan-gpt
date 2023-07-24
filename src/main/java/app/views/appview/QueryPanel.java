package main.java.app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class QueryPanel extends JPanel {
    public QueryPanel(QueryArea queryArea, JLabel enterToSend, GPTModels dropdownGPTModels, JProgressBar progressBar) {
        setLayout(new BorderLayout());
        add(new JScrollPane(queryArea), BorderLayout.NORTH);
        add(enterToSend, BorderLayout.WEST);
        add(dropdownGPTModels, BorderLayout.EAST);
        add(progressBar, BorderLayout.CENTER);
    }
}