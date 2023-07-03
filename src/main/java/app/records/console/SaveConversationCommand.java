package main.java.app.records.console;

import main.java.app.managers.frontend.ViewManager;

/**
 * @author Dennis Woithe
 */
public class SaveConversationCommand extends ConsoleCommand {

    protected SaveConversationCommand() {
        super("save-conversation [name]", "Speichert die aktuelle Konversation unter dem angegebenen Namen.");
    }

    @Override
    public String apply(String input, ViewManager manager) {
        if (input.isBlank()) return "Bitte gib einen Namen einzigartigen Namen für die Konversation an.";
        return manager.saveConversationAs(input.strip())
                ? "Konversation wurde gespeichert."
                : "Konversation konnte nicht gespeichert werden.";
    }
}