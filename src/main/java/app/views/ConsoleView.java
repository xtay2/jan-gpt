package main.java.app.views;

import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.Role;
import main.java.app.records.console.ConsoleCommand;
import main.java.app.records.console.misc.PromptCommand;
import main.java.app.util.ANSI;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Dennis Woithe
 */
public class ConsoleView implements View {

    public static final String SHELL_NAME = Role.ASSISTANT.alias(true), USER_NAME = Role.USER.alias(true);
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
            write(USER_NAME, "");
            execute(input.nextLine());
        }
    }

    private void checkAPIKey() {
        if (manager.hasAPIKey()) return;
        write(SHELL_NAME, ANSI.colorize("Bitte gib deinen API-Schlüssel ein: ", ANSI.Color.YELLOW));
        while (!manager.hasAPIKey()) {
            if (manager.setAPIKey(input.nextLine())) return;
            write(SHELL_NAME, ANSI.colorize("Der API-Schlüssel ist ungültig. Versuche es erneut: ", ANSI.Color.RED));
        }
    }

    private void execute(String inputStr) {
        if (inputStr == null || inputStr.isBlank()) return;
        inputStr = inputStr.strip();
        for (var command : commands) {
            if (inputStr.startsWith(command.name)) {
                command.apply(
                        inputStr.substring(command.name.length()).trim(),
                        out -> write(SHELL_NAME, out),
                        out -> write(SHELL_NAME, ANSI.colorize(out, ANSI.Color.RED)),
                        manager
                );
                return;
            }
        }
        new PromptCommand().apply(
                inputStr,
                out -> write(SHELL_NAME, out),
                out -> write(SHELL_NAME, ANSI.colorize(out, ANSI.Color.RED)),
                manager
        );
    }


    /**
     * Writes the given message to the output stream.
     * If the message is null, nothing happens.
     *
     * @param alias the name of the sender
     * @param msg   the message to write
     */
    private void write(String alias, String msg) {
        if (msg == null) return;
        msg = alias + "> " + msg;
        for (char c : msg.toCharArray())
            output.write(c);
        output.flush();
    }

}