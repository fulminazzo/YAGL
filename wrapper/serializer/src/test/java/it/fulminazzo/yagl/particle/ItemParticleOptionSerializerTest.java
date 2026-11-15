package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemParticleOptionSerializerTest extends SerializerTestHelper<ItemParticleOption<Item>> {

    @SuppressWarnings("unchecked")
    protected ItemParticleOptionSerializerTest() {
        super((Class<ItemParticleOption<Item>>) (Class<?>) ItemParticleOption.class, new SingleParticleOptionSerializer<>(
                (Class<ItemParticleOption<Item>>) (Class<?>) ItemParticleOption.class,
                ItemParticleOption::new
        ));
    }

    @Test
    void testDeserialize() throws IOException {
        final ItemParticleOption<Item> actual = load("item");
        assertNotNull(actual);
        assertEquals(new Item("stone"), actual.getOption());
    }

    @Test
    void testSerialize() throws IOException {
        final ItemParticleOption<Item> particleOption = new ItemParticleOption<>(new Item("diamond"));
        String raw = readRawString(dump(particleOption, "item"));
        assertEquals("diamond", raw);
    }

}
