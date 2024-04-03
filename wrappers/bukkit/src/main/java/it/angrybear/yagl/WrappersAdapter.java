package it.angrybear.yagl;

import it.angrybear.yagl.items.AbstractItem;
import it.angrybear.yagl.particles.BlockDataOption;
import it.angrybear.yagl.particles.Particle;
import it.angrybear.yagl.utils.EnumUtils;
import it.angrybear.yagl.wrappers.Enchantment;
import it.angrybear.yagl.wrappers.Potion;
import it.angrybear.yagl.wrappers.PotionEffect;
import it.angrybear.yagl.wrappers.Sound;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.Triple;
import it.fulminazzo.fulmicollection.structures.Tuple;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.function.Function;

/**
 * A utility class to convert objects from this library to Minecraft Bukkit and vice versa.
 */
public class WrappersAdapter {

    /**
     * Spawn particle.
     *
     * @param world    the world
     * @param particle the particle
     * @param x        the x
     * @param y        the y
     * @param z        the z
     * @param count    the count
     */
    public static void spawnParticle(final @NotNull World world, final @NotNull Particle particle,
                                     double x, double y, double z, int count) {
        spawnParticle(world, particle, x, y, z, count, 0, 0, 0);
    }

    /**
     * Spawn particle.
     *
     * @param world    the world
     * @param particle the particle
     * @param location the location
     * @param count    the count
     */
    public static void spawnParticle(final @NotNull World world, final @NotNull Particle particle,
                                     final @NotNull Location location, int count) {
        spawnParticle(world, particle, location, count, 0, 0, 0);
    }

    /**
     * Spawn particle.
     *
     * @param world    the world
     * @param particle the particle
     * @param x        the x
     * @param y        the y
     * @param z        the z
     * @param count    the count
     * @param offsetX  the offset x
     * @param offsetY  the offset y
     * @param offsetZ  the offset z
     */
    public static void spawnParticle(final @NotNull World world, final @NotNull Particle particle,
                                     double x, double y, double z, int count,
                                     double offsetX, double offsetY, double offsetZ) {
        spawnParticle(world, particle, new Location(world, x, y, z), count, offsetX, offsetY, offsetZ);
    }

    /**
     * Spawn particle.
     *
     * @param world    the world
     * @param particle the particle
     * @param location the location
     * @param count    the count
     * @param offsetX  the offset x
     * @param offsetY  the offset y
     * @param offsetZ  the offset z
     */
    public static void spawnParticle(final @NotNull World world, final @NotNull Particle particle,
                                     final @NotNull Location location, int count,
                                     double offsetX, double offsetY, double offsetZ) {
        Tuple<org.bukkit.Particle, ?> tuple = wParticleToParticle(particle);
        org.bukkit.Particle actual = tuple.getKey();
        Object option = tuple.getValue();
        if (option == null) world.spawnParticle(actual, location, count, offsetX, offsetY, offsetZ);
        else world.spawnParticle(actual, location, count, offsetX, offsetY, offsetZ, option);
    }

    /**
     * Spawn particle.
     *
     * @param player   the player
     * @param particle the particle
     * @param x        the x
     * @param y        the y
     * @param z        the z
     * @param count    the count
     */
    public static void spawnParticle(final @NotNull Player player, final @NotNull Particle particle,
                                     double x, double y, double z, int count) {
        spawnParticle(player, particle, x, y, z, count, 0, 0, 0);
    }

    /**
     * Spawn particle.
     *
     * @param player   the player
     * @param particle the particle
     * @param location the location
     * @param count    the count
     */
    public static void spawnParticle(final @NotNull Player player, final @NotNull Particle particle,
                                     final @NotNull Location location, int count) {
        spawnParticle(player, particle, location, count, 0, 0, 0);
    }

    /**
     * Spawn particle.
     *
     * @param player   the player
     * @param particle the particle
     * @param x        the x
     * @param y        the y
     * @param z        the z
     * @param count    the count
     * @param offsetX  the offset x
     * @param offsetY  the offset y
     * @param offsetZ  the offset z
     */
    public static void spawnParticle(final @NotNull Player player, final @NotNull Particle particle,
                                     double x, double y, double z, int count,
                                     double offsetX, double offsetY, double offsetZ) {
        spawnParticle(player, particle, new Location(player.getWorld(), x, y, z), count, offsetX, offsetY, offsetZ);
    }

    /**
     * Spawn particle.
     *
     * @param player   the player
     * @param particle the particle
     * @param location the location
     * @param count    the count
     * @param offsetX  the offset x
     * @param offsetY  the offset y
     * @param offsetZ  the offset z
     */
    public static void spawnParticle(final @NotNull Player player, final @NotNull Particle particle,
                                     final @NotNull Location location, int count,
                                     double offsetX, double offsetY, double offsetZ) {
        Tuple<org.bukkit.Particle, ?> tuple = wParticleToParticle(particle);
        org.bukkit.Particle actual = tuple.getKey();
        Object option = tuple.getValue();
        if (option == null) player.spawnParticle(actual, location, count, offsetX, offsetY, offsetZ);
        else player.spawnParticle(actual, location, count, offsetX, offsetY, offsetZ, option);
    }

    /**
     * Converts the given {@link Particle} to a tuple containing the corresponding {@link org.bukkit.Particle} and
     * the parsed particle option (if present).
     *
     * @param particle the particle
     * @return the tuple
     */
    public static @NotNull Tuple<org.bukkit.Particle, ?> wParticleToParticle(final @NotNull Particle particle) {
        return wParticleToGeneral(particle, org.bukkit.Particle.class, org.bukkit.Particle::getDataType);
    }

    /**
     * Spawn effect.
     *
     * @param world    the world
     * @param particle the particle
     * @param x        the x
     * @param y        the y
     * @param z        the z
     */
    public static void spawnEffect(final @NotNull World world, final @NotNull Particle particle,
                                   double x, double y, double z) {
        spawnEffect(world, particle, new Location(world, x, y, z));
    }

    /**
     * Spawn effect.
     *
     * @param world    the world
     * @param particle the particle
     * @param location the location
     */
    public static void spawnEffect(final @NotNull World world, final @NotNull Particle particle,
                                   final @NotNull Location location) {
        world.getPlayers().forEach(p -> spawnEffect(p, particle, location));
    }

    /**
     * Spawn effect.
     *
     * @param player   the player
     * @param particle the particle
     * @param x        the x
     * @param y        the y
     * @param z        the z
     */
    public static void spawnEffect(final @NotNull Player player, final @NotNull Particle particle,
                                     double x, double y, double z) {
        spawnEffect(player, particle, new Location(player.getWorld(), x, y, z));
    }

    /**
     * Spawn effect.
     *
     * @param player   the player
     * @param particle the particle
     * @param location the location
     */
    public static void spawnEffect(final @NotNull Player player, final @NotNull Particle particle,
                                   final @NotNull Location location) {
        Tuple<Effect, ?> tuple = wParticleToEffect(particle);
        Effect actual = tuple.getKey();
        Object option = tuple.getValue();
        player.playEffect(location, actual, option);
    }

    /**
     * Converts the given {@link Particle} to a tuple containing the corresponding {@link Effect} and
     * the parsed particle option (if present).
     *
     * @param particle the particle
     * @return the tuple
     */
    public static @NotNull Tuple<Effect, ?> wParticleToEffect(final @NotNull Particle particle) {
        return wParticleToGeneral(particle, Effect.class, Effect::getData);
    }

    private static <T extends Enum<?>> @NotNull Tuple<T, ?> wParticleToGeneral(final @NotNull Particle particle,
                                                                               final @NotNull Class<T> tClass,
                                                                               final @NotNull Function<T, Class<?>> dataTypeGetter) {
        T actual = EnumUtils.valueOf(tClass, particle.getType());
        Object option = particle.getOption();
        Class<?> dataType = dataTypeGetter.apply(actual);
        if (option == null || dataType == null) return new Tuple<>(actual, null);
        else {
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

    static @Nullable Object convertOption(@NotNull Class<?> dataType, @NotNull Object option) {
        if (option instanceof AbstractItem) return itemToItemStack((AbstractItem) option);
        if (option instanceof Potion) return wPotionToPotion((Potion) option);
        if (dataType.isEnum()) return EnumUtils.valueOf(dataType, option.toString());
        if (dataType.equals(MaterialData.class)) {
            if (!(option instanceof Tuple))
                throw new IllegalArgumentException(String.format("Expected %s but got %s",
                        Tuple.class.getSimpleName(), option.getClass().getSimpleName()));
            Tuple<String, Integer> tuple = (Tuple<String, Integer>) option;
            Material material = EnumUtils.valueOf(Material.class, tuple.getKey());
            Integer data = tuple.getValue();
            return material.getNewData((byte) (data == null ? 0 : data));
        }
        if (dataType.getCanonicalName().equalsIgnoreCase("org.bukkit.Vibration")) return option;
        if (dataType.getSimpleName().equals("BlockData")) {
            String raw = option.toString();
            BlockDataOption blockDataOption = new BlockDataOption(raw);
            Material material = EnumUtils.valueOf(Material.class, blockDataOption.getMaterial());
            if (!material.isBlock())
                throw new IllegalArgumentException(String.format("Cannot use non-block material '%s' as block data", material));
            String nbt = blockDataOption.getNBT().trim();
            return nbt.isEmpty() ? material.createBlockData() : material.createBlockData(String.format("[%s]", nbt));
        }
        if (option instanceof Color) return wColorToColor((Color) option);
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

    private static Object @NotNull [] prepareParameters(final Object @NotNull ... parameters) {
        for (int i = 0; i < parameters.length; i++) {
            Object o = parameters[i];
            if (o instanceof Color) parameters[i] = wColorToColor((Color) o);
        }
        return parameters;
    }

    /**
     * Converts the given {@link AbstractItem} to an {@link ItemStack}.
     *
     * @param item the item
     * @return the item stack
     */
    public static @Nullable ItemStack itemToItemStack(final @Nullable AbstractItem item) {
        try {
            Class<?> clazz = Class.forName("it.angrybear.yagl.utils.ItemUtils");
            return new Refl<>(clazz).invokeMethod("itemToItemStack", item);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find ItemUtils class. This function requires the 'item:bukkit' module to be added");
        }
    }

    /**
     * Converts the given wrapper {@link Color} to a {@link org.bukkit.Color}.
     *
     * @param color the color
     * @return the color
     */
    public static @NotNull org.bukkit.Color wColorToColor(final @NotNull Color color) {
        org.bukkit.Color actualColor;
        try {
            actualColor = new Refl<>(org.bukkit.Color.class).invokeMethod("fromARGB",
                    color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
            if (actualColor == null) throw new IllegalStateException("Unreachable code");
        } catch (Exception e) {
            actualColor = org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
        }
        return actualColor;
    }

    /**
     * Converts the given {@link org.bukkit.Color} to a wrapper {@link Color}.
     *
     * @param color the color
     * @return the color
     */
    public static @NotNull Color colorToWColor(final @NotNull org.bukkit.Color color) {
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
        playInternalSound(player, location, sound, s -> EnumUtils.valueOf(org.bukkit.Sound.class, s.getName()));
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
        playInternalSound(player, location, sound, Sound::getName);
    }

    private static <T> void playInternalSound(final @NotNull Player player, final @NotNull Location location,
                                              final @NotNull Sound sound, final @NotNull Function<Sound, T> actualSound) {
        final Refl<Player> playerRefl = new Refl<>(player);
        final T actual = actualSound.apply(sound);
        try {
            String category = sound.getCategory();
            if (category != null) {
                final Object actualCategory = EnumUtils.valueOf(Class.forName("org.bukkit.SoundCategory"), category);
                playerRefl.callMethod("playSound", location, actual, actualCategory, sound.getVolume(), sound.getPitch());
                return;
            }
        } catch (ClassNotFoundException e) {
            // Prevent other versions from complaining about method not found.
        }
        playerRefl.callMethod("playSound", location, actual, sound.getVolume(), sound.getPitch());
    }

    /**
     * Converts the given wrapper {@link PotionEffect} to a {@link org.bukkit.potion.PotionEffect}.
     *
     * @param potionEffect the potion effect
     * @return the potion effect
     */
    public static @NotNull org.bukkit.potion.PotionEffect wPotionEffectToPotionEffect(final @NotNull PotionEffect potionEffect) {
        final String effect = potionEffect.getName();
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
    public static @NotNull PotionEffect potionEffectToWPotionEffect(final @NotNull org.bukkit.potion.PotionEffect potionEffect) {
        try {
            return new PotionEffect(potionEffect.getType().getName(), (double) potionEffect.getDuration() / 20,
                    potionEffect.getAmplifier() + 1, potionEffect.hasParticles(), potionEffect.hasIcon());
        } catch (NoSuchMethodError e) {
            return new PotionEffect(potionEffect.getType().getName(), (double) potionEffect.getDuration() / 20,
                    potionEffect.getAmplifier() + 1, potionEffect.hasParticles());
        }
    }

    /**
     * Converts the given wrapper {@link Potion} to a Bukkit potion.
     *
     * @param potion the potion
     * @return the potion
     */
    public static @NotNull org.bukkit.potion.Potion wPotionToPotion(final @NotNull Potion potion) {
        return new org.bukkit.potion.Potion(EnumUtils.valueOf(PotionType.class, potion.getName()),
                potion.getLevel(), potion.isSplash(), potion.isExtended());
    }

    /**
     * Converts the given Bukkit potion to a wrapper {@link Potion}.
     *
     * @param potion the potion
     * @return the potion
     */
    public static @NotNull Potion potionToWPotion(final @NotNull org.bukkit.potion.Potion potion) {
        return new Potion(potion.getType().name(), potion.getLevel(), potion.isSplash(), potion.hasExtendedDuration());
    }

    /**
     * Converts the given wrapper {@link Enchantment} to a {@link Tuple} containing its corresponding one and its level.
     *
     * @param enchantment the enchantment
     * @return the tuple
     */
    public static @NotNull Tuple<org.bukkit.enchantments.Enchantment, Integer> wEnchantToEnchant(final @NotNull Enchantment enchantment) {
        String raw = enchantment.getName();
        org.bukkit.enchantments.Enchantment actual = null;
        try {
            Object key = getNamespacedKey(raw);
            actual = new Refl<>(org.bukkit.enchantments.Enchantment.class).invokeMethod("getByKey", key);
            if (actual == null) throw new Exception("Cannot find from getKey");
        } catch (Exception e) {
            // Prevent other versions from complaining about method not found.
            Map<String, org.bukkit.enchantments.Enchantment> byName = new Refl<>(org.bukkit.enchantments.Enchantment.class).getFieldObject("byName");
            if (byName != null)
                for (String key : byName.keySet())
                    if (key.equalsIgnoreCase(raw)) {
                        actual = byName.get(key);
                        break;
                    }
            if (actual == null) throw new IllegalStateException(String.format("Could not find enchantment '%s'", raw));
        }
        return new Tuple<>(actual, enchantment.getLevel());
    }

    /**
     * Gets the corresponding namespaced key if the current version of Minecraft supports it.
     * Uses <i>minecraft</i> as key for {@link #getNamespacedKey(String, String)}.
     *
     * @param value the value
     * @return the namespaced key
     */
    public static Object getNamespacedKey(final @NotNull String value) {
        return getNamespacedKey("minecraft", value);
    }

    /**
     * Gets the corresponding namespaced key if the current version of Minecraft supports it.
     *
     * @param key   the key
     * @param value the value
     * @return the namespaced key
     */
    public static Object getNamespacedKey(final @NotNull String key, final @NotNull String value) {
        try {
            Class<?> clazz = Class.forName("org.bukkit.NamespacedKey");
            return new Refl<>(clazz, key, value).getObject();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("NamespacedKey did not exist in this version of Minecraft");
        }
    }

    /**
     * Converts the given Bukkit enchantment to a {@link Enchantment}.
     * Uses {@link org.bukkit.enchantments.Enchantment#getName()} for retro-compatibility purposes.
     *
     * @param enchantment the enchantment
     * @return the enchantment
     */
    public static @NotNull Enchantment enchantToWEnchant(final @NotNull org.bukkit.enchantments.Enchantment enchantment) {
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
    public static @NotNull Enchantment enchantToWEnchant(final @NotNull org.bukkit.enchantments.Enchantment enchantment, final int level) {
        return new Enchantment(enchantment.getName(), level);
    }
}
