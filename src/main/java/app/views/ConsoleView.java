package main.java.app.views;

import main.java.app.managers.backend.GPTPort;
import main.java.app.managers.frontend.ViewManager;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * @author Dennis Woithe
 */
public class ConsoleView implements View, AutoCloseable {

    private static final String NAME = "Jan-GPT";
    private static final Scanner input = new Scanner(System.in);
    private static final OutputStream output = System.out;
    private boolean running = false;
    private ViewManager manager;

    public void start(ViewManager manager) {
        if (isRunning())
            throw new IllegalStateException("Shell is already running.");
        this.manager = manager;
        manager.setAPIKey("");
        running = true;
        write("Willkommen bei " + NAME + ". Was kann ich für dich tun?\n");
        do {
            write(NAME + "> ");
            var output = execute(input.nextLine());
            write(output);
        } while (running);
    }

    private String execute(String input) {
        try {
            return manager.callGPT(input).orElse("Ich habe dich leider nicht verstanden.");
        } catch (GPTPort.MissingAPIKeyException e) {
            return "Ich kann dir leider nicht helfen, da ich keine API-Schlüssel habe.";
        } catch (GPTPort.MissingModelException e) {
            return "Bitte lege ein Modell fest, mit dem ich arbeiten soll.";
        }
    }


    /**
     * Writes the given message to the output stream.
     * If the message is null, nothing happens.
     */
    private void write(String msg) {
        if (msg == null) return;
        if(!msg.startsWith(System.lineSeparator())) msg = System.lineSeparator() + msg;
        try {
            for (char c : msg.toCharArray())
                output.write(c);
            output.flush();
        } catch (IOException e) {
            close();
        }
    }

    public boolean isClosed() {
        return !running;
    }

    public boolean isRunning() {
        return running;
    }

    /** Closes this shell and writes the given exit message to the output stream. */
    public void close(String exitMessage) {
        write(exitMessage);
        close();
    }

    /** Closes this shell and the corresponding streams. */
    @Override
    public void close() {
        if (isClosed())
            throw new IllegalStateException(NAME + " was already closed.");
        try {
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            running = false;
        }
    }

}