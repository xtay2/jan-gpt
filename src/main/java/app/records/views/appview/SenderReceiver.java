package app.records.views.appview;

import app.managers.backend.GPTPort;
import app.records.Role;

import javax.swing.*;
import java.util.concurrent.*;

public class SenderReceiver {

    private final ApplicationView app;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public ExecutorService executorService = Executors.newSingleThreadExecutor();
    public Future<?> future;

    public SenderReceiver(ApplicationView app) {
        this.app = app;
    }

    public void sendMessage() {
        // Disable UI components to prevent multiple requests
        app.disableElements();
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
                    app.chatPane.writeMsg("< Fehler: Keine Antwort von OpenAI erhalten. >", Role.ASSISTANT);
                    app.chatPane.setCaretPosition(app.chatPane.getDocument().getLength());
                    SwingUtilities.invokeLater(() -> app.timeoutTextField.requestFocusInWindow());
                }
                response.ifPresent(s -> {
                    long endTime = System.currentTimeMillis();
                    app.timeoutLabel.setText("Antwort nach " + (endTime - startTime) / 1000 + "s");
                    app.chatPane.writeMsg(s, Role.ASSISTANT);
                    SwingUtilities.invokeLater(() -> {
                        app.chatPane.setCaretPosition(app.chatPane.getDocument().getLength());
                        SwingUtilities.invokeLater(() -> app.queryPane.requestFocusInWindow());
                        app.savedChatsList.concurrentlyUpdateList();
                    });
                });
            } catch (GPTPort.MissingAPIKeyException ex) {
                app.timeoutLabel.setText("missing API key");
                System.err.println("API key is missing.");
                throw new RuntimeException(ex);
            } catch (GPTPort.MissingModelException ex) {
                app.timeoutLabel.setText("missing model");
                System.err.println("Model is missing.");
                throw new RuntimeException(ex);
            } catch (TimeoutException ex) {
                app.timeoutLabel.setText("timeout reached");
                System.err.println("Timeout reached.");
                throw new RuntimeException(ex);
            }

            // Cancel the timer once the future is done
            timerFuture.cancel(true);

            // Enable UI components after the request is completed
            SwingUtilities.invokeLater(app::enableElements);
        });


        new Thread(() -> {
            try {
                future.get(app.timeoutValue, TimeUnit.SECONDS);  // This now runs on a separate thread
            } catch (TimeoutException e) {
                future.cancel(true);  // Interrupts the task
                System.err.println("Timeout reached.");
                // You may want to update the UI to indicate the timeout. Ensure this is done on the EDT:
                SwingUtilities.invokeLater(() -> app.timeoutLabel.setText("Timeout erreicht!"));
            } catch (InterruptedException | ExecutionException e) {
                // Handle other exceptions
                e.printStackTrace();
            }
        }).start();
    }
}
