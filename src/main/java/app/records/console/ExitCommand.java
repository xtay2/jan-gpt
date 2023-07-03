package main.java.app.records.console;

import main.java.app.managers.frontend.ViewManager;
import main.java.app.views.ConsoleView;

/**
 * @author Dennis Woithe
 */
public class ExitCommand extends ConsoleCommand {

    protected ExitCommand() {
        super("exit", "Beendet das Programm.");
    }

    @Override
    public String apply(String input, ViewManager manager) {
        System.out.println(ConsoleView.SHELL_NAME + "> Auf Wiedersehen!");
        System.exit(0);
        return null;
    }
}