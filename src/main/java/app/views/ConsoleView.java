package main.java.app.views;

import main.java.app.managers.backend.GPTPort;
import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.console.ConsoleCommand;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Dennis Woithe
 */
public class ConsoleView implements View {

    public static final String SHELL_NAME = "Jan-GPT";
    private static final Scanner input = new Scanner(System.in, StandardCharsets.UTF_8);
    private static final PrintWriter output = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

    private ViewManager manager;

    private final Set<ConsoleCommand> commands;

    public ConsoleView() {
        this.commands = ConsoleCommand.getCommands();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void start(ViewManager manager) {
        // Init
        this.manager = manager;
        checkAPIKey();
        assert manager.hasGPTModel();
        write(SHELL_NAME, "Hallo! Was kann ich für dich tun?\n(Für eine Auswahl an Befehlen, gib \"help\" ein.)\n");
        // Run
        while (true) {
            write("Prompt", "");
            var output = execute(input.nextLine());
            if(output != null)
                write(SHELL_NAME, output);
        }
    }

    private void checkAPIKey() {
        if(manager.hasAPIKey()) return;
        write(SHELL_NAME, "Bitte gib deinen API-Schlüssel ein: ");
        while (!manager.hasAPIKey()) {
            if(manager.setAPIKey(input.nextLine())) continue;
            write(SHELL_NAME,"Der API-Schlüssel ist ungültig. Versuche es erneut: ");
        }
    }

    private String execute(String inputStr) {
        if(inputStr == null || inputStr.isBlank()) return null;
        inputStr = inputStr.strip();
        for(var command : commands) {
            if(inputStr.startsWith(command.name)) {
                var inputRest = inputStr.substring(command.name.length()).trim();
                return command.apply(inputRest, manager);
            }
        }
        // Prompt
        try {
            return manager.callGPT(inputStr).orElse("Ich habe dich leider nicht verstanden.");
        } catch (GPTPort.MissingAPIKeyException e) {
            return "Ich kann dir leider nicht helfen, da ich keine API-Schlüssel habe.";
        } catch (GPTPort.MissingModelException e) {
            return "Bitte lege das Modell fest, mit dem ich arbeiten soll.";
        }
    }


    /**
     * Writes the given message to the output stream.
     * If the message is null, nothing happens.
     * @param alias the name of the sender
     * @param msg the message to write
     */
    private void write(String alias, String msg) {
        if (msg == null) return;
        msg = System.lineSeparator() + alias.strip() + "> " + msg;
        for (char c : msg.toCharArray())
            output.write(c);
        output.flush();
    }

}