package app.managers.backend;

import app.records.GPTVersion;

import java.util.Optional;

/**
 * @author Dennis Woithe
 */
public class GPTManager implements GPTPort {

    private final String apiKey;

    public GPTManager(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Optional<String> callGPT(GPTVersion version, String prompt) {
        if(version == null || prompt == null)
            return Optional.empty();

        return Optional.empty();
    }

    @Override
    public boolean testConnection(GPTVersion version) {
        return false;
    }
}