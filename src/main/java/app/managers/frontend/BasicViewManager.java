package main.java.app.managers.frontend;

import main.java.app.managers.backend.ApiKeyManager;
import main.java.app.managers.backend.GPTPort;
import main.java.app.records.GPTModel;

import java.util.Optional;

/**
 * @author Dennis Woithe
 */
public class BasicViewManager implements ViewManager {

    private GPTPort gptPort;
    private GPTModel gptModel = GPTModel.GPT_3_5;

    public BasicViewManager() {
        ApiKeyManager.getApiKey().ifPresent(this::setAPIKey);
    }

    @Override
    public boolean hasAPIKey() {
        return gptPort != null;
    }

    @Override
    public boolean setAPIKey(String apiKey) {
        gptPort = GPTPort.getInstance(apiKey);
        return gptPort.testConnection(gptModel) && ApiKeyManager.saveApiKey(apiKey);
    }

    @Override
    public boolean setGPTModel(GPTModel model) {
        gptModel = model;
        return gptPort.testConnection(gptModel);
    }

    @Override
    public Optional<String> callGPT(String prompt) throws GPTPort.MissingAPIKeyException, GPTPort.MissingModelException {
        if (gptPort == null)
            throw new GPTPort.MissingAPIKeyException();
        return gptPort.callGPT(gptModel, prompt);
    }
}