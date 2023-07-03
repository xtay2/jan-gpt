package main.java.app.records.console;

import main.java.app.managers.frontend.ViewManager;

import java.util.Set;

/**
 * @author Dennis Woithe
 */
public class HelpCommand extends ConsoleCommand {

    private final Set<ConsoleCommand> otherCommands;

    public HelpCommand(Set<ConsoleCommand> otherCommands) {
        super("help", "Zeigt alle verfügbaren Befehle an.");
        this.otherCommands = otherCommands;
    }

    @Override
    public String apply(String input, ViewManager manager) {
        var builder = new StringBuilder();
        builder.append("Verfügbare Befehle:\n");
        for (var command : otherCommands) {
            builder.append(command.syntax)
                    .append(" ".repeat(40 - command.syntax.length()))
                    .append(command.description)
                    .append("\n");
        }
        return builder.toString();
    }
}