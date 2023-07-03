package main.java.app.managers.backend;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import main.java.app.records.Message;

import java.io.File;
import java.io.IOException;
import java.io.SyncFailedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static main.java.app.Main.BASE_DATA_PATH;

/**
 * @author Dennis Woithe
 */
public interface ConversationManager {

    String CONVERSATIONS_PATH = BASE_DATA_PATH + "conversations";

    private static Path conversationPath(String conversationName) {
        return Path.of(CONVERSATIONS_PATH + File.separator + conversationName + ".json");
    }

    static boolean saveConversation(String conversationName, List<Message> messages) {
        if (conversationName == null || conversationName.isBlank() || messages == null || messages.isEmpty())
            return false;
        try {
            var path = conversationPath(conversationName);
            if (Files.exists(path)) return false;
            Files.createDirectories(path.getParent());
            Files.write(path, new Gson().toJson(messages).getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    static Optional<List<Message>> loadConversation(String conversationName) {
        if (conversationName == null || conversationName.isBlank()) return Optional.empty();
        try {
            var path = conversationPath(conversationName);
            if (!Files.exists(path)) return Optional.empty();
            var json = Files.readString(path);
            var messages = new Gson().fromJson(json, Message[].class);
            return Optional.of(Arrays.stream(messages).collect(Collectors.toList()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    static Optional<List<String>> getConversationNames() {
        try (var files = Files.walk(Path.of(CONVERSATIONS_PATH))) {
            return Optional.of(files
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(s -> s.endsWith(".json"))
                    .map(s -> s.substring(0, s.lastIndexOf(".json")))
                    .collect(Collectors.toList())
            );
        } catch (IOException e) {
            return Optional.empty();
        }
    }

}