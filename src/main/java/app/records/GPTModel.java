package app.records;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import app.managers.backend.ApiKeyManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Dennis Woithe
 */
public class GPTModel {

    public static final URI MODELS_URI = URI.create("https://api.openai.com/v1/models");

    private static final Set<GPTModel> MODELS = new HashSet<>();

    /**
     * Fetches all models from the OpenAI API and returns them as a set of {@link GPTModel}.
     */
    public static Optional<Set<GPTModel>> getModels() {
        if (!MODELS.isEmpty())
            return Optional.of(MODELS);
        try {
            var apikey = ApiKeyManager.getApiKey();
            if (apikey.isEmpty())
                return Optional.empty();
            JsonParser.parseString(
                            HttpClient.newHttpClient().send(
                                    HttpRequest.newBuilder(MODELS_URI)
                                            .GET()
                                            .header("Authorization", "Bearer " + apikey.get())
                                            .build(),
                                    HttpResponse.BodyHandlers.ofString()
                            ).body()
                    ).getAsJsonObject().getAsJsonArray("data").asList().stream().map(JsonElement::getAsJsonObject)
                    .filter(jsonObject -> jsonObject.getAsJsonPrimitive("id").getAsString().startsWith("gpt"))
                    .map(jObj -> new GPTModel(jObj.get("id").getAsString(), jObj.get("created").getAsLong())
                    ).forEach(MODELS::add);
            return Optional.of(MODELS);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public final String modelName;
    private final LocalDateTime created;

    private GPTModel(String modelName, long createdTs) {
        this.modelName = modelName;
        this.created = LocalDateTime.ofEpochSecond(createdTs, 0, ZoneOffset.UTC);
    }

    public static Optional<GPTModel> valueOf(String model) {
        for (GPTModel gptModel : MODELS) {
            if (gptModel.modelName.equalsIgnoreCase(model))
                return Optional.of(gptModel);
        }
        return Optional.empty();
    }

    public static Set<String> values() {
        return getModels().orElse(Collections.emptySet()).stream()
                .map(gptModel -> gptModel.modelName)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static Optional<GPTModel> getNewest() {
        return getModels().flatMap(m -> m.stream().max(Comparator.comparing(o -> o.created)));
    }

    public static String modelString() {
        return getModels().map(models -> models.stream()
                .sorted(Comparator.comparing(o -> o.modelName))
                .map(gptModel -> "- " + gptModel.modelName + " (" + gptModel.created.format(DateTimeFormatter.ofPattern("dd.MM.yy")) + ")")
                .collect(Collectors.joining("\n"))
        ).orElse("Keine");
    }
}