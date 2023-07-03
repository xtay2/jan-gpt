package main.java.app.records.console.misc;

import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.console.ConsoleCommand;

import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public class ExitCommand extends ConsoleCommand {

    public ExitCommand() {
        super(
                "exit",
                "Beendet das Programm.",
                CommandGroup.MISC
        );
    }

    @Override
    public void apply(String input, Consumer<String> outStream, Consumer<String> errStream, ViewManager manager) {
        outStream.accept("Auf Wiedersehen!");
        System.exit(0);
    }
}