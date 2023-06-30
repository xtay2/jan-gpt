package app.managers.backend;

import app.records.GPTVersion;

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
    Optional<String> callGPT(GPTVersion version, String prompt);

    /**
     * Test the connection to the GPT API.
     */
    boolean testConnection(GPTVersion version);
}