package it.fulminazzo.yagl.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YaglModuleTest {
    private final @NotNull Person person = new Person("Alex", 24);
    private final @NotNull Map<?, ?> data = Map.of(
            PersonSerializer.NAME_PATH, person.getName(),
            PersonSerializer.AGE_PATH, person.getAge()
    );

    private final @NotNull ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new YaglModule());
    }

    @Test
    void testDeserialize() throws JsonProcessingException {
        final String serializedPerson = objectMapper.writeValueAsString(data);
        final Person deserializedPerson = objectMapper.readValue(serializedPerson, Person.class);
        assertNotNull(deserializedPerson);
        assertEquals(person.getName(), deserializedPerson.getName());
        assertEquals(person.getAge(), deserializedPerson.getAge());
    }

    @Test
    void testSerializationExceptionOnDeserializeIsRethrownAsIOException() throws JsonProcessingException {
        final String serializedPerson = objectMapper.writeValueAsString(Map.of(PersonSerializer.NAME_PATH, person.getName()));
        Exception exception = assertThrowsExactly(JsonMappingException.class, () -> objectMapper.readValue(serializedPerson, Person.class));
        String message = exception.getMessage();
        assertTrue(
                message.contains(SerializationException.class.getName()),
                "Exception message should contain serialization exception class name, but was: " + message
        );
    }

    @Test
    void testSerialize() throws JsonProcessingException {
        final String serializedPerson = objectMapper.writeValueAsString(person);
        assertNotNull(serializedPerson);
        final Map<?, ?> data = objectMapper.readValue(serializedPerson, Map.class);
        assertNotNull(data);
        assertEquals(person.getName(), data.get(PersonSerializer.NAME_PATH));
        assertEquals(person.getAge(), data.get(PersonSerializer.AGE_PATH));
    }

    @Test
    void testSerializationExceptionOnSerializeIsRethrownAsIOException() {
        final Person underagePerson = new Person("John", 17);
        Exception exception = assertThrowsExactly(JsonMappingException.class, () -> objectMapper.writeValueAsString(underagePerson));
        assertTrue(
                exception.getMessage().contains(SerializationException.class.getName()),
                "Exception message should contain serialization exception class name, but was: " + exception.getMessage()
        );
    }

}