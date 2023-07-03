package main.java.app.records.console;

import main.java.app.managers.frontend.ViewManager;

/**
 * @author Dennis Woithe
 */
public class ExitCommand extends ConsoleCommand {

    protected ExitCommand() {
        super("exit", "Beendet das Programm.");
    }

    @Override
    public String apply(String input, ViewManager manager) {
        System.out.println("Auf Wiedersehen!");
        System.exit(0);
        return null;
    }
}