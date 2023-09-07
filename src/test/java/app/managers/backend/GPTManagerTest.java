package app.managers.backend;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import app.records.GPTModel;
import app.records.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GPTManagerTest {

    @Mock
    HttpClient httpClient;

    @Mock
    HttpResponse<String> httpResponse;

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
    public void testCallGPT_whenSuccessfulRequest_thenReturnResponseMessage() throws GPTPort.MissingAPIKeyException, GPTPort.MissingModelException, IOException, InterruptedException {

        final Optional<String> EXPECTED_RESPONSE = Optional.of("0");
        final String PROMPT = "Antworte mit genau einer 0";


        GPTModel gptModel = GPTModel.getNewest().orElse(null);
        assert gptModel != null;
        GPTManager gptManager = new GPTManager(apiKey);
        gptManager.httpClient = httpClient;
//        assertTrue(setPrivateField(gptManager, "httpClient", Mockito.mock(HttpClient.class)));


        final String responseMessage = "{\n" +
                "  \"id\": \"chatcmpl-7w91w1VxzHcHIpfUKI6nej691Z6MP\",\n" +
                "  \"object\": \"chat.completion\",\n" +
                "  \"created\": 1694092092,\n" +
                "  \"model\": \"gpt-4-0613\",\n" +
                "  \"choices\": [\n" +
                "    {\n" +
                "      \"index\": 0,\n" +
                "      \"message\": {\n" +
                "        \"role\": \"assistant\",\n" +
                "        \"content\": \"0\"\n" +
                "      },\n" +
                "      \"finish_reason\": \"stop\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"usage\": {\n" +
                "    \"prompt_tokens\": 15,\n" +
                "    \"completion_tokens\": 1,\n" +
                "    \"total_tokens\": 16\n" +
                "  }\n" +
                "}";


        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn(responseMessage);

        Optional<String> result = gptManager.callGPT(gptModel, PROMPT);
        assertEquals(EXPECTED_RESPONSE.get(), result.get());

        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));

    }

    @Test
    public void testCallGPT_whenStatus500_thenReturnOptionalEmpty() throws IOException, InterruptedException, GPTPort.MissingAPIKeyException, GPTPort.MissingModelException {

        GPTModel gptModel = GPTModel.getNewest().orElse(null);
        assert gptModel != null;
        GPTManager gptManager = new GPTManager(apiKey);
        gptManager.httpClient = httpClient;

        when(httpResponse.statusCode()).thenReturn(500);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        Optional<String> response = gptManager.callGPT(gptModel, "test-prompt");

        assertEquals(Optional.empty(), response);
    }


//    static <T> boolean setPrivateField(T instance, String fieldName, Object value) {
//        try {
//            var cls = instance.getClass();
//            var field = cls.getDeclaredField(fieldName);
//            field.setAccessible(true);
//            field.set(value, instance);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }


}
