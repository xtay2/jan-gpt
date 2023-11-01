package app.managers.frontend;


import app.managers.backend.GPTPort;
import app.records.GPTModel;
import app.records.Message;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * @author Dennis Woithe
 */
public interface ViewManager {

    /**
     * Return true if a valid API key was set.
     */
    boolean hasAPIKey();

    /**
     * Set the API key to use for the GPT API.
     *
     * @return true if the API key was set successfully and can establish a connection.
     */
    boolean setAPIKey(String apiKey);

    /**
     * Return true if a valid GPT version was set.
     */
    boolean hasGPTModel();

    /**
     * Set the GPT version to use.
     *
     * @return false if null was passed or the version is not supported.
     */
    boolean setGPTModel(String version);

    /**
     * Set the GPT version to use.
     *
     * @return false if null was passed or the version is not supported.
     */
    boolean setGPTModel(GPTModel version);

    /**
     * Send a prompt to the GPT-3 API and return the response.
     *
     * @return The response from the API or {@link Optional#empty()} if the API call failed.
     */
    Optional<String> callGPT(String prompt) throws GPTPort.MissingAPIKeyException, GPTPort.MissingModelException, TimeoutException;

    /**
     * Return the currently set {@link GPTModel}.
     */
    Optional<GPTModel> getGPTModel();

    /**
     * Save the current conversation to a file.
     */
    boolean saveConversationAs(String name);

    /**
     * Return the names of all saved conversations.
     */
    Optional<List<String>> getConversations();

    /**
     * Load a conversation from a file.
     */
    Optional<List<Message>> loadConversation(String name);

    /**
     * Start a new conversation.
     */
    void newConversation();

    /**
     * Delete a conversation-file.
     */
    boolean deleteConversation(String name);

    void setTimeoutSec(int timeout);
}