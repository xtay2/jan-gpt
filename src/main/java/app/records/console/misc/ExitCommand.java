package app.records.console.misc;

import app.managers.frontend.ViewManager;
import app.records.console.ConsoleCommand;

import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public class ExitCommand extends ConsoleCommand {

    public ExitCommand() {
        super(
                "exit",
                "Beendet das Programm.",
                ConsoleCommand.CommandGroup.MISC
        );
    }

    @Override
    public void apply(String input, Consumer<String> outStream, Consumer<String> errStream, ViewManager manager) {
        outStream.accept("Auf Wiedersehen!");
        System.exit(0);
    }
}