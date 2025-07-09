package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.item.field.ItemField;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.fulmicollection.objects.Printable;
import it.fulminazzo.yagl.wrapper.PotionEffect;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ItemImplTest {

    @Test
    void testCopy() {
        Item i1 = new ItemImpl();
        Item i2 = i1.copy();
        assertEquals(i1, i2);
    }

    @Test
    void testCopyItem() {
        Item i1 = new ItemImpl();
        Item i2 = i1.copy(Item.class);
        assertEquals(i1, i2);
    }

    @Test
    void testSimilarity() {
        Item i1 = new ItemImpl()
                .setMaterial("material")
                .setAmount(2)
                .addLore("lore")
                .addEnchantments("enchant1", "enchant2")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        Item i2 = new ItemImpl()
                .setMaterial("material")
                .setAmount(4)
                .addLore("lore")
                .addEnchantments("enchant1", "enchant2")
                .addItemFlags(ItemFlag.HIDE_DESTROYS);
        assertTrue(i1.isSimilar(i2, ItemField.AMOUNT, ItemField.ITEM_FLAGS));
    }

    @Test
    void testPrint() {
        Item item = new ItemImpl()
                .setMaterial("material")
                .setAmount(2)
                .setDurability(4)
                .setDisplayName("Hello world")
                .addLore("lore")
                .addEnchantments("enchant1", "enchant2")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .addPotionEffects(new PotionEffect("strength", 20, 2))
                .setCustomModelData(2)
                .setUnbreakable(true);
        String output = item.toString();
        String expected = Printable.convertToJson(item);
        assertEquals(expected, output);
    }

    @Test
    void testStrippedPrint() {
        String output = new ItemImpl()
                .setMaterial("stone")
                .setAmount(2).toString();
        for (String s : Arrays.asList("durability", "displayName", "lore", "enchantments", "itemFlags", "customModelData"))
            assertFalse(output.contains(s), String.format("'%s' should not contain %s", output, s));
    }

}
