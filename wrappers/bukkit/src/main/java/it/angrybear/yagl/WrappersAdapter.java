package it.angrybear.yagl;

import it.angrybear.yagl.particles.Particle;
import it.angrybear.yagl.utils.EnumUtils;
import it.angrybear.yagl.wrappers.Enchantment;
import it.angrybear.yagl.wrappers.PotionEffect;
import it.angrybear.yagl.wrappers.Sound;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.Triple;
import it.fulminazzo.fulmicollection.structures.Tuple;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;

/**
 * A utility class to convert objects from this library to Minecraft Bukkit and vice versa.
 */
public class WrappersAdapter {

    public static void spawnParticle(final @NotNull Player player, final @NotNull Particle particle,
                                     double x, double y, double z, int count) {
        spawnParticle(player, particle, x, y, z, count, 0, 0, 0);
    }

    public static void spawnParticle(final @NotNull Player player, final @NotNull Particle particle,
                                     Location location, int count) {
        spawnParticle(player, particle, location, count, 0, 0, 0);
    }

    public static void spawnParticle(final @NotNull Player player, final @NotNull Particle particle,
                                     double x, double y, double z, int count,
                                     double offsetX, double offsetY, double offsetZ) {
        spawnParticle(player, particle, new Location(player.getWorld(), x, y, z), count, offsetX, offsetY, offsetZ);
    }

    public static void spawnParticle(final @NotNull Player player, final @NotNull Particle particle,
                                     Location location, int count,
                                     double offsetX, double offsetY, double offsetZ) {
        spawnParticle(new Refl<>(player), particle, location, count, offsetX, offsetY, offsetZ);
    }
    
    private static void spawnParticle(final @NotNull Refl<?> spawner, final @NotNull Particle particle,
                                     Location location, int count,
                                     double offsetX, double offsetY, double offsetZ) {
        Tuple<org.bukkit.Particle, ?> tuple = wParticleToParticle(particle);
        org.bukkit.Particle actual = tuple.getKey();
        Object option = tuple.getValue();
        if (option == null) spawner.callMethod("spawnParticle", actual, location, count, offsetX, offsetY, offsetZ);
        else spawner.callMethod("spawnParticle", actual, location, count, offsetX, offsetY, offsetZ, option);
    }

    public static Tuple<org.bukkit.Particle, ?> wParticleToParticle(Particle particle) {
        org.bukkit.Particle actual = EnumUtils.valueOf(org.bukkit.Particle.class, particle.getType());
        Object option = particle.getOption();
        if (option == null) return new Tuple<>(actual, null);
        else {
            Class<?> dataType = actual.getDataType();
            if (ReflectionUtils.isPrimitiveOrWrapper(dataType)) return new Tuple<>(actual, option);
            else try {
                final Object finalOption = convertOption(dataType, option);
                return new Tuple<>(actual, finalOption);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException(String.format("Could not find constructor for data type '%s'",
                        dataType.getSimpleName()));
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(String.format("Invalid option '%s' for '%s'",
                        option.getClass().getSimpleName(), actual.name()));
            }
        }
    }

    private static Object convertOption(Class<?> dataType, Object option) {
        final Object finalOption;
        Constructor<?> constructor = dataType.getDeclaredConstructors()[0];
        int size = constructor.getParameterCount();
        if (size == 2) {
            Tuple<?, ?> t = (Tuple<?, ?>) option;
            finalOption = new Refl<>(dataType, prepareParameters(t.getKey(), t.getValue())).getObject();
        } else if (size == 3) {
            Triple<?, ?, ?> t = (Triple<?, ?, ?>) option;
            finalOption = new Refl<>(dataType, prepareParameters(t.getFirst(), t.getSecond(), t.getThird())).getObject();
        } else throw new IllegalArgumentException("Cannot create option from constructor: " + constructor);
        return finalOption;
    }

    private static Object[] prepareParameters(final Object @NotNull ... parameters) {
        for (int i = 0; i < parameters.length; i++) {
            Object o = parameters[i];
            if (o instanceof Color) parameters[i] = wColorToColor((Color) o);
        }
        return parameters;
    }

    /**
     * Converts the given wrapper {@link Color} to a {@link org.bukkit.Color}.
     *
     * @param color the color
     * @return the color
     */
    public static org.bukkit.Color wColorToColor(final @NotNull Color color) {
        try {
            return new Refl<>(org.bukkit.Color.class).invokeMethod("fromARGB",
                    color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
        } catch (Exception e) {
            return org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
        }
    }

    /**
     * Converts the given {@link org.bukkit.Color} to a wrapper {@link Color}.
     *
     * @param color the color
     * @return the color
     */
    public static Color colorToWColor(final @NotNull org.bukkit.Color color) {
        try {
            return new Color(new Refl<>(color).invokeMethod("getAlpha"), color.getRed(), color.getGreen(), color.getBlue());
        } catch (Exception e) {
            return new Color(color.getRed(), color.getGreen(), color.getBlue());
        }
    }

    /**
     * Plays the given {@link Sound} using Bukkit methods at the player's {@link Location}.
     * In older versions, {@link Sound#getCategory()} is ignored.
     *
     * @param player the player
     * @param sound  the sound
     */
    public static void playSound(final @NotNull Player player, final @NotNull Sound sound) {
        playSound(player, player.getLocation(), sound);
    }

    /**
     * Plays the given {@link Sound} using Bukkit methods.
     * In older versions, {@link Sound#getCategory()} is ignored.
     *
     * @param player   the player
     * @param location the location
     * @param sound    the sound
     */
    public static void playSound(final @NotNull Player player, final @NotNull Location location, final @NotNull Sound sound) {
        final org.bukkit.Sound actual = EnumUtils.valueOf(org.bukkit.Sound.class, sound.getSound());
        try {
            String category = sound.getCategory();
            if (category != null) {
                final Object actualCategory = EnumUtils.valueOf(Class.forName("org.bukkit.SoundCategory"), category);
                new Refl<>(player).callMethod("playSound", location, actual, actualCategory, sound.getVolume(), sound.getPitch());
                return;
            }
        } catch (Exception e) {
            // Prevent other versions from complaining about method not found.
        }
        player.playSound(location, actual, sound.getVolume(), sound.getPitch());
    }

    /**
     * Plays the given {@link Sound} using Bukkit methods at the player's {@link Location}.
     * In older versions, {@link Sound#getCategory()} is ignored.
     * The {@link Player#playSound(Location, org.bukkit.Sound, float, float)} method is used
     * to skip sound verification.
     *
     * @param player the player
     * @param sound  the sound
     */
    public static void playCustomSound(final @NotNull Player player, final @NotNull Sound sound) {
        playCustomSound(player, player.getLocation(), sound);
    }

    /**
     * Plays the given {@link Sound} using Bukkit methods.
     * In older versions, {@link Sound#getCategory()} is ignored.
     * The {@link Player#playSound(Location, org.bukkit.Sound, float, float)} method is used
     * to skip sound verification.
     *
     * @param player   the player
     * @param location the location
     * @param sound    the sound
     */
    public static void playCustomSound(final @NotNull Player player, final @NotNull Location location, final @NotNull Sound sound) {
        final String actual = sound.getSound();
        try {
            String category = sound.getCategory();
            if (category != null) {
                final Object actualCategory = EnumUtils.valueOf(Class.forName("org.bukkit.SoundCategory"), category);
                new Refl<>(player).callMethod("playSound", location, actual, actualCategory, sound.getVolume(), sound.getPitch());
                return;
            }
        } catch (Exception e) {
            // Prevent other versions from complaining about method not found.
        }
        player.playSound(location, actual, sound.getVolume(), sound.getPitch());
    }

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
            actual = new Refl<>(org.bukkit.enchantments.Enchantment.class).invokeMethod("getByKey", key);
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
