package app.records.console.conv;

import app.managers.frontend.ViewManager;
import app.records.console.ConsoleCommand;

import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public class DeleteConversationCommand extends ConsoleCommand {

    public DeleteConversationCommand() {
        super(
                "delete-conv [name]",
                "Deletes the conversation with the given name",
                CommandGroup.CONVERSATION
        );
    }

    @Override
    public void apply(String input, Consumer<String> outStream, Consumer<String> errStream, ViewManager manager) {
        if(manager.deleteConversation(input))
            outStream.accept("Unterhaltung \"" + input + "\" gelöscht.");
        else
            errStream.accept("Unterhaltung \"" + input + "\" wurde nicht gefunden oder konnte nicht gelöscht werden.");
    }
}