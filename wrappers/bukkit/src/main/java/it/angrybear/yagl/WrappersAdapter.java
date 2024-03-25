package it.angrybear.yagl;

import it.angrybear.yagl.utils.EnumUtils;
import it.angrybear.yagl.wrappers.Enchantment;
import it.angrybear.yagl.wrappers.PotionEffect;
import it.fulminazzo.fulmicollection.structures.Tuple;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class to convert objects from this library to Minecraft Bukkit and vice versa.
 */
public class WrappersAdapter {

    /**
     * Converts the given wrapper {@link PotionEffect} to a {@link org.bukkit.potion.PotionEffect}.
     *
     * @param potionEffect the potion effect
     * @return the potion effect
     */
    public static org.bukkit.potion.PotionEffect wPotionEffectToPotionEffect(final @NotNull PotionEffect potionEffect) {
        final String effect = potionEffect.getEffect();
        final PotionEffectType type = EnumUtils.valueOf(PotionEffectType.class, effect, "getByName");
        try {
            return new org.bukkit.potion.PotionEffect(type, potionEffect.getDurationInTicks(), potionEffect.getAmplifier(),
                    potionEffect.isShowingParticles(), potionEffect.isShowingParticles(),
                    potionEffect.isShowingIcon());
        } catch (NoSuchMethodError e) {
            return new org.bukkit.potion.PotionEffect(type, potionEffect.getDurationInTicks(), potionEffect.getAmplifier(),
                    potionEffect.isShowingParticles(), potionEffect.isShowingParticles());
        }
    }

    /**
     * Converts the given potion effect to a {@link PotionEffect}.
     * On older versions, {@link PotionEffect#isShowingIcon()} will be ignored.
     *
     * @param potionEffect the potion effect
     * @return the potion effect
     */
    public static PotionEffect potionEffectToWPotionEffect(final @NotNull org.bukkit.potion.PotionEffect potionEffect) {
        try {
            return new PotionEffect(potionEffect.getType().getName(), (double) potionEffect.getDuration() / 20,
                    potionEffect.getAmplifier() + 1, potionEffect.hasParticles(), potionEffect.hasIcon());
        } catch (NoSuchMethodError e) {
            return new PotionEffect(potionEffect.getType().getName(), (double) potionEffect.getDuration() / 20,
                    potionEffect.getAmplifier() + 1, potionEffect.hasParticles());
        }
    }

    /**
     * Converts the given wrapper {@link Enchantment} to a {@link Tuple} containing its corresponding one and its level.
     *
     * @param enchantment the enchantment
     * @return the tuple
     */
    public static Tuple<org.bukkit.enchantments.Enchantment, Integer> wEnchantToEnchant(final @NotNull Enchantment enchantment) {
        String raw = enchantment.getEnchantment();
        org.bukkit.enchantments.Enchantment actual;
        try {
            Class<?> clazz = Class.forName("org.bukkit.NamespacedKey");
            Object key = new Refl<>(clazz).invokeMethod("minecraft", raw);
            actual = org.bukkit.enchantments.Enchantment.getByKey(key);
        } catch (Exception e) {
            // Prevent other versions from complaining about method not found.
            actual = EnumUtils.valueOf(org.bukkit.enchantments.Enchantment.class, raw, "getByName");
        }
        if (actual == null) throw new IllegalArgumentException(String.format("Could not find enchantment '%s'", raw));
        return new Tuple<>(actual, enchantment.getLevel());
    }

    /**
     * Converts the given Bukkit enchantment to a {@link Enchantment}.
     * Uses {@link org.bukkit.enchantments.Enchantment#getName()} for retro-compatibility purposes.
     *
     * @param enchantment the enchantment
     * @return the enchantment
     */
    public static Enchantment enchantToWEnchant(org.bukkit.enchantments.Enchantment enchantment) {
        return enchantToWEnchant(enchantment, 1);
    }

    /**
     * Converts the given Bukkit enchantment to a {@link Enchantment}.
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
