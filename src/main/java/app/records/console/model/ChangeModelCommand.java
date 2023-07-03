package main.java.app.records.console.model;

import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.GPTModel;
import main.java.app.records.console.ConsoleCommand;

import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public class ChangeModelCommand extends ConsoleCommand {

    public ChangeModelCommand() {
        super(
                "set-model [model-nr]",
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
        GPTModel.valueOf(input).ifPresentOrElse(model -> {
                    if (manager.setGPTModel(model))
                        outStream.accept("Modell erfolgreich geändert.\n");
                    else
                        errStream.accept("Modell konnte nicht geändert werden.\n");
                },
                () -> errStream.accept(
                        "Modell \"" + input + "\" konnte nicht gefunden werden. Verfügbare Versionen:\n" + GPTModel.modelString() + "\n"
                )
        );
    }

}