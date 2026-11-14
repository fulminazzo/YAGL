package it.fulminazzo.yagl;

import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ColorSerializerTest extends SerializerTestHelper<Color> {

    protected ColorSerializerTest() {
        super(Color.class, new ColorSerializer());
    }

    @Test
    void testDeserializeNull() throws IOException {
        Color color = load("null_color");
        assertNull(color);
    }

    @Test
    void testDeserializeKnownColor() throws IOException {
        Color color = load("known_color");
        assertNotNull(color);
        assertEquals(Color.BLUE, color);
    }

    @Test
    void testDeserializeCustomColor() throws IOException {
        Color color = load("custom_color");
        assertNotNull(color);
        assertEquals(200, color.getRed());
        assertEquals(100, color.getGreen());
        assertEquals(50, color.getBlue());
        assertEquals(255, color.getAlpha());
    }

    @Test
    void testSerializeNull() throws IOException {
        String raw = readRawString(dump(null, "null_color"));
        assertNull(raw);
    }

    @Test
    void testSerializeKnownColor() throws IOException {
        Color expected = Color.RED;

        String raw = readRawString(dump(expected, "known_color"));
        assertNotNull(raw);
        Color actual = Color.valueOf(raw);
        assertEquals(expected, actual);
        assertSame(expected, actual);
    }

    @Test
    void testSerializeCustomColor() throws IOException {
        Color color = new Color(128, 64, 32);

        String raw = readRawString(dump(color, "custom_color"));
        assertNotNull(raw);
        assertEquals("#FF804020", raw);
    }

}
