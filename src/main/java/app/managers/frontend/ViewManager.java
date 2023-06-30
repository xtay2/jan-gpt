package main.java.app.managers.frontend;

import main.java.app.managers.backend.GPTPort;
import main.java.app.records.GPTModel;

import java.util.Optional;

/**
 * @author Dennis Woithe
 */
public interface ViewManager {

    /**
     * Set the API key to use for the GPT API.
     *
     * @return true if the API key was set successfully and can establish a connection.
     */
    boolean setAPIKey(String apiKey);

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
    Optional<String> callGPT(String prompt) throws GPTPort.MissingAPIKeyException;

}