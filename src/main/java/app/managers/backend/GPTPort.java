package app.managers.backend;

import app.records.GPTModel;
import app.records.Message;

import java.util.List;
import java.util.Optional;

/**
 * @author Dennis Woithe
 */
public interface GPTPort {

    /**
     * Receive a GPTPort instance.
     *
     * @param apiKey The API key to use for the GPT-API.
     */
    static GPTPort getInstance(String apiKey) {
        return new GPTManager(apiKey);
    }

    /**
     * Send a prompt to the GPT-3 API and return the response.
     *
     * @return The response from the API or {@link Optional#empty()} if the API call failed.
     */
    Optional<String> callGPT(GPTModel model, String prompt) throws MissingAPIKeyException, MissingModelException;

    /**
     * Test the connection to the GPT API.
     */
    boolean testConnection();

    /** Returns the messages of the current conversation. */
    List<Message> getMessages();

    /** Sets the messages of the current conversation. */
    void setMessages(List<Message> messages);

    void setTimeoutSec(int sec);

    class MissingAPIKeyException extends Exception {
        public MissingAPIKeyException() {
            super("No API key was provided.");
        }
    }

    class MissingModelException extends Exception {
        public MissingModelException() {
            super("No model was provided.");
        }
    }

}