package main.java.app.records.console;

import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.console.conv.GetConversationsCommand;
import main.java.app.records.console.conv.LoadConversationCommand;
import main.java.app.records.console.conv.SaveConversationCommand;
import main.java.app.records.console.misc.ExitCommand;
import main.java.app.records.console.misc.HelpCommand;
import main.java.app.records.console.model.ChangeModelCommand;
import main.java.app.records.console.model.GetCurrentModelCommand;
import main.java.app.records.console.model.GetModelsCommand;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public abstract class ConsoleCommand {

    public final String name, syntax, description;

    public final CommandGroup group;

    /** Used to group commands in the help menu. */
    public enum CommandGroup {
    	MODEL,
    	CONVERSATION,
    	MISC
    }

    /**
     * Create a new console command.
     *
     * @param syntax      The syntax of the command and its arguments.
     * @param description A short description of the command.
     */
    protected ConsoleCommand(String syntax, String description, CommandGroup group) {
        this.name = syntax.split(" ")[0];
        this.syntax = syntax;
        this.description = description;
        this.group = group;
    }

    /**
     * Returns a set of all available {@link ConsoleCommand}s.
     */
    public static Set<ConsoleCommand> getCommands() {
        var commands = new LinkedHashSet<>(List.of(

                // Model commands
                new ChangeModelCommand(),
                new GetModelsCommand(),
                new GetCurrentModelCommand(),

                // Conversation commands
                new SaveConversationCommand(),
                new GetConversationsCommand(),
                new LoadConversationCommand(),

                // Misc
                new ExitCommand()
        ));
        commands.add(new HelpCommand(commands));
        return commands;
    }

    public abstract void apply(
            String input,
            Consumer<String> outStream,
            Consumer<String> errStream,
            ViewManager manager
    );

}