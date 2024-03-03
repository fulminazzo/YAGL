package it.angrybear.items;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemImplTest {

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
        assertDoesNotThrow(new ItemImpl()
                .setMaterial("material")
                .setAmount(2)
                .addLore("lore")
                .addEnchantments("enchant1", "enchant2")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)::toString);
    }
}