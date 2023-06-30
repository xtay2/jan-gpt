package main.java.app.views;

import main.java.app.managers.backend.GPTPort;
import main.java.app.managers.frontend.ViewManager;

import javax.swing.*;

/**
 * @author Dennis Woithe
 */
public class ApplicationView implements View {
    private ViewManager manager;
    private JFrame frame;
    private JTextArea queryTextArea;
    private JButton sendButton;
    private String gptModel;


    /**
     * @param manager The component that excepts data.
     */
    @Override
    public void start(ViewManager manager) {

        this.manager = manager;

        frame = new JFrame("Chatus GPTus");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        queryTextArea = new JTextArea();
        queryTextArea.setBounds(10, 10, 380, 200);
        frame.getContentPane().add(queryTextArea);

        sendButton = new JButton("Send");
        sendButton.setBounds(10, 220, 80, 30);
        frame.getContentPane().add(sendButton);

        sendButton.addActionListener(e -> {
            String query = queryTextArea.getText();
            try {
                manager.callGPT(query);
            } catch (GPTPort.MissingAPIKeyException ex) {
                System.err.println("API key is missing.");
                throw new RuntimeException(ex);
            } catch (GPTPort.MissingModelException ex) {
                System.err.println("Model is missing.");
                throw new RuntimeException(ex);
            }
        });

        frame.setVisible(true);
    }
}