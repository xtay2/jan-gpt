package main.java.app.records.console.conv;

import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.console.ConsoleCommand;

import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public class GetConversationsCommand extends ConsoleCommand {

    public GetConversationsCommand() {
        super(
                "convs",
                "Gibt die Namen aller gespeicherten Konversationen aus.",
                CommandGroup.CONVERSATION
        );
    }

    @Override
    public void apply(String input, Consumer<String> outStream, Consumer<String> errStream, ViewManager manager) {
        manager.getConversations().ifPresentOrElse(conv -> {
            if (conv.isEmpty())
                outStream.accept("Es sind keine Konversationen gespeichert.");
            else
                outStream.accept("Gespeicherte Konversationen:\n" + String.join("\n", conv));
        }, () -> errStream.accept("Die Gespeicherten Konversationen konnten nicht ausgelesen werden."));
    }
}