package it.fulminazzo.yagl;

import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SerializableFunctionTest {

    @Test
    void testThatSerializationWorks() {
        Person person = new Person("John", 30);
        String serialized = person.serialize();
        assertNotNull(serialized);

        byte[] bytes = Base64.getDecoder().decode(serialized);
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
             ObjectInputStream inputStream = new ObjectInputStream(byteStream)) {
            Person deserialized = (Person) inputStream.readObject();
            assertEquals(person, deserialized);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Value
    static class Person implements SerializableFunction, Serializable {
        @NotNull String name;
        int age;

    }

}