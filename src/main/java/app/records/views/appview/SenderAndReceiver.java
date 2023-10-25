package app.records.views.appview;

import app.managers.backend.GPTPort;
import app.records.Role;

import javax.swing.*;

/**
 * @author A.Mukhamedov
 * <p>
 * This class is responsible for sending the user's query to the GPT-3 API and displaying the response.
 */
public class SenderAndReceiver {

    private final ApplicationView app;

    public SenderAndReceiver(ApplicationView app) {
        this.app = app;
    }

    public void sendMessage() {
        System.out.println("sendMessage");
        // Disable UI components to prevent multiple requests
        app.queryPane.disableListener();
        app.timeoutLabel.setText("denke nach...");
        app.progressBar.setIndeterminate(true);

        String query = app.queryPane.getText();

        // Create a background thread for sending the request
        Thread thread = new Thread(() -> {
            try {
                app.chatPane.writeMsg(query, Role.USER);
                app.queryPane.setText("");
                long startTime = System.currentTimeMillis();
                var response = app.manager.callGPT(query);
                if (response.isEmpty()) {
                    app.chatPane.writeMsg("Timeout erreicht!\n Erhöhe ggf. die Wartezeit.", Role.ASSISTANT);
                    app.timeoutLabel.setText("Timeout erreicht!");
                    SwingUtilities.invokeLater(() -> app.timeoutTextField.requestFocusInWindow());
                    app.progressBar.setIndeterminate(false);
                }
                response.ifPresent(s -> {
                    long endTime = System.currentTimeMillis();
                    app.timeoutLabel.setText("Antwort nach " + (endTime - startTime) / 1000 + "s");
                    app.chatPane.writeMsg(s, Role.ASSISTANT);
                    SwingUtilities.invokeLater(() -> {
                        app.progressBar.setIndeterminate(false);
                        app.chatPane.setCaretPosition(app.chatPane.getDocument().getLength());
                        SwingUtilities.invokeLater(() -> app.queryPane.requestFocusInWindow());
                        app.savedChatsList.concurrentlyUpdateList();
                    });
                });
            } catch (GPTPort.MissingAPIKeyException ex) {
                System.err.println("API key is missing.");
                throw new RuntimeException(ex);
            } catch (GPTPort.MissingModelException ex) {
                System.err.println("Model is missing.");
                throw new RuntimeException(ex);
            }
            // Enable UI components after the request is completed
            SwingUtilities.invokeLater(() -> app.queryPane.enableListener());
        });
        // Start the background thread
        thread.start();
    }
}