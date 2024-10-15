package work.vietdefi.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * GsonConverter implements the IGsonConverter interface using the Gson library
 * for converting between Java objects, JSON strings, and JsonElement objects.
 */
public class GsonConverter implements IGsonConverter {

    private final Gson gson = new Gson();

    @Override
    public String toJsonString(Object object) {
        // Converts a Java object to its JSON string representation
        return gson.toJson(object);
    }

    @Override
    public <T> T fromJsonString(String jsonString, Class<T> clazz) {
        // Converts a JSON string to a Java object of the specified type
        return gson.fromJson(jsonString, clazz);
    }

    @Override
    public JsonElement toJsonElement(String jsonString) {
        // Parses a JSON string to create a JsonElement
        return JsonParser.parseString(jsonString);
    }

    @Override
    public JsonElement toJsonElement(Object object) {
        // Converts a Java object to a JsonElement representation
        return gson.toJsonTree(object);
    }

    @Override
    public String fromJsonElementToString(JsonElement jsonElement) {
        // Converts a JsonElement back to its JSON string representation
        return gson.toJson(jsonElement);
    }

    @Override
    public <T> T fromJsonElement(JsonElement jsonElement, Class<T> clazz) {
        // Converts a JsonElement to a Java object of the specified type
        return gson.fromJson(jsonElement, clazz);
    }
}
