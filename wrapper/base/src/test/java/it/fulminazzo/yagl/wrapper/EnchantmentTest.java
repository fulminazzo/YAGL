package it.fulminazzo.yagl.wrapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnchantmentTest {

    @Test
    void testCompareLevelsPositive() {
        assertFalse(new Enchantment("unbreaking", 3).compareLevels(new Enchantment("unbreaking", 1)));
    }

    @Test
    void testCompareLevelsNegative() {
        assertTrue(new Enchantment("unbreaking", 1).compareLevels(new Enchantment("unbreaking", 3)));
    }

    @Test
    void testCompareLevelsNull() {
        assertFalse(new Enchantment("unbreaking", 1).compareLevels(null));
    }

    @Test
    void testCompareLevelsDifferentName() {
        assertFalse(new Enchantment("unbreaking", 1).compareLevels(new Enchantment("unbreaking_", 1)));
    }

    @Test
    void testIsSimilar() {
        assertTrue(new Enchantment("unbreaking", 3).isSimilar(new Enchantment("unbreaking", 1)));
    }

}
