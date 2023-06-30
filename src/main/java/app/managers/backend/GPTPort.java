package app.managers.backend;

import app.records.GPTModel;

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
    Optional<String> callGPT(GPTModel model, String prompt) throws MissingAPIKeyException;

    /**
     * Test the connection to the GPT API.
     */
    boolean testConnection(GPTModel model);

    class MissingAPIKeyException extends Exception {
        public MissingAPIKeyException() {
            super("No API key was provided.");
        }
    }

}