package main.java.app.managers.backend;


import main.java.app.records.GPTModel;

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
    public Optional<String> callGPT(GPTModel model, String prompt) throws GPTPort.MissingAPIKeyException {
        if(apiKey == null)
            throw new GPTPort.MissingAPIKeyException();
        if(model == null || prompt == null)
            return Optional.empty();

        return Optional.empty();
    }

    @Override
    public boolean testConnection(GPTModel model) {
        return false;
    }
}