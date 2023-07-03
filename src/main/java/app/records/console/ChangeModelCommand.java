package main.java.app.records.console;

import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.GPTModel;

/**
 * @author Dennis Woithe
 */
public class ChangeModelCommand extends ConsoleCommand {

    public ChangeModelCommand() {
        super("set-model [model-nr]", "Setzt das Modell, das für die Generierung verwendet wird.");
    }

    @Override
    public String apply(String input, ViewManager manager) {
        if (input.isEmpty())
            return "Bitte gib ein Modell an. Verfügbare Versionen:\n" + GPTModel.modelString();
        var model = GPTModel.valueOf(input);
        return model.isPresent() && manager.setGPTModel(model.get())
                ? "Modell erfolgreich geändert."
                : "Modell konnte nicht geändert werden. Verfügbare Versionen:\n" + GPTModel.modelString();
    }

}