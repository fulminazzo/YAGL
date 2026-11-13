package it.fulminazzo.yagl.serializer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObjectSerializerTest extends SerializerTestHelper<ObjectSerializerTest.Person> {

    private ObjectSerializerTest() {
        super(Person.class, new PersonSerializer());
    }

    @Test
    void testDeserialize() throws IOException {
        final Person person = new Person("Alex", 24);
        final Person deserialized = load("person");
        assertNotNull(deserialized);
        assertEquals(person.getName(), deserialized.getName());
        assertEquals(person.getAge(), deserialized.getAge());
    }

    @Test
    void testNullDeserialize() throws IOException {
        final Person deserialized = load("null_person");
        assertNull(deserialized);
    }

    @Test
    void testSerialize() throws IOException {
        final Person person = new Person("Alex", 24);

        Map<?, ?> raw = readRaw(dump(person, "person"));
        assertEquals(person.getName(), raw.get("name"));
        assertEquals(person.getAge(), raw.get("age"));
    }

    static final class PersonSerializer extends ObjectSerializer<Person> {

        @Override
        protected @NotNull Person initialize(final @NotNull DataNode node) throws SerializationException {
            return new Person(node.getOrThrow("name", String.class));
        }

        @Override
        protected void loadData(final @NotNull ObjectSerializerTest.Person value,
                                final @NotNull DataNode node) throws SerializationException {
            value.setAge(node.getOrThrow("age", Integer.class));
        }

        @Override
        protected void dumpData(final @NotNull ObjectSerializerTest.Person value,
                                final @NotNull Map<String, Object> output) {
            output.put("name", value.getName());
            output.put("age", value.getAge());
        }

    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @RequiredArgsConstructor
    @AllArgsConstructor
    static class Person {
        final @NotNull String name;
        int age;

    }

}