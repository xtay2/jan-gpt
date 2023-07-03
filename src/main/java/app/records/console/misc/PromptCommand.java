package main.java.app.records.console.misc;

import main.java.app.managers.backend.GPTPort;
import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.console.ConsoleCommand;

import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public class PromptCommand extends ConsoleCommand {

    public PromptCommand() {
        super("", "Anfrage an das ausgewählte GPT-Modell", CommandGroup.MISC);
    }

    @Override
    public void apply(String input, Consumer<String> outStream, Consumer<String> errStream, ViewManager manager) {
        try {
            manager.callGPT(input).map(s -> s + "\n").ifPresentOrElse(
                    outStream,
                    () -> errStream.accept("Ich habe dich leider nicht verstanden.")
            );
        } catch (GPTPort.MissingAPIKeyException e) {
            errStream.accept("Ich kann dir leider nicht helfen, da ich keine API-Schlüssel habe.");
        } catch (GPTPort.MissingModelException e) {
            errStream.accept("Bitte lege das Modell fest, mit dem ich arbeiten soll.");
        }
    }
}