package work.vietdefi.json;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JacksonConverterTest {

    private JacksonConverter converter;

    // Setup before each test
    @BeforeEach
    public void setUp() {
        converter = new JacksonConverter();
    }

    @Test
    public void testToJsonString() throws JsonProcessingException {
        // Create a sample object
        Person person = new Person("Alice", 30);

        // Convert to JSON string
        String jsonString = converter.toJsonString(person);

        // Verify the result
        assertEquals("{\"name\":\"Alice\",\"age\":30}", jsonString);
    }

    @Test
    public void testFromJsonString() throws JsonProcessingException {
        // JSON string to be converted
        String json = "{\"name\":\"Bob\",\"age\":25}";

        // Convert JSON string to Person object
        Person person = converter.fromJsonString(json, Person.class);

        // Verify the result
        assertEquals("Bob", person.name);
        assertEquals(25, person.age);
    }

    @Test
    public void testToJsonNode() throws JsonProcessingException {
        // JSON string to be parsed
        String json = "{\"name\":\"Charlie\",\"age\":35}";

        // Parse to JsonNode
        JsonNode jsonNode = converter.toJsonNode(json);

        // Verify the result
        assertEquals("Charlie", jsonNode.get("name").asText());
        assertEquals(35, jsonNode.get("age").asInt());
    }

    @Test
    public void testToJsonNodeFromObject() {
        // Create a sample object
        Person person = new Person("Dana", 28);

        // Convert to JsonNode
        JsonNode jsonNode = converter.toJsonNode(person);

        // Verify the result
        assertEquals("Dana", jsonNode.get("name").asText());
        assertEquals(28, jsonNode.get("age").asInt());
    }

    @Test
    public void testFromJsonNodeToString() {
        // Create a JsonNode manually
        JsonNode jsonNode = converter.toJsonNode(new Person("Eve", 40));

        // Convert JsonNode to JSON string
        String jsonString = converter.fromJsonNodeToString(jsonNode);

        // Verify the result
        assertEquals("{\"name\":\"Eve\",\"age\":40}", jsonString);
    }

    @Test
    public void testFromJsonNode() throws JsonProcessingException {
        // Create a JsonNode from JSON string
        JsonNode jsonNode = converter.toJsonNode("{\"name\":\"Frank\",\"age\":22}");

        // Convert JsonNode to Person object
        Person person = converter.fromJsonNode(jsonNode, Person.class);

        // Verify the result
        assertEquals("Frank", person.name);
        assertEquals(22, person.age);
    }

    // Inner class for testing purposes
    static class Person {
        public String name;
        public int age;

        // Constructor for testing
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}