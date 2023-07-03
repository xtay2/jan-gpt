package main.java.app.managers.frontend;

import main.java.app.managers.backend.ApiKeyManager;
import main.java.app.managers.backend.ConversationManager;
import main.java.app.managers.backend.GPTPort;
import main.java.app.records.GPTModel;
import main.java.app.records.Message;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public boolean saveConversationAs(String name) {
        return ConversationManager.saveConversation(name,gptPort.getMessages());
    }

    @Override
    public Optional<List<String>> getConversations() {
        return ConversationManager.getConversationNames();
    }

    @Override
    public Optional<List<Message>> loadConversation(String name) {
        var conv = ConversationManager.loadConversation(name);
        conv.ifPresent(gptPort::setMessages);
        return conv;
    }

    @Override
    public void newConversation() {
        gptPort.setMessages(new ArrayList<>());
    }
}