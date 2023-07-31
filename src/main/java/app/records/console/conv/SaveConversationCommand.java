package app.records.console.conv;

import app.managers.frontend.ViewManager;
import app.records.console.ConsoleCommand;

import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public class SaveConversationCommand extends ConsoleCommand {

    public SaveConversationCommand() {
        super(
                "save-conv [name]",
                "Speichert die aktuelle Konversation unter dem angegebenen Namen.",
                CommandGroup.CONVERSATION
        );
    }

    @Override
    public void apply(String input, Consumer<String> outStream, Consumer<String> errStream, ViewManager manager) {
        if (input.isBlank()) {
            errStream.accept("Bitte gib einen Namen einzigartigen Namen für die Konversation an.");
            return;
        }
        if (manager.saveConversationAs(input.strip())) {
            outStream.accept("Konversation wurde gespeichert.");
            return;
        }
        errStream.accept("Konversation konnte nicht gespeichert werden.");
    }
}