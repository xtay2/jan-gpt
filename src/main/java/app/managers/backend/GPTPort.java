package app.managers.backend;

import app.records.GPTModel;
import app.records.Message;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

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
     * Send a prompt to the GPT API and return the response.
     *
     * @return The response from the API or {@link Optional#empty()} if the API call failed.
     */
    Optional<String> callGPT(GPTModel model, String prompt)
            throws MissingAPIKeyException, MissingModelException, TimeoutException;

    /**
     * Send a prompt to the GPT API and streams the response token wise into the consumer.
     * This function is not asynchronous and will block until the response is fully streamed.
     */
    void streamGPT(GPTModel model, String prompt, Consumer<String> consumer)
            throws MissingAPIKeyException, MissingModelException;

    /**
     * Test the connection to the GPT API.
     */
    boolean testConnection();

    /**
     * Returns the messages of the current conversation.
     */
    List<Message> getMessages();

    /**
     * Sets the messages of the current conversation.
     */
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