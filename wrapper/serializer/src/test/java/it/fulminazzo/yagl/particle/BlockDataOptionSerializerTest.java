package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BlockDataOptionSerializerTest extends SerializerTestHelper<BlockDataOption> {

    protected BlockDataOptionSerializerTest() {
        super(BlockDataOption.class, new BlockDataOptionSerializer());
    }

    @Test
    void testDeserialize() throws IOException {
        final BlockDataOption actual = load("blockdata");
        assertNotNull(actual);
        assertEquals("stone", actual.getOption());
        assertEquals("stone", actual.getMaterial());
        assertEquals("", actual.getNBT());
    }

    @Test
    void testDeserializeWithNBT() throws IOException {
        final BlockDataOption actual = load("blockdata_nbt");
        assertNotNull(actual);
        assertEquals("stone", actual.getMaterial());
        assertEquals("BlockState={foo='bar'}", actual.getNBT());
    }

    @Test
    void testSerialize() throws IOException {
        final BlockDataOption particleOption = new BlockDataOption("diamond");
        String raw = readRawString(dump(particleOption, "blockdata"));
        assertEquals("diamond", raw);
    }

    @Test
    void testSerializeWithNBT() throws IOException {
        final BlockDataOption particleOption = new BlockDataOption("stone", "BlockState={foo='bar'}");
        String raw = readRawString(dump(particleOption, "blockdata_nbt"));
        assertEquals("stone[BlockState={foo='bar'}]", raw);
    }

}
