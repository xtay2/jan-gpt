package main.java.app.records.console.misc;

import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.console.ConsoleCommand;

import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public class HelpCommand extends ConsoleCommand {

    private final Set<ConsoleCommand> otherCommands;

    public HelpCommand(Set<ConsoleCommand> otherCommands) {
        super(
                "help",
                "Zeigt alle verfügbaren Befehle an.",
                CommandGroup.MISC
        );
        this.otherCommands = otherCommands;
    }

    @Override
    public void apply(String input, Consumer<String> outStream, Consumer<String> errStream, ViewManager manager) {
        var builder = new StringBuilder();
        builder.append("Verfügbare Befehle:\n");
        CommandGroup group = null;
        for (var command : otherCommands) {
            if (group != command.group) {
                group = command.group;
                var name = group.name();
                builder.append("\n")
                        .append("-".repeat(10))
                        .append(name).append("-".repeat(110 - name.length()))
                        .append("\n");
            }
            builder.append(command.syntax)
                    .append(" ".repeat(40 - command.syntax.length()))
                    .append(command.description)
                    .append("\n");
        }
        outStream.accept(builder.toString());
    }
}