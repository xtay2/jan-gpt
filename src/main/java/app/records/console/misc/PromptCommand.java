package app.records.console.misc;


import app.managers.backend.GPTPort;
import app.managers.frontend.ViewManager;
import app.records.console.ConsoleCommand;

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
            manager.callGPT(input).ifPresentOrElse(
                    outStream,
                    () -> errStream.accept("Ich habe dich leider nicht verstanden.")
            );
        } catch (GPTPort.MissingAPIKeyException e) {
            errStream.accept("Ich kann dir leider nicht helfen, da ich keine API-Schlüssel habe.");
        } catch (GPTPort.MissingModelException e) {
            errStream.accept("Bitte lege das Modell fest, mit dem ich arbeiten soll.");
        } catch (Exception e) {
            errStream.accept("Es ist ein Fehler aufgetreten: " + e.getMessage());
        }
    }
}