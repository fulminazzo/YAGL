package it.angrybear.yagl;

import it.angrybear.yagl.wrappers.Enchantment;
import it.fulminazzo.fulmicollection.structures.Tuple;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class to convert objects from this library to Minecraft Bukkit and vice versa.
 */
public class WrappersAdapter {

    /**
     * Converts the given wrapper {@link Enchantment} to a {@link Tuple} containing its corresponding one and its level.
     *
     * @param enchantment the enchantment
     * @return the tuple
     */
    public static Tuple<org.bukkit.enchantments.Enchantment, Integer> wEnchantToEnchant(final @NotNull Enchantment enchantment) {
        String raw = enchantment.getEnchantment();
        org.bukkit.enchantments.Enchantment actual = org.bukkit.enchantments.Enchantment.getByName(raw);
        if (actual == null)
            try {
                // Prevent other versions from complaining about method not found.
                actual = org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft(raw));
            } catch (Exception ignored) {}
        if (actual == null) throw new IllegalArgumentException(String.format("Could not find enchantment '%s'", raw));
        return new Tuple<>(actual, enchantment.getLevel());
    }

    /**
     * Converts the given bukkit enchantment to a {@link Enchantment}.
     * Uses {@link org.bukkit.enchantments.Enchantment#getName()} for retro-compatibility purposes.
     *
     * @param enchantment the enchantment
     * @return the enchantment
     */
    public static Enchantment enchantToWEnchant(org.bukkit.enchantments.Enchantment enchantment) {
        return enchantToWEnchant(enchantment, 1);
    }

    /**
     * Converts the given bukkit enchantment to a {@link Enchantment}.
     * Uses {@link org.bukkit.enchantments.Enchantment#getName()} for retro-compatibility purposes.
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @return the enchantment
     */
    public static Enchantment enchantToWEnchant(org.bukkit.enchantments.Enchantment enchantment, int level) {
        return new Enchantment(enchantment.getName(), level);
    }
}
