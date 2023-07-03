package main.java.app.records.console;

import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.GPTModel;

/**
 * @author Dennis Woithe
 */
public class GetModelsCommand extends ConsoleCommand {

    public GetModelsCommand() {
        super("models", "Gibt alle verfügbaren GPT-Modelle aus.");
    }

    @Override
    public String apply(String input, ViewManager manager) {
        return "Verfügbare Modelle:\n" + GPTModel.modelString();
    }
}