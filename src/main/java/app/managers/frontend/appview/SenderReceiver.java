package app.managers.frontend.appview;

import app.managers.backend.GPTPort;
import app.records.Role;

import javax.swing.*;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SenderReceiver {

    private final ApplicationView app;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public ExecutorService executorService = Executors.newSingleThreadExecutor();
    public Future<?> future;

    public SenderReceiver(ApplicationView app) {
        this.app = app;
    }

    public void sendMessage() {
        app.appPreferenceManager.disableElements();

        var secondsSpent = new AtomicInteger();

        final Future<?> timerFuture = scheduler.scheduleAtFixedRate(
                () -> SwingUtilities.invokeLater(
                        () -> app.timeoutLabel.setText("Denke nach... " + secondsSpent.getAndIncrement() + " / " + app.timeoutValue + "s")
                ), 1, 1, TimeUnit.SECONDS);

        future = executorService.submit(() -> {
            try {
                var query = app.queryPane.getText();
                app.chatPane.writeMsg(query, Role.USER);
                app.chatPane.setCaretPosition(app.chatPane.getDocument().getLength());
                app.queryPane.setText("");
                long startTime = System.currentTimeMillis();
                Optional<String> response;


                response = app.manager.callGPT(query);


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

            timerFuture.cancel(true);

            SwingUtilities.invokeLater(app.appPreferenceManager::enableElements);
        });


        new Thread(() -> {
            try {
                future.get(app.timeoutValue, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                future.cancel(true);
                System.err.println("Timeout reached.");
                SwingUtilities.invokeLater(() -> app.timeoutLabel.setText("Timeout erreicht!"));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
