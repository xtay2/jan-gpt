package app.records.console.model;

import app.managers.frontend.ViewManager;
import app.records.GPTModel;
import app.records.console.ConsoleCommand;

import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public class ChangeModelCommand extends ConsoleCommand {

    public ChangeModelCommand() {
        super(
                "set-model [model-name]",
                "Setzt das Modell, das für die Generierung verwendet wird.",
                CommandGroup.MODEL
        );
    }

    @Override
    public void apply(String input, Consumer<String> outStream, Consumer<String> errStream, ViewManager manager) {
        if (input.isEmpty()) {
            errStream.accept("Bitte gib ein Modell an. Verfügbare Versionen:\n" + GPTModel.modelString());
            return;
        }
        if (manager.setGPTModel(input))
            outStream.accept("Modell erfolgreich geändert.");
        else
            errStream.accept("Modell konnte nicht geändert werden. Verfügbare Versionen:\n" + GPTModel.modelString());
    }

}