package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MaterialDataOptionSerializerTest extends SerializerTestHelper<MaterialDataOption> {

    protected MaterialDataOptionSerializerTest() {
        super(MaterialDataOption.class, new MaterialDataOptionSerializer());
    }

    @Test
    void testDeserialize() throws IOException {
        final MaterialDataOption actual = load("materialdata");
        assertNotNull(actual);
        assertEquals("stone", actual.getOption().getFirst());
        assertEquals(0, actual.getOption().getSecond());
    }

    @Test
    void testDeserializeWithData() throws IOException {
        final MaterialDataOption actual = load("materialdata_data");
        assertNotNull(actual);
        assertEquals("stone", actual.getOption().getFirst());
        assertEquals(1, actual.getOption().getSecond());
    }

    @Test
    void testSerialize() throws IOException {
        final MaterialDataOption particleOption = new MaterialDataOption("diamond", 0);
        String raw = readRawString(dump(particleOption, "materialdata"));
        assertEquals("diamond[0]", raw);
    }

    @Test
    void testSerializeWithData() throws IOException {
        final MaterialDataOption particleOption = new MaterialDataOption("stone", 1);
        String raw = readRawString(dump(particleOption, "materialdata_data"));
        assertEquals("stone[1]", raw);
    }

}
