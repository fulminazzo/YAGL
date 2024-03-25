package it.angrybear.yagl;

import it.angrybear.yagl.wrappers.Enchantment;
import it.fulminazzo.fulmicollection.objects.Refl;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WrappersAdapterTest {

    private static org.bukkit.enchantments.Enchantment[] getEnchantments() {
        List<org.bukkit.enchantments.Enchantment> enchantments = new ArrayList<>();
        for (Field field : org.bukkit.enchantments.Enchantment.class.getDeclaredFields())
            if (field.getType().equals(org.bukkit.enchantments.Enchantment.class))
                try {
                    org.bukkit.enchantments.Enchantment enchant = (org.bukkit.enchantments.Enchantment) field.get(org.bukkit.enchantments.Enchantment.class);
                    enchantments.add(new MockEnchantment(enchant.getKey()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
        // Register enchantments
        Map<NamespacedKey, org.bukkit.enchantments.Enchantment> byKey = new Refl<>(org.bukkit.enchantments.Enchantment.class)
                .getFieldObject("byKey");
        if (byKey != null) enchantments.forEach(e -> byKey.put(e.getKey(), e));
        return enchantments.toArray(new org.bukkit.enchantments.Enchantment[0]);
    }

    @ParameterizedTest
    @MethodSource("getEnchantments")
    void testEnchantmentsConversion(org.bukkit.enchantments.Enchantment expected) {
        Enchantment enchantment = WrappersAdapter.enchantToWEnchant(expected);
        assertEquals(expected, WrappersAdapter.wEnchantToEnchant(enchantment).getKey());
    }

    private static class MockEnchantment extends org.bukkit.enchantments.Enchantment {

        public MockEnchantment(@NotNull NamespacedKey key) {
            super(key);
        }

        @NotNull
        @Override
        public String getName() {
            return getKey().getKey();
        }

        @Override
        public int getMaxLevel() {
            return 0;
        }

        @Override
        public int getStartLevel() {
            return 0;
        }

        @NotNull
        @Override
        public EnchantmentTarget getItemTarget() {
            return null;
        }

        @Override
        public boolean isTreasure() {
            return false;
        }

        @Override
        public boolean isCursed() {
            return false;
        }

        @Override
        public boolean conflictsWith(@NotNull org.bukkit.enchantments.Enchantment other) {
            return false;
        }

        @Override
        public boolean canEnchantItem(@NotNull ItemStack item) {
            return false;
        }
    }
}