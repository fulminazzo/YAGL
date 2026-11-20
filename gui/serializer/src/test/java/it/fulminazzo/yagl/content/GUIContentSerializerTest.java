package it.fulminazzo.yagl.content;

import it.fulminazzo.yagl.action.command.GUIItemCommand;
import it.fulminazzo.yagl.content.requirement.PermissionRequirement;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.item.ItemSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import it.fulminazzo.yagl.wrapper.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GUIContentSerializerTest extends SerializerTestHelper<GUIContent> {
    private static final @NotNull ItemGUIContent EXPECTED_CONTENT = ItemGUIContent.newInstance("diamond")
            .setAmount(32)
            .setDurability(200)
            .setDisplayName("Mana")
            .addLore("Use this item", "to cast your", "magic spells!")
            .addEnchantment("unbreaking", 1)
            .addItemFlags("hide_enchants")
            .setUnbreakable(true)
            .setCustomModelData(15)
            .setPriority(10)
            .setClickSound(new Sound("block.note_block.harp", 2.0f, 1.5f, "master"))
            .setViewRequirements(new PermissionRequirement("yagl.test"))
            .onClickItem(new GUIItemCommand("say hello"))
            .setVariable("key1", "value1")
            .setVariable("key2", "value2");

    protected GUIContentSerializerTest() {
        super(GUIContent.class, new GUIContentSerializer());
    }

    @Test
    void testThatYaglSerializerGetSerializerGetsCorrectSerializer() throws SerializationException {
        Serializer<?> serializer = YaglSerializer.getInstance().getSerializer(ItemGUIContent.class);
        assertNotNull(serializer);
        assertInstanceOf(CustomItemGUIContentSerializer.class, serializer);
    }

    @Test
    void testDeserialize() throws IOException {
        GUIContent content = load("item_gui_content");
        assertNotNull(content);
        assertEquals(EXPECTED_CONTENT, content);
    }

    @Test
    void testSerialize() throws IOException {
        @Nullable Map<?, ?> map = readRawMap(dump(EXPECTED_CONTENT, "item_gui_content"));
        assertNotNull(map);
        Item item = EXPECTED_CONTENT.to(Item.class);
        assertEquals(item.getMaterial(), map.get(ItemSerializer.MATERIAL_PATH));
        assertEquals(item.getAmount(), map.get(ItemSerializer.AMOUNT_PATH));
        assertEquals(item.getDurability(), map.get(ItemSerializer.DURABILITY_PATH));
        assertEquals(item.getDisplayName(), map.get(ItemSerializer.DISPLAY_NAME_PATH));
        assertEquals(item.getLore(), map.get(ItemSerializer.LORE_PATH));
        Object enchantments = map.get(ItemSerializer.ENCHANTMENTS_PATH);
        assertInstanceOf(List.class, enchantments);
        int expectedEnchantmentCount = item.getEnchantments().size();
        assertEquals(
                expectedEnchantmentCount,
                ((List<?>) enchantments).size(),
                "Invalid enchantment count, expected " + expectedEnchantmentCount + " but got: " + enchantments
        );
        assertEquals(new ArrayList<>(item.getItemFlags()), map.get(ItemSerializer.ITEM_FLAGS_PATH));
        assertEquals(item.isUnbreakable(), map.get(ItemSerializer.UNBREAKABLE_PATH));
        assertEquals(item.getCustomModelData(), map.get(ItemSerializer.CUSTOM_MODEL_DATA_PATH));
        assertEquals(EXPECTED_CONTENT.getPriority(), map.get(GUIContentSerializer.PRIORITY_PATH));
        assertNotNull(map.get(GUIContentSerializer.CLICK_SOUND_PATH));
        assertNotNull(map.get(GUIContentSerializer.REQUIREMENTS_PATH));
        assertNotNull(map.get(GUIContentSerializer.CLICK_ACTION_PATH));
        assertEquals(EXPECTED_CONTENT.variables(), map.get(GUIContentSerializer.VARIABLES_PATH));
    }

}
