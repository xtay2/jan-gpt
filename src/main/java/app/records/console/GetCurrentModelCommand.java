package main.java.app.records.console;

import main.java.app.managers.frontend.ViewManager;

/**
 * @author Dennis Woithe
 */
public class GetCurrentModelCommand extends ConsoleCommand {

    protected GetCurrentModelCommand() {
        super("get-model", "Gibt das aktuell verwendete Modell aus.");
    }

    @Override
    public String apply(String input, ViewManager manager) {
        var model = manager.getGPTModel();
        return model.map(m -> "Aktuelles Modell: " + m.modelName)
                .orElse("Kein Modell ausgewählt.");
    }
}