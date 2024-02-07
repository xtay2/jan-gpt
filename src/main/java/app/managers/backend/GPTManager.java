package app.managers.backend;

import app.records.GPTModel;
import app.records.Message;
import app.records.Role;
import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * @author Dennis Woithe
 */
public class GPTManager implements GPTPort {

    private static final URI GPT_CHAT_URI = URI.create("https://api.openai.com/v1/chat/completions"), GPT_MODEL_URI = URI.create("https://api.openai.com/v1/models"), GPT_STREAM_URI = URI.create("https://api.openai.com/v1/chat/completions");

    private final List<Message> messages = new ArrayList<>();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiKey;
    private Duration timeout = Duration.of(120, java.time.temporal.ChronoUnit.SECONDS);

    public GPTManager(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Optional<String> callGPT(GPTModel model, String prompt) throws GPTPort.MissingAPIKeyException, MissingModelException, TimeoutException {
        if (apiKey == null) throw new GPTPort.MissingAPIKeyException();
        if (model == null) throw new GPTPort.MissingModelException();
        if (prompt == null) return Optional.empty();
        // PREPARE REQUEST
        messages.add(new Message(Role.USER, prompt));
        var request = HttpRequest.newBuilder().uri(GPT_CHAT_URI).POST(HttpRequest.BodyPublishers.ofString(buildPromptRequestBody(model).toString()))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json").timeout(timeout).build();
        // RECEIVE RESPONSE
        try {
            var responseBody = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            var responseMsg = parseJsonResponse(responseBody.strip());
            responseMsg.ifPresent(s -> messages.add(new Message(Role.ASSISTANT, s)));
            return responseMsg;
        } catch (HttpConnectTimeoutException e) {
            throw new TimeoutException(e.getMessage());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private JsonObject buildPromptRequestBody(GPTModel model) {
        var requestBody = new JsonObject();
        requestBody.add("model", new JsonPrimitive(model.modelName));
        requestBody.add("messages", messages.stream().map(Message::toJsonObject).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        return requestBody;
    }

    private Optional<String> parseJsonResponse(String response) {
        if (response == null || response.isBlank()) return Optional.empty();
        var jsonResponse = new Gson().fromJson(response, JsonObject.class);
        try {
            return Optional.of(jsonResponse.get("choices").getAsJsonArray().get(0).getAsJsonObject().get("message").getAsJsonObject().get("content").getAsString().trim());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "StatementWithEmptyBody"})
    @Override
    public void streamGPT(GPTModel model, String prompt, Consumer<String> consumer) throws MissingAPIKeyException, MissingModelException {
        if (apiKey == null) throw new GPTPort.MissingAPIKeyException();
        if (model == null) throw new GPTPort.MissingModelException();
        if (prompt == null) return;
        var request = HttpRequest.newBuilder().uri(GPT_STREAM_URI)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString("""
                        {
                            "model": "%s",
                            "messages": [
                              {
                                "role": "user",
                                "content": "%s"
                              }
                            ],
                            "stream": true
                        }
                        """.formatted(model.modelName, prompt))).build();
        var wholeMsgBuilder = new StringBuilder();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()).thenApply(HttpResponse::body).thenAccept(body -> {
            try (var reader = new InputStreamReader(body)) {
                var buffer = new StringBuilder();
                int read;
                reader.read(new char["data: ".length()]);
                while ((read = reader.read()) != -1) {
                    buffer.append((char) read);
                    if (read == '}') {
                        var optJson = tryParseJson(buffer.toString());
                        if (optJson.isEmpty())
                            continue;
                        optJson.ifPresent(str -> {
                            wholeMsgBuilder.append(str);
                            consumer.accept(str);
                        });
                        buffer = new StringBuilder();
                        while ((read = reader.read()) != -1 && read != '{') ;
                        buffer.append((char) read);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).join();
        messages.add(new Message(Role.USER, prompt));
        messages.add(new Message(Role.ASSISTANT, wholeMsgBuilder.toString()));
    }

    private Optional<String> tryParseJson(String json) {
        try {
            return Optional.of(JsonParser.parseString(json))
                    .map(JsonElement::getAsJsonObject)
                    .map(jsonObject -> jsonObject.get("choices"))
                    .map(JsonElement::getAsJsonArray)
                    .map(jsonElements -> jsonElements.get(0))
                    .map(JsonElement::getAsJsonObject)
                    .map(jsonObject -> jsonObject.get("delta"))
                    .map(JsonElement::getAsJsonObject)
                    .map(jsonObject -> jsonObject.get("content"))
                    .map(JsonElement::getAsString);
        } catch (JsonSyntaxException | IllegalStateException e) {
            return Optional.empty();
        }
    }


    @Override
    public boolean testConnection() {
        try {
            return httpClient.send(HttpRequest.newBuilder().uri(GPT_MODEL_URI).GET().header("Authorization", "Bearer " + apiKey).timeout(timeout).build(), HttpResponse.BodyHandlers.ofString()).statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void setMessages(List<Message> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
    }

    @Override
    public void setTimeoutSec(int sec) {
        timeout = Duration.ofSeconds(sec);
    }


}