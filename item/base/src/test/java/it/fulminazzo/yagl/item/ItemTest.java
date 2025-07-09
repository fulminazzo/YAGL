package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.item.field.ItemField;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yagl.structure.EnchantmentSet;
import it.fulminazzo.yagl.wrapper.Enchantment;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yagl.wrapper.PotionEffect;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

    private static Object[] getAbstractClasses() {
        return new Object[]{MockItemInterface.class, MockItemAbstract.class};
    }

    @ParameterizedTest
    @MethodSource("getAbstractClasses")
    void testCopyItemFromAbstractWithNoImpl(Class<? extends Item> clazz) {
        final String className = clazz.getCanonicalName();
        final String implName = className + "Impl";

        Throwable exception = assertThrowsExactly(IllegalArgumentException.class, () -> Item.newItem("stone").copy(clazz));

        String message = exception.getMessage();
        assertNotNull(message, "Exception message was null");
        assertTrue(message.contains(implName), "Exception message did not contain abstract implementation class name");

        // Check that the returned exception is not from ReflectionUtils
        String reflMessage = ReflectionUtils.getOrThrow(ReflectionUtils.getField(ReflectionUtils.class, "CLASS_NOT_FOUND"), ReflectionUtils.class);
        assertNotEquals(reflMessage.replace("%class%", implName), message,
                "Exception message should not be the same as the one returned by ReflectionUtils");
    }

    @Test
    void testRemoveLore() {
        assertEquals(1, Item.newItem().setLore("Hello", "world").removeLore("Hello").getLore().size(),
                "After removal there should be only one lore element");
    }

    @Test
    void testIsNotSimilar() {
        Item i1 = Item.newItem().setAmount(1).setMaterial("grass").setDisplayName("Hello world");
        Item i2 = Item.newItem().setAmount(1).setMaterial("glass").setDisplayName("Hello world");
        assertFalse(i1.isSimilar(i2));
    }

    @Test
    void testIsSimilarNull() {
        assertFalse(Item.newItem("stone").setAmount(1).isSimilar(null));
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
        private final Set<PotionEffect> potionEffects = new LinkedHashSet<>();
        private boolean unbreakable;
        private int customModelData;

        public MockItem(String material) {
            this.material = material;
        }

        @Override
        public @NotNull Item setMaterial(@NotNull String material) {
            this.material = material;
            return this;
        }

        @Override
        public @NotNull Item setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        @Override
        public @NotNull Item setDurability(int durability) {
            this.durability = durability;
            return this;
        }

        @Override
        public @NotNull Item setDisplayName(@NotNull String displayName) {
            this.displayName = displayName;
            return this;
        }

        @Override
        public @NotNull Item setLore(@NotNull Collection<String> lore) {
            this.lore.clear();
            this.lore.addAll(lore);
            return this;
        }

        @Override
        public @NotNull Item setUnbreakable(boolean unbreakable) {
            this.unbreakable = unbreakable;
            return this;
        }

        @Override
        public @NotNull Item setCustomModelData(int customModelData) {
            return null;
        }

        @Override
        public boolean isSimilar(Item item, ItemField @NotNull ... ignore) {
            return false;
        }

    }

}
