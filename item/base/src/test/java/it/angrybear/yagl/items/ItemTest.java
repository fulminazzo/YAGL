package it.angrybear.yagl.items;

import it.angrybear.yagl.items.fields.ItemField;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.angrybear.yagl.structures.EnchantmentSet;
import it.angrybear.yagl.wrappers.Enchantment;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void invalidConstructorItemClassShouldReturnItemImpl() {
        Item item = new MockItem("STONE").copy();
        assertInstanceOf(ItemImpl.class, item);
    }

    @Test
    void testAddEnchantment() {
        Item item = new MockItem("STONE");
        Enchantment e1 = new Enchantment("mock-e", 1);
        item.addEnchantments(e1);

        Enchantment e2 = new Enchantment("mock-e", 2);
        item.addEnchantments(e2);
        assertEquals(1, item.getEnchantments().size(), "There should be only 1 enchantment");
        assertEquals(2, item.getEnchantmentLevel("mock-e"));

        Enchantment e3 = new Enchantment("mock-e", 4);
        item.addEnchantments(e3);
        assertEquals(1, item.getEnchantments().size(), "There should be only 1 enchantment");
        assertEquals(4, item.getEnchantmentLevel("mock-e"));
    }

    @Test
    void testGetEnchantmentLevel() {
        Item item = new MockItem("STONE");
        String[] enchantments = new String[]{"ench1", "ench2", "ench3"};
        item.addEnchantments(enchantments);
        for (String e : enchantments) {
            assertEquals(1, item.getEnchantmentLevel(e));
            assertEquals(1, item.getEnchantmentLevel(new Enchantment(e)));
        }
    }

    @Test
    void testHasEnchantment() {
        Item item = new MockItem("STONE");
        String[] enchantments = new String[]{"ench1", "ench2", "ench3"};
        item.addEnchantments(enchantments);
        for (String e : enchantments) {
            assertTrue(item.hasEnchantment(e));
            assertTrue(item.hasEnchantment(new Enchantment(e)));
        }
    }

    @Test
    void testItemFlagMethods() {
        Item item = new MockItem("STONE");
        final ItemFlag flag = ItemFlag.HIDE_ATTRIBUTES;
        assertFalse(item.hasItemFlag(flag), "Item should not have flag at start");
        item.addItemFlags(flag);
        assertTrue(item.hasItemFlag(flag), "Item should have flag after add");
        item.removeItemFlags(flag);
        assertFalse(item.hasItemFlag(flag), "Item should not have flag after remove");
    }

    @Getter
    public static class MockItem implements Item {
        private String material;
        private int amount;
        private int durability;
        private String displayName = "";
        private final List<String> lore = new LinkedList<>();
        private final Set<Enchantment> enchantments = new EnchantmentSet();
        private final Set<ItemFlag> itemFlags = new LinkedHashSet<>();
        private boolean unbreakable;
        private int customModelData;

        public MockItem(String material) {
            this.material = material;
        }

        @Override
        public Item setMaterial(@NotNull String material) {
            this.material = material;
            return this;
        }

        @Override
        public Item setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        @Override
        public Item setDurability(int durability) {
            this.durability = durability;
            return this;
        }

        @Override
        public Item setDisplayName(@NotNull String displayName) {
            this.displayName = displayName;
            return this;
        }

        @Override
        public Item setLore(@NotNull Collection<String> lore) {
            this.lore.clear();
            this.lore.addAll(lore);
            return this;
        }

        @Override
        public Item setUnbreakable(boolean unbreakable) {
            this.unbreakable = unbreakable;
            return this;
        }

        @Override
        public Item setCustomModelData(int customModelData) {
            return null;
        }

        @Override
        public boolean isSimilar(Item item, ItemField @NotNull ... ignore) {
            return false;
        }
    }
}