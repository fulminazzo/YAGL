package it.angrybear.items;

import it.angrybear.items.fields.Enchantment;
import it.angrybear.items.fields.ItemField;
import it.angrybear.items.fields.ItemFlag;
import it.angrybear.structures.EnchantmentSet;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testAddEnchantment() {
        Item item = new MockItem();
        Enchantment e1 = new Enchantment("mock-e", 1);
        item.addEnchantments(e1);

        Enchantment e2 = new Enchantment("mock-e", 0);
        item.addEnchantments(e2);
        assertEquals(1, item.getEnchantments().size(), "There should be only 1 enchantment");
        assertEquals(1, item.getEnchantmentLevel("mock-e"));

        Enchantment e3 = new Enchantment("mock-e", 4);
        item.addEnchantments(e3);
        assertEquals(1, item.getEnchantments().size(), "There should be only 1 enchantment");
        assertEquals(4, item.getEnchantmentLevel("mock-e"));
    }

    @Test
    void testGetEnchantmentLevel() {
        Item item = new MockItem();
        String[] enchantments = new String[]{"ench1", "ench2", "ench3"};
        item.addEnchantments(enchantments);
        for (String e : enchantments)
            assertEquals(0, item.getEnchantmentLevel(e));
    }

    @Getter
    public static class MockItem implements Item {
        private String material;
        private int amount;
        private int durability;
        private String displayName = "";
        private List<String> lore = new LinkedList<>();
        private Set<Enchantment> enchantments = new EnchantmentSet();
        private Set<ItemFlag> itemFlags = new LinkedHashSet<>();
        private boolean unbreakable;
        private int customModelData;

        @Override
        public Item setMaterial(String material) {
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
        public Item setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        @Override
        public Item setLore(Collection<String> lore) {
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
        public boolean isSimilar(Item item, ItemField... ignore) {
            return false;
        }

        @Override
        public <I extends Item> I copy(Class<I> clazz) {
            return null;
        }
    }
}