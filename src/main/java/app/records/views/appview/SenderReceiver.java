package app.records.views.appview;

import app.managers.backend.GPTPort;
import app.records.Role;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SenderReceiver {

    private final ApplicationView app;
    // Add a ScheduledExecutorService for the timer
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public ExecutorService executorService = Executors.newSingleThreadExecutor();
    public Future<?> future;

    public SenderReceiver(ApplicationView app) {
        this.app = app;
    }

    public void sendMessage() {
        // Disable UI components to prevent multiple requests
        app.queryPane.disableListener();
        app.savedChatsList.disableListener();
        app.disableButtons();
        app.dropdownGPTModels.setEnabled(false);
        app.currentChatNameField.setEnabled(false);
        app.timeoutTextField.setEnabled(false);
        app.progressBar.setIndeterminate(true);
        String query = app.queryPane.getText();

        // Initialize the counter for seconds spent waiting
        final int[] secondsSpent = {0};

        // Schedule a task to update the UI every second
        final Future<?> timerFuture = scheduler.scheduleAtFixedRate(() -> {
            secondsSpent[0]++;
            SwingUtilities.invokeLater(() -> app.timeoutLabel.setText("Denke nach... " + secondsSpent[0] + " / " + app.timeoutValue + "s"));
        }, 1, 1, TimeUnit.SECONDS);

        future = executorService.submit(() -> {
            try {
                app.chatPane.writeMsg(query, Role.USER);
                app.chatPane.setCaretPosition(app.chatPane.getDocument().getLength());
                app.queryPane.setText("");
                long startTime = System.currentTimeMillis();
                var response = app.manager.callGPT(query);
                if (response.isEmpty()) {
                    app.chatPane.writeMsg("Timeout erreicht!\n Erhöhe ggf. die Wartezeit.", Role.ASSISTANT);
                    app.chatPane.setCaretPosition(app.chatPane.getDocument().getLength());
                    app.timeoutLabel.setText("Timeout erreicht.");
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

            // Cancel the timer once the future is done
            timerFuture.cancel(true);

            // Enable UI components after the request is completed
            SwingUtilities.invokeLater(() -> app.queryPane.enableListener());
            SwingUtilities.invokeLater(() -> app.savedChatsList.enableListener());
            SwingUtilities.invokeLater(app::enableButtons);
            SwingUtilities.invokeLater(() -> app.dropdownGPTModels.setEnabled(true));
            SwingUtilities.invokeLater(() -> app.currentChatNameField.setEnabled(true));
            SwingUtilities.invokeLater(() -> app.timeoutTextField.setEnabled(true));
        });
    }
}
