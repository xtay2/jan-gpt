package app.managers.frontend;

import app.managers.backend.GPTPort;
import app.records.GPTVersion;

import java.util.Optional;

/**
 * @author Dennis Woithe
 */
public class BasicViewManager implements ViewManager {

    private GPTPort gptPort;
    private GPTVersion gptVersion;

    @Override
    public boolean setAPIKey(String apiKey) {
        gptPort = GPTPort.getInstance(apiKey);
        return gptPort.testConnection(gptVersion);
    }

    @Override
    public boolean setGPTVersion(GPTVersion version) {
        gptVersion = version;
        return gptPort.testConnection(gptVersion);
    }

    @Override
    public Optional<String> callGPT(String prompt) {
        return gptPort.callGPT(gptVersion, prompt);
    }
}