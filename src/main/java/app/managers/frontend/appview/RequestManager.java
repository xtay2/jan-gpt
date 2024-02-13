package app.managers.frontend.appview;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import app.managers.backend.GPTPort;
import app.records.Role;

import javax.swing.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static app.records.Role.ASSISTANT;
import static app.records.Role.USER;

/**
 * @author A.Mukhamedov
 */
public class RequestManager {

    private final ApplicationView app;
    private final ScheduledExecutorService ThreadScheduler = Executors.newScheduledThreadPool(1);
    private final ExecutorService threadManager = Executors.newSingleThreadExecutor();
    private final AtomicInteger secondsSpent = new AtomicInteger();

    public RequestManager(ApplicationView app) {
        this.app = app;
    }

    public void sendRequest() {
        app.appPreferenceManager.disableElements();
        var query = fetchQuery();
        printRole(USER);
        printMsg(query);

        Future<?> timerThread = ThreadScheduler.scheduleAtFixedRate(
                () -> app.timeoutLabel.setText(getHourglass()
                        + " ... " + secondsSpent.getAndIncrement()
                        + " / " + app.timeoutValue + "s"),
                1, 1, TimeUnit.SECONDS);

        Future<?> requestThread = threadManager.submit(() -> {


            try {
                printRole(ASSISTANT);
                long startTime = System.currentTimeMillis();
                app.manager.streamGPT(query, app.chatPane::writeStreamedMsg);
                app.chatPane.appendToDoc("\n\n");
                long endTime = System.currentTimeMillis();
                app.timeoutLabel.setText("Antwort nach " + (endTime - startTime) / 1000 + "s");
                app.chatPane.reformatChat();
            } catch (GPTPort.MissingAPIKeyException | GPTPort.MissingModelException ex) {
                var exMsg = switch (ex) {
                    case GPTPort.MissingAPIKeyException ignored -> "API key is missing.";
                    case GPTPort.MissingModelException ignored -> "Model is missing.";
                    default -> throw new IllegalStateException("Unexpected value: " + ex);
                };
                app.timeoutLabel.setText(exMsg);
                System.err.println(exMsg);
                throw new RuntimeException(ex);
            }


        });
        timerThread.cancel(true);
        SwingUtilities.invokeLater(app.appPreferenceManager::enableElements);
        app.chatPane.setCaretPosition(app.chatPane.getDocument().getLength());


        new Thread(() -> {
            try {
                requestThread.get(app.timeoutValue, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                requestThread.cancel(true);
                System.err.println("Timeout reached.");
                SwingUtilities.invokeLater(() -> app.timeoutLabel.setText("Timeout erreicht!"));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public String fetchQuery() {
        var query = app.queryPane.getText();
        app.queryPane.setText("");
        return query;
    }

    public void printRole(Role role) {
        app.chatPane.appendToDoc(role.alias(false) + ":\n", app.chatPane.getRoleColor(role));
    }

    public void printMsg(String msg) {
        app.chatPane.appendToDoc(msg + "\n\n");
    }

    private String getHourglass() {
        return (System.currentTimeMillis() / 1000) % 2 == 0 ? "\u231B" : "\u23F3";
    }

    public void printChat(Role role, String msg) {
        printRole(role);
        printMsg(msg);
    }

}



