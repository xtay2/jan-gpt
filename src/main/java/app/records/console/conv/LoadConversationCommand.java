package main.java.app.records.console.conv;

import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.console.ConsoleCommand;

import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Dennis Woithe
 */
public class LoadConversationCommand extends ConsoleCommand {

    public LoadConversationCommand() {
        super(
                "load-conv [name]",
                "Lädt die Konversation mit dem angegebenen Namen.",
                CommandGroup.CONVERSATION
        );
    }


    @Override
    public void apply(String input, Consumer<String> outStream, Consumer<String> errStream, ViewManager manager) {
        manager.loadConversation(input.trim())
                .map(conv -> "Konversation \"" + input.trim() + "\" wurde rekonstruiert:\n" +
                        conv.stream()
                        .map(c -> c.role().alias(true) + ": " + c.content() + "\n")
                        .collect(Collectors.joining("\n"))
                ).ifPresentOrElse(
                        outStream,
                        () -> errStream.accept("Konversation konnte nicht geladen werden.")
                );
    }
}