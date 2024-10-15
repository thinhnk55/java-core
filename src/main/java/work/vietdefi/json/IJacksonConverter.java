package work.vietdefi.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * IJacksonConverter provides methods for converting between Java objects,
 * JSON strings, and JsonNode objects using the Jackson library.
 * These methods throw JsonProcessingException in case of conversion failures.
 */
public interface IJacksonConverter {

    /**
     * Converts a Java object to its JSON string representation.
     *
     * @param object The Java object to convert.
     * @return The JSON string representation of the object.
     * @throws JsonProcessingException If the conversion fails.
     */
    String toJsonString(Object object) throws JsonProcessingException;

    /**
     * Converts a JSON string to a Java object of the specified type.
     *
     * @param jsonString The JSON string to convert.
     * @param clazz      The class of the Java object to create.
     * @param <T>        The type of the Java object.
     * @return The Java object created from the JSON string.
     * @throws JsonProcessingException If the conversion fails.
     */
    <T> T fromJsonString(String jsonString, Class<T> clazz) throws JsonProcessingException;

    /**
     * Parses a JSON string into a JsonNode object.
     *
     * @param jsonString The JSON string to parse.
     * @return The JsonNode representation of the JSON string.
     * @throws JsonProcessingException If parsing fails.
     */
    JsonNode toJsonNode(String jsonString) throws JsonProcessingException;

    /**
     * Converts a Java object to a JsonNode.
     *
     * @param object The Java object to convert.
     * @return The JsonNode representation of the Java object.
     */
    JsonNode toJsonNode(Object object);

    /**
     * Converts a JsonNode to a JSON string.
     *
     * @param jsonNode The JsonNode to convert.
     * @return The JSON string representation of the JsonNode.
     */
    String fromJsonNodeToString(JsonNode jsonNode);

    /**
     * Converts a JsonNode to a Java object of the specified type.
     *
     * @param jsonNode The JsonNode to convert.
     * @param clazz    The class of the Java object to create.
     * @param <T>      The type of the Java object.
     * @return The Java object created from the JsonNode.
     * @throws JsonProcessingException If the conversion fails.
     */
    <T> T fromJsonNode(JsonNode jsonNode, Class<T> clazz) throws JsonProcessingException;
}
