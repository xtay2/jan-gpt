package app.views;

import app.managers.frontend.ViewManager;
import app.records.Role;
import app.records.console.ConsoleCommand;
import app.records.console.misc.PromptCommand;
import app.util.ANSI;

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
    private static final PrintWriter output = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), false);

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
        write("Hallo! Was kann ich für dich tun?\n(Für eine Auswahl an Befehlen, gib \"help\" ein.)");
        // Run
        while (true) {
            awaitPrompt();
            execute(input.nextLine());
        }
    }

    private void checkAPIKey() {
        if (manager.hasAPIKey()) return;
        write(ANSI.colorize("Bitte gib deinen API-Schlüssel ein: ", ANSI.Color.YELLOW));
        while (!manager.hasAPIKey()) {
            if (manager.setAPIKey(input.nextLine())) return;
            write(ANSI.colorize("Der API-Schlüssel ist ungültig. Versuche es erneut: ", ANSI.Color.RED));
        }
    }

    private void execute(String inputStr) {
        if (inputStr == null || inputStr.isBlank()) return;
        inputStr = inputStr.strip();
        for (var command : commands) {
            if (inputStr.startsWith(command.name)) {
                command.apply(
                        inputStr.substring(command.name.length()).trim(),
                        this::write,
                        out -> write(ANSI.colorize(out, ANSI.Color.RED)),
                        manager
                );
                return;
            }
        }
        new PromptCommand().apply(
                inputStr,
                this::write,
                out -> write(ANSI.colorize(out, ANSI.Color.RED)),
                manager
        );
    }


    /**
     * Writes the given message to the output stream.
     * If the message is null, nothing happens.
     *
     * @param msg the message to write
     */
    private void write(String msg) {
        if (msg == null) return;
        msg = ConsoleView.SHELL_NAME + "> " + msg + "\n\n";
        for (char c : msg.toCharArray())
            output.write(c);
        output.flush();
    }

    private void awaitPrompt() {
        output.write(USER_NAME + "> ");
        output.flush();
    }


}