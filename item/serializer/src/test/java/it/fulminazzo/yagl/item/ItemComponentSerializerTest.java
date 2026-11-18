package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.ItemSerializersLoader;
import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import it.fulminazzo.yagl.wrapper.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ItemComponentSerializerTest extends SerializerTestHelper<ItemComponent> {
    private static final @NotNull String TYPE_NAME = "ENCHANTMENT";
    private static final @NotNull EnchantmentItemComponent COMPONENT = new EnchantmentItemComponent(
            new Enchantment("sharpness", 5),
            new Enchantment("unbreaking", 3),
            new Enchantment("mending", 1)
    );

    protected ItemComponentSerializerTest() {
        super(ItemComponent.class, ItemSerializersLoader.getItemComponentSerializer());
        // Register custom serializer
        ItemSerializersLoader.getItemComponentSerializer().registerSerializer(
                TYPE_NAME,
                EnchantmentItemComponent.class,
                new EnchantmentItemComponentSerializer()
        );
    }

    @Test
    void testDeserialize() throws IOException {
        ItemComponent component = load("component");
        assertNotNull(component);
        assertInstanceOf(COMPONENT.getClass(), component);
        assertEquals(COMPONENT.getEnchantments(), ((EnchantmentItemComponent) component).getEnchantments());
    }

    @Test
    void testSerialize() throws IOException {
        Map<?, ?> rawData = readRawMap(dump(COMPONENT, "component"));
        assertNotNull(rawData);
        assertEquals(TYPE_NAME, rawData.get("type"));
        Object rawContents = rawData.get("enchants");
        assertNotNull(rawContents);
        assertInstanceOf(List.class, rawContents);
        int expectedSize = COMPONENT.getEnchantments().size();
        assertEquals(
                expectedSize,
                ((List<?>) rawContents).size(),
                "Contents should have contained " + expectedSize + " objects but was: " + rawContents
        );
    }

}