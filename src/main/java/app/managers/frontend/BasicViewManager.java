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
    private GPTModel gptModel = GPTModel.getNewest().orElse(null);

    public BasicViewManager() {
        ApiKeyManager.getApiKey().ifPresent(this::setAPIKey);
    }

    @Override
    public boolean hasAPIKey() {
        return gptPort != null && gptPort.testConnection();
    }

    @Override
    public boolean hasGPTModel() {
        return gptModel != null;
    }

    @Override
    public boolean setAPIKey(String apiKey) {
        gptPort = GPTPort.getInstance(apiKey);
        return gptPort.testConnection() && ApiKeyManager.saveApiKey(apiKey);
    }

    @Override
    public boolean setGPTModel(GPTModel model) {
        if (model == null)
            return false;
        gptModel = model;
        return gptPort.testConnection();
    }

    @Override
    public Optional<String> callGPT(String prompt) throws GPTPort.MissingAPIKeyException, GPTPort.MissingModelException {
        if (gptPort == null)
            throw new GPTPort.MissingAPIKeyException();
        return gptPort.callGPT(gptModel, prompt);
    }

    @Override
    public Optional<GPTModel> getGPTModel() {
        return Optional.ofNullable(gptModel);
    }
}