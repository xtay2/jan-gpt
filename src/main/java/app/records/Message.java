package app.records;

import com.google.gson.JsonObject;


/**
 * @author Dennis Woithe
 */
public record Message(Role role, String content) {

    public JsonObject toJsonObject() {
        var jsonObject = new JsonObject();
        jsonObject.addProperty("role", role.toString());
        jsonObject.addProperty("content", content);
        return jsonObject;
    }

}