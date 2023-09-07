package app.records.console.conv;

import app.managers.frontend.ViewManager;
import app.records.console.ConsoleCommand;

import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public class ClearConversationCommand extends ConsoleCommand {

    public ClearConversationCommand() {
        super(
                "clear-conv",
                "Lässt GPT den Kontext der aktuellen Konversation vergessen.",
                CommandGroup.CONVERSATION
        );
    }

    @Override
    public void apply(String input, Consumer<String> outStream, Consumer<String> errStream, ViewManager manager) {
        manager.newConversation();
        outStream.accept("Ich habe den Kontext der aktuellen Konversation vergessen. Lass uns über etwas anderes reden!");
    }
}