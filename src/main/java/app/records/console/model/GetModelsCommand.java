package app.records.console.model;

import app.managers.frontend.ViewManager;
import app.records.GPTModel;
import app.records.console.ConsoleCommand;

import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public class GetModelsCommand extends ConsoleCommand {

    public GetModelsCommand() {
        super(
                "models",
                "Gibt alle verfügbaren GPT-Modelle aus.",
                CommandGroup.MODEL
        );
    }

    @Override
    public void apply(String input, Consumer<String> outStream, Consumer<String> errStream, ViewManager manager) {
        outStream.accept("Verfügbare Modelle:\n" + GPTModel.modelString());
    }
}