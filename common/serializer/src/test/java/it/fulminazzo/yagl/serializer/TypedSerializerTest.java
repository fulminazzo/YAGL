package it.fulminazzo.yagl.serializer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TypedSerializerTest extends SerializerTestHelper<Cat> {

    protected TypedSerializerTest() {
        super(Cat.class, new CatSerializer());
    }

    @Test
    void testDeserializePersian() throws IOException {
        Cat cat = load("persian");
        assertInstanceOf(PersianCat.class, cat);
        PersianCat persianCat = (PersianCat) cat;
        assertEquals("Garfield", persianCat.getName());
        assertEquals(15, persianCat.getAge());
        assertEquals("orange", persianCat.getColor());
    }

    @Test
    void testDeserializeSiamese() throws IOException {
        Cat cat = load("siamese");
        assertInstanceOf(SiameseCat.class, cat);
        SiameseCat siameseCat = (SiameseCat) cat;
        assertEquals("White", siameseCat.getName());
        assertEquals(5, siameseCat.getAge());
        assertEquals("Alex", siameseCat.getMaster());
        assertEquals("Italy", siameseCat.getRegion());
    }

    @Test
    void testSerializePersian() throws IOException {
        PersianCat persianCat = new PersianCat("Garfield");
        persianCat.setAge(15);
        persianCat.setColor("orange");

        Map<?, ?> raw = readRawMap(dump(persianCat, "persian"));
        assertEquals(persianCat.getName(), raw.get("name"));
        assertEquals(persianCat.getAge(), raw.get("age"));
        assertEquals(persianCat.getColor(), raw.get("color"));
        assertEquals("PERSIAN", raw.get("type"));
    }

    @Test
    void testSerializeSiamese() throws IOException {
        SiameseCat siameseCat = new SiameseCat("White", "Alex");
        siameseCat.setAge(5);
        siameseCat.setRegion("Italy");

        Map<?, ?> raw = readRawMap(dump(siameseCat, "siamese"));
        assertEquals(siameseCat.getName(), raw.get("name"));
        assertEquals(siameseCat.getAge(), raw.get("age"));
        assertEquals(siameseCat.getMaster(), raw.get("master"));
        assertEquals(siameseCat.getRegion(), raw.get("region"));
        assertEquals("SIAMESE", raw.get("type"));
    }

    @Test
    void testDeserializeThrowsIfInvalidTypeIsSpecified() {
        IOException exception = assertThrows(IOException.class, () -> load("invalid-type"));
        Throwable cause = exception.getCause();
        assertInstanceOf(SerializationException.class, cause);
        String message = cause.getMessage();
        assertTrue(message.contains("BRITISH"), "Invalid message: " + message);
    }

    @Test
    void testDeserializeThrowsIfNoTypeIsSpecified() {
        IOException exception = assertThrows(IOException.class, () -> load("missing-type"));
        Throwable cause = exception.getCause();
        assertInstanceOf(SerializationException.class, cause);
    }

}