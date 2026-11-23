package it.fulminazzo.yagl.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YaglTypeAdapterFactoryTest {
    private final @NotNull Person person = new Person("Alex", 24);
    private final @NotNull Map<?, ?> data = Map.of(
            PersonSerializer.NAME_PATH, person.getName(),
            PersonSerializer.AGE_PATH, person.getAge()
    );

    private @NotNull Gson gson;

    @BeforeEach
    void setup() {
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(new YaglTypeAdapterFactory())
                .create();
    }

    @Test
    void testDeserialize() {
        final String serializedPerson = gson.toJson(data);
        final Person deserializedPerson = gson.fromJson(serializedPerson, Person.class);
        assertNotNull(deserializedPerson);
        assertEquals(person.getName(), deserializedPerson.getName());
        assertEquals(person.getAge(), deserializedPerson.getAge());
    }

    @Test
    void testSerializationExceptionOnDeserializeIsRethrownAsIOException() {
        final String serializedPerson = gson.toJson(Map.of(PersonSerializer.NAME_PATH, person.getName()));
        Exception exception = assertThrowsExactly(JsonParseException.class, () -> gson.fromJson(serializedPerson, Person.class));
        String message = exception.getMessage();
        assertTrue(
                message.contains(SerializationException.class.getName()),
                "Exception message should contain serialization exception class name, but was: " + message
        );
    }

    @Test
    void testSerialize() {
        final String serializedPerson = gson.toJson(person);
        assertNotNull(serializedPerson);
        final Map<?, ?> data = gson.fromJson(serializedPerson, Map.class);
        assertNotNull(data);
        assertEquals(person.getName(), data.get(PersonSerializer.NAME_PATH));
        Object age = data.get(PersonSerializer.AGE_PATH);
        if (age instanceof Double && (Double) age == ((Double) age).intValue()) age = ((Double) age).intValue();
        assertEquals(person.getAge(), age);
    }

    @Test
    void testSerializationExceptionOnSerializeIsRethrownAsIOException() {
        final Person underagePerson = new Person("John", 17);
        Exception exception = assertThrowsExactly(JsonIOException.class, () -> gson.toJson(underagePerson));
        assertTrue(
                exception.getMessage().contains(SerializationException.class.getName()),
                "Exception message should contain serialization exception class name, but was: " + exception.getMessage()
        );
    }

}
