package app.managers.frontend;

import app.managers.backend.GPTPort;
import app.records.GPTModel;

import java.util.Optional;

/**
 * @author Dennis Woithe
 */
public class BasicViewManager implements ViewManager {

    private GPTPort gptPort;
    private GPTModel gptModel;

    @Override
    public boolean setAPIKey(String apiKey) {
        gptPort = GPTPort.getInstance(apiKey);
        return gptPort.testConnection(gptModel);
    }

    @Override
    public boolean setGPTVersion(GPTModel model) {
        gptModel = model;
        return gptPort.testConnection(gptModel);
    }

    @Override
    public Optional<String> callGPT(String prompt) {
        return gptPort.callGPT(gptModel, prompt);
    }
}