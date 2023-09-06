package app.managers.backend;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import app.records.GPTModel;
import app.records.Message;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class GPTManagerTest {

    @Mock
    String mockApi = "mockedApiKey";

    @Mock
    GPTModel mockModel = GPTModel.getNewest().orElse(null);
    @InjectMocks
    GPTManager systemUnderTest = mock(GPTManager.class);

    final URI GPT_CHAT_URI = URI.create("https://api.openai.com/v1/chat/completions"), GPT_MODEL_URI = URI.create("https://api.openai.com/v1/models");
    final List<Message> messages = new ArrayList<>();
    final HttpClient httpClient = HttpClient.newHttpClient();
    final String apiKey = "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";

    @Test
    public void testCallGPT_whenApiKeyNull_thenThrowMissingAPIKeyException() {

        final String THROW_MESSAGE = "No API key was provided.";
        String apiKey = null;
        GPTManager gptManager = new GPTManager(apiKey);
        GPTModel gptModel = GPTModel.getNewest().orElse(null);

        GPTPort.MissingAPIKeyException thrown = assertThrows(GPTPort.MissingAPIKeyException.class, () ->
                {
                    gptManager.callGPT(gptModel, "Test-String");
                }
        );
        assertEquals(THROW_MESSAGE, thrown.getMessage());
    }

    @Test
    public void testCallGPT_whenGPTModelNull_thenThrowMissingModelException() {

        final String THROW_MESSAGE = "No model was provided.";
        GPTManager gptManager = new GPTManager(apiKey);
        GPTModel gptModel = null;

        GPTPort.MissingModelException thrown = assertThrows(GPTPort.MissingModelException.class, () ->
                {
                    gptManager.callGPT(gptModel, "Test-String");
                }
        );
        assertEquals(THROW_MESSAGE, thrown.getMessage());
    }

    @Test
    public void testCallGPT_whenPromptNull_thenReturnEmpty() throws GPTPort.MissingAPIKeyException, GPTPort.MissingModelException {
        GPTManager gptManager = new GPTManager(apiKey);
        GPTModel gptModel = GPTModel.getNewest().orElse(null);
        assertEquals(Optional.empty(), gptManager.callGPT(gptModel, null));
    }

    @Test
    public void testCallGPT_whenPromptEmpty_thenReturnEmpty() throws GPTPort.MissingAPIKeyException, GPTPort.MissingModelException {
        GPTManager gptManager = new GPTManager(apiKey);
        GPTModel gptModel = GPTModel.getNewest().orElse(null);
        assertEquals(Optional.empty(), gptManager.callGPT(gptModel, ""));
    }

    @Test
    public void testCallGPT_happyPath_thenReturnResponseMessage() throws GPTPort.MissingAPIKeyException, GPTPort.MissingModelException, IOException, InterruptedException {

        final Optional<String> EXPECTED_RESPONSE = Optional.of("0");
        final String PROMPT = "Antworte mit genau einer 0";

        // Mockito TODO
//        when(httpClient.send(any(), any()));

        GPTModel gptModel = GPTModel.getNewest().orElse(null);
        assert gptModel != null;

        // Mockito TODO
//        Optional<String> response = systemUnderTest.callGPT(gptModel, PROMPT);

        GPTManager gptManager = new GPTManager(apiKey);

        Optional<String> response = gptManager.callGPT(gptModel, PROMPT);

        assertFalse(response.isEmpty());
        assertNotNull(response);
        assertEquals(EXPECTED_RESPONSE.get(), response.get());
    }
}
