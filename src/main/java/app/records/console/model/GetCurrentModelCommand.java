package app.records.console.model;

import app.managers.frontend.ViewManager;
import app.records.console.ConsoleCommand;

import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public class GetCurrentModelCommand extends ConsoleCommand {

    public GetCurrentModelCommand() {
        super(
                "get-model",
                "Gibt das aktuell verwendete Modell aus.",
                CommandGroup.MODEL
        );
    }

    @Override
    public void apply(String input, Consumer<String> outStream, Consumer<String> errStream, ViewManager manager) {
        manager.getGPTModel().ifPresentOrElse(
                m -> outStream.accept("Aktuelles Modell: " + m.modelName),
                () -> errStream.accept("Kein Modell ausgewählt.")
        );
    }
}