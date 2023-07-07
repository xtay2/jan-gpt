package main.java.app.views.appview;

import main.java.app.managers.backend.GPTPort;
import main.java.app.records.Role;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 *
 * This class is responsible for sending the user's query to the GPT-3 API and displaying the response.
 */
public class Sender {

    private final ApplicationView app;
    public Sender(ApplicationView app) {
        this.app = app;
    }

    void sendMessage() {
        // Disable UI components to prevent multiple requests
        app.queryArea.setEnabled(false);
        app.progressBar.setIndeterminate(true);

        String query = app.queryArea.getText();

        // Create a background thread for sending the request
        Thread thread = new Thread(() -> {
            if (!query.isEmpty()) {
                app.chatArea.append(Role.USER.alias(false) + ": \n ");
                app.chatArea.append(query + "\n");
                app.queryArea.setText("");

                try {
                    var response = app.manager.callGPT(query);
                    if (response.isEmpty()) {
                        app.chatArea.append("ERROR: model could not be reached\n");
                        return;
                    }
                    response.ifPresent(s -> {
                        // Add line breaks to the response if it exceeds the conversation text area width
                        int textAreaWidth = app.chatArea.getWidth();
                        FontMetrics fontMetrics = app.chatArea.getFontMetrics(app.chatArea.getFont());
                        String wrappedResponse = Wrapper.wrapText(s, textAreaWidth, fontMetrics);

                        // Update the UI on the EDT
                        SwingUtilities.invokeLater(() -> {
                            app.progressBar.setIndeterminate(false);
                            app.chatArea.append("_______ \nJan-GPT: \n ");
                            app.chatArea.append(wrappedResponse + "\n_______ \n");
                            app.chatArea.setCaretPosition(app.chatArea.getDocument().getLength());
                            // Set focus on the query text field
                            SwingUtilities.invokeLater(() -> app.queryArea.requestFocusInWindow());
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
            }

            // Enable UI components after the request is completed
            SwingUtilities.invokeLater(() -> {
                app.queryArea.setEnabled(true);
            });
        });

        // Start the background thread
        thread.start();
    }
}
