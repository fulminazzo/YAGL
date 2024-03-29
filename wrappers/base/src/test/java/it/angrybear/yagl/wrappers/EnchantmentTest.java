package it.angrybear.yagl.wrappers;

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
    void testIsSimilar() {
        assertTrue(new Enchantment("unbreaking", 3).isSimilar(new Enchantment("unbreaking", 1)));
    }
}