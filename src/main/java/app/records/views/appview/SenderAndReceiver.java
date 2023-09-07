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

    void sendMessage() {
        System.out.println("sendMessage");
        // Disable UI components to prevent multiple requests
        app.queryPane.disableListener();
        app.progressBar.setIndeterminate(true);

        String query = app.queryPane.getText();

        // Create a background thread for sending the request
        Thread thread = new Thread(() -> {

            app.chatPane.writeMsg(Role.USER, query);
            app.queryPane.setText("");

            try {
                var response = app.manager.callGPT(query);
                if (response.isEmpty()) {
                    app.chatPane.writeMsg( Role.ASSISTANT, "ERROR: model could not be reached\n");
                    app.progressBar.setIndeterminate(false);
                    return;
                }
                response.ifPresent(s -> {
                    app.wrapper.formatCode(s);
                    SwingUtilities.invokeLater(() -> {
                        app.progressBar.setIndeterminate(false);
                        app.chatPane.setCaretPosition(app.chatPane.getDocument().getLength());
                        SwingUtilities.invokeLater(() -> app.queryPane.requestFocusInWindow());
                        app.savedChatsList.concurrentlyUpdateList();
                    });
                });
                System.out.println(response);
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