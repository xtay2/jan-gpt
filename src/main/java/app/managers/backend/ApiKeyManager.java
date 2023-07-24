package main.java.app.managers.backend;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static main.java.Main.BASE_DATA_PATH;

/**
 * @author Dennis Woithe
 */
public interface ApiKeyManager {

    Path API_KEY_FILE_PATH = Path.of(BASE_DATA_PATH + "api_key.txt");

    static boolean saveApiKey(String apiKey) {
        try {
            Files.createDirectories(API_KEY_FILE_PATH.getParent());
            Files.writeString(API_KEY_FILE_PATH, apiKey);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static Optional<String> getApiKey() {
        try {
            return Optional.of(Files.readString(API_KEY_FILE_PATH));
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}