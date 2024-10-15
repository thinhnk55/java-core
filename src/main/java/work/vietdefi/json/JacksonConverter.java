package work.vietdefi.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JacksonConverter implements the IJacksonConverter interface using the Jackson library
 * to convert between Java objects, JSON strings, and JsonNode objects.
 */
public class JacksonConverter implements IJacksonConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toJsonString(Object object) throws JsonProcessingException {
        // Converts a Java object to its JSON string representation.
        return objectMapper.writeValueAsString(object);
    }

    @Override
    public <T> T fromJsonString(String jsonString, Class<T> clazz) throws JsonProcessingException {
        // Converts a JSON string to a Java object of the specified type.
        return objectMapper.readValue(jsonString, clazz);
    }

    @Override
    public JsonNode toJsonNode(String jsonString) throws JsonProcessingException {
        // Parses a JSON string to create a JsonNode.
        return objectMapper.readTree(jsonString);
    }

    @Override
    public JsonNode toJsonNode(Object object) {
        // Converts a Java object to a JsonNode representation.
        return objectMapper.valueToTree(object);
    }

    @Override
    public String fromJsonNodeToString(JsonNode jsonNode) {
        // Converts a JsonNode to a JSON string representation.
        return jsonNode.toString();
    }

    @Override
    public <T> T fromJsonNode(JsonNode jsonNode, Class<T> clazz) throws JsonProcessingException {
        // Converts a JsonNode to a Java object of the specified type.
        return objectMapper.treeToValue(jsonNode, clazz);
    }
}
