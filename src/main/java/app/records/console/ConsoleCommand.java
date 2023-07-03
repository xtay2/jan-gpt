package main.java.app.records.console;

import main.java.app.managers.frontend.ViewManager;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dennis Woithe
 */
public abstract class ConsoleCommand {

    public final String name, syntax, description;

    /**
     * Create a new console command.
     *
     * @param syntax The syntax of the command and its arguments.
     * @param description A short description of the command.
     */
    protected ConsoleCommand(String syntax, String description) {
        this.name = syntax.split(" ")[0];
        this.syntax = syntax;
        this.description = description;
    }

    /**
     * Returns a set of all available {@link ConsoleCommand}s.
     */
    public static Set<ConsoleCommand> getCommands() {
        var commands = new HashSet<>(Set.of(
                new ExitCommand(),
                new ChangeModelCommand(),
                new GetModelsCommand(),
                new GetCurrentModelCommand(),
                new SaveConversationCommand()
        ));
        commands.add(new HelpCommand(commands));
        return commands;
    }

    public abstract String apply(String input, ViewManager manager);

}