package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SerializerTestHelperTest extends SerializerTestHelper<Person> {
    private final @NotNull Person person;

    private SerializerTestHelperTest() {
        super(Person.class, new PersonSerializer());
        this.person = new Person("Alex", 24);
    }

    @Test
    void testThatLoadCorrectlyLoadsData() throws IOException {
        Person other = load("person");
        assertNotNull(other, "Expected non-null data");
        assertEquals(person.getName(), other.getName(), "Expected 'name' value to match");
        assertEquals(person.getAge(), other.getAge(), "Expected 'age' value to match");
        assertEquals(person, other);
    }

    @Test
    void testThatDumpCorrectlySavesData() throws IOException {
        File file = dump(person, "person");
        assertTrue(file.exists(), "File does not exist: " + file.getAbsolutePath());

        Map<?, ?> raw = readRaw(file);
        assertNotNull(raw, "Expected non-null data");
        assertTrue(raw.containsKey("name"), "Expected 'name' key in serialized data");
        assertEquals(person.getName(), raw.get("name"), "Expected 'name' value to match");
        assertTrue(raw.containsKey("age"), "Expected 'age' key in serialized data");
        assertEquals(person.getAge(), raw.get("age"), "Expected 'age' value to match");
    }

}