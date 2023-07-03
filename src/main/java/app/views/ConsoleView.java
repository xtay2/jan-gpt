package main.java.app.views;

import main.java.app.managers.frontend.ViewManager;
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

    public static final String SHELL_NAME = "Jan-GPT", USER_NAME = "Prompt";
    private static final Scanner input = new Scanner(System.in, StandardCharsets.UTF_8);
    private static final PrintWriter output = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

    private static final PrintWriter error = new PrintWriter(new OutputStreamWriter(System.err, StandardCharsets.UTF_8), true);

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
        write(output, SHELL_NAME, "Hallo! Was kann ich für dich tun?\n(Für eine Auswahl an Befehlen, gib \"help\" ein.)\n");
        // Run
        while (true) {
            write(output, "Prompt", "");
            execute(input.nextLine());
        }
    }

    private void checkAPIKey() {
        if (manager.hasAPIKey()) return;
        write(output, SHELL_NAME, "Bitte gib deinen API-Schlüssel ein: ");
        while (!manager.hasAPIKey()) {
            if (manager.setAPIKey(input.nextLine())) return;
            write(error, SHELL_NAME, "Der API-Schlüssel ist ungültig. Versuche es erneut: ");
        }
    }

    private void execute(String inputStr) {
        if (inputStr == null || inputStr.isBlank()) return;
        inputStr = inputStr.strip();
        for (var command : commands) {
            if (inputStr.startsWith(command.name)) {
                command.apply(
                        inputStr.substring(command.name.length()).trim(),
                        out -> write(output,SHELL_NAME, out),
                        out -> write(error,  SHELL_NAME, out),
                        manager
                );
                return;
            }
        }
        new PromptCommand().apply(
                inputStr,
                out -> write(output, SHELL_NAME, out),
                out -> write(error, SHELL_NAME, out),
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
    private void write(PrintWriter out, String alias, String msg) {
        if (msg == null) return;
        alias = switch (alias.trim()) {
            case SHELL_NAME -> ANSI.colorize(SHELL_NAME, ANSI.Color.BLUE);
            case USER_NAME -> ANSI.colorize(USER_NAME, ANSI.Color.GREEN);
            default -> ANSI.colorize(alias, ANSI.Color.YELLOW);
        };

        msg = System.lineSeparator() + alias + "> " + msg;
        for (char c : msg.toCharArray())
            out.write(c);
        out.flush();
    }

}