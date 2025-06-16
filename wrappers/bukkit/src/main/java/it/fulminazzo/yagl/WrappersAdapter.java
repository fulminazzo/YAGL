package it.fulminazzo.yagl;

import it.fulminazzo.yagl.items.AbstractItem;
import it.fulminazzo.yagl.particles.BlockDataOption;
import it.fulminazzo.yagl.particles.Particle;
import it.fulminazzo.yagl.utils.EnumUtils;
import it.fulminazzo.yagl.wrappers.Enchantment;
import it.fulminazzo.yagl.wrappers.Potion;
import it.fulminazzo.yagl.wrappers.PotionEffect;
import it.fulminazzo.yagl.wrappers.Sound;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.CacheMap;
import it.fulminazzo.fulmicollection.structures.tuples.Triple;
import it.fulminazzo.fulmicollection.structures.tuples.Tuple;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
@SuppressWarnings("deprecation")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WrappersAdapter {
    private static final Map<Particle, Tuple<org.bukkit.Particle, ?>> PARTICLE_CACHE = new CacheMap<>();

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
        spawnParticle(world, particle, x, y, z, count, 0.0, 0.0, 0.0);
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
        spawnParticle(world, particle, location, count, 0.0, 0.0, 0.0);
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
        spawnParticle(world, particle, x, y, z, count, offsetX, offsetY, offsetZ, 0.0);
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
        spawnParticle(world, particle, location, count, offsetX, offsetY, offsetZ, 0.0);
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
     * @param speed    the speed
     */
    public static void spawnParticle(final @NotNull World world, final @NotNull Particle particle,
                                     double x, double y, double z, int count,
                                     double offsetX, double offsetY, double offsetZ, double speed) {
        spawnParticle(world, particle, new Location(world, x, y, z), count, offsetX, offsetY, offsetZ, speed);
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
     * @param speed    the speed
     */
    public static void spawnParticle(final @NotNull World world, final @NotNull Particle particle,
                                     final @NotNull Location location, int count,
                                     double offsetX, double offsetY, double offsetZ, double speed) {
        spawnParticleCommon(world, particle, location, count, offsetX, offsetY, offsetZ, speed);
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
        spawnParticle(player, particle, x, y, z, count, 0.0, 0.0, 0.0);
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
        spawnParticle(player, particle, location, count, 0.0, 0.0, 0.0);
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
        spawnParticle(player, particle, x, y, z, count, offsetX, offsetY, offsetZ, 0.0);
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
        spawnParticle(player, particle, location, count, offsetX, offsetY, offsetZ, 0.0);
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
     * @param speed    the speed
     */
    public static void spawnParticle(final @NotNull Player player, final @NotNull Particle particle,
                                     double x, double y, double z, int count,
                                     double offsetX, double offsetY, double offsetZ, double speed) {
        spawnParticle(player, particle, new Location(player.getWorld(), x, y, z), count, offsetX, offsetY, offsetZ, speed);
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
     * @param speed    the speed
     */
    public static void spawnParticle(final @NotNull Player player, final @NotNull Particle particle,
                                     final @NotNull Location location, int count,
                                     double offsetX, double offsetY, double offsetZ, double speed) {
        spawnParticleCommon(player, particle, location, count, offsetX, offsetY, offsetZ, speed);
    }

    private static <T> void spawnParticleCommon(final @NotNull T target, final @NotNull Particle particle,
                                                final @NotNull Location location, int count,
                                                double offsetX, double offsetY, double offsetZ, double speed) {
        Tuple<org.bukkit.Particle, ?> tuple = PARTICLE_CACHE.computeIfAbsent(particle, p -> wParticleToParticle(particle));
        final org.bukkit.Particle actual = tuple.getKey();
        final Object option = tuple.getValue();

        if (target instanceof Player) {
          Player player = (Player) target;
          if (option == null) player.spawnParticle(actual, location, count, offsetX, offsetY, offsetZ, speed);
          else player.spawnParticle(actual, location, count, offsetX, offsetY, offsetZ, speed, option);
        } else if (target instanceof World) {
            World world = (World) target;
            if (option == null) world.spawnParticle(actual, location, count, offsetX, offsetY, offsetZ, speed);
            else world.spawnParticle(actual, location, count, offsetX, offsetY, offsetZ, speed, option);
        } else throw new IllegalArgumentException(String.format("Do not know how to spawn particles for '%s'", target));
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

    @SuppressWarnings("unchecked")
    private static <T extends Enum<?>> @NotNull Tuple<T, ?> wParticleToGeneral(final @NotNull Particle particle,
                                                                               final @NotNull Class<T> tClass,
                                                                               final @NotNull Function<T, Class<?>> dataTypeGetter) {
        T actual;
        if (tClass.getCanonicalName().equals("org.bukkit.Particle")) actual = (T) ParticleConverter.convertToBukkit(particle);
        else actual = EnumUtils.valueOf(tClass, particle.getType());
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

    /**
     * Converts the given raw option in the specified data type.
     *
     * @param dataType the data type
     * @param option   the option
     * @return the object
     */
    @SuppressWarnings("unchecked")
    static @Nullable Object convertOption(@NotNull Class<?> dataType, @NotNull Object option) {
        // Check options
        if (option instanceof AbstractItem) return itemToItemStack((AbstractItem) option);
        else if (option instanceof Potion) return wPotionToPotion((Potion) option);
        else if (option instanceof Color) return wColorToColor((Color) option);
        // Check data types
        else if (dataType.isEnum()) return EnumUtils.valueOf(dataType, option.toString());
        else if (dataType.equals(MaterialData.class)) {
            if (!(option instanceof Tuple))
                throw new IllegalArgumentException(String.format("Expected %s but got %s",
                        Tuple.class.getSimpleName(), option.getClass().getSimpleName()));
            Tuple<String, Integer> tuple = (Tuple<String, Integer>) option;
            Material material = EnumUtils.valueOf(Material.class, tuple.getKey());
            Integer data = tuple.getValue();
            return material.getNewData((byte) (data == null ? 0 : data));
        } else if (dataType.getCanonicalName().equals("org.bukkit.Vibration")) return option;
        else if (dataType.getCanonicalName().equals("org.bukkit.block.data.BlockData")) {
            String raw = option.toString();
            BlockDataOption blockDataOption = new BlockDataOption(raw);
            Material material = EnumUtils.valueOf(Material.class, blockDataOption.getMaterial());
            if (!material.isBlock())
                throw new IllegalArgumentException(String.format("Cannot use non-block material '%s' as block data", material));
            String nbt = blockDataOption.getNBT().trim();
            return nbt.isEmpty() ? material.createBlockData() : material.createBlockData(String.format("[%s]", nbt));
        } else {
            // Try creation from the data type
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
        final Class<?> itemUtils;
        try {
            itemUtils = ReflectionUtils.getClass("it.fulminazzo.yagl.ItemAdapter");
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Could not find ItemAdapter class. This function requires the 'item:bukkit' module to be added");
        }
        return new Refl<>(itemUtils).invokeMethod("itemToItemStack", item);
    }

    /**
     * Converts the given wrapper {@link Color} to a {@link org.bukkit.Color}.
     *
     * @param color the color
     * @return the color
     */
    @SuppressWarnings("DataFlowIssue")
    public static @NotNull org.bukkit.Color wColorToColor(final @NotNull Color color) {
        org.bukkit.Color actualColor;
        try {
            actualColor = new Refl<>(org.bukkit.Color.class).invokeMethod("fromARGB",
                    color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
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
    @SuppressWarnings("DataFlowIssue")
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
        Object actual = actualSound.apply(sound);
        try {
            actual = org.bukkit.Sound.valueOf(actual.toString().toUpperCase());
        } catch (IllegalArgumentException ignored) {
        }
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
        String effect = potionEffect.getName();
        PotionEffectType type;
        try {
            type = EnumUtils.valueOf(PotionEffectType.class, effect, "getByName");
        } catch (IllegalArgumentException e) {
            // For Minecraft 1.20.6 and above
            effect = effect.replace(" ", "_").toLowerCase();
            if (effect.equalsIgnoreCase("bad_luck")) effect = "unluck";
            type = EnumUtils.valueOf(PotionEffectType.class, effect, "getByName");
        }
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
            return new PotionEffect(potionEffect.getType().getName(), (double) potionEffect.getDuration() / Constants.TICKS_IN_SECOND,
                    potionEffect.getAmplifier() + 1, potionEffect.hasParticles(), potionEffect.hasIcon());
        } catch (NoSuchMethodError e) {
            return new PotionEffect(potionEffect.getType().getName(), (double) potionEffect.getDuration() / Constants.TICKS_IN_SECOND,
                    potionEffect.getAmplifier() + 1, potionEffect.hasParticles());
        }
    }

    /**
     * Converts the given wrapper {@link Potion} to a Bukkit potion.
     * Because of Bukkit recent changes (1.20.6+), <code>org.bukkit.potion.Potion</code> does not exist anymore.
     * For retro-compatibility reasons, this class will return a {@link PotionWrapper} instead,
     * from which the actual <code>org.bukkit.potion.Potion</code> object can be retrieved.
     *
     * @param potion the potion
     * @return the wrapped potion
     */
    public static @NotNull PotionWrapper wPotionToPotion(final @NotNull Potion potion) {
        return new PotionWrapper(EnumUtils.valueOf(PotionType.class, potion.getName()),
                potion.getLevel(), potion.isSplash(), potion.isExtended());
    }

    /**
     * Converts the given Bukkit potion to a wrapper {@link Potion}.
     * Because of Bukkit recent changes (1.20.6+), <code>org.bukkit.potion.Potion</code> does not exist anymore.
     * For retro-compatibility reasons, this class allows a generic parameter to be passed,
     * but it will require an object of type org.bukkit.potion.Potion.
     *
     * @param <P>    the type of the potion (org.bukkit.potion.Potion).
     * @param potion the bukkit potion
     * @return the potion
     */
    public static <P> @NotNull Potion potionToWPotion(final @NotNull P potion) {
        return potionToWPotion(new PotionWrapper(potion));
    }

    /**
     * Converts the given potion wrapper to a wrapper {@link Potion}.
     *
     * @param potion the potion wrapper
     * @return the potion
     */
    public static @NotNull Potion potionToWPotion(final @NotNull PotionWrapper potion) {
        return new Potion(potion.getType().name(), potion.getLevel(), potion.isSplash(), potion.hasExtendedDuration());
    }

    /**
     * Converts the given wrapper {@link Enchantment} to a {@link Tuple} containing its corresponding one and its level.
     *
     * @param enchantment the enchantment
     * @return the tuple
     */
    @SuppressWarnings("DataFlowIssue")
    public static @NotNull Tuple<org.bukkit.enchantments.Enchantment, Integer> wEnchantToEnchant(final @NotNull Enchantment enchantment) {
        String raw = enchantment.getName();
        org.bukkit.enchantments.Enchantment actual = null;
        try {
            Object key = getNamespacedKey(raw);
            actual = new Refl<>(org.bukkit.enchantments.Enchantment.class).invokeMethod("getByKey", key);
            if (actual == null) throw new IllegalArgumentException("Cannot find from getKey");
        } catch (Exception e) {
            // Prevent other versions from complaining about method not found.
            Map<String, org.bukkit.enchantments.Enchantment> byName = new Refl<>(org.bukkit.enchantments.Enchantment.class).getFieldObject("byName");
            for (String key : byName.keySet())
                if (key.equalsIgnoreCase(raw)) {
                    actual = byName.get(key);
                    break;
                }
            if (actual == null) throw new IllegalArgumentException(String.format("Could not find enchantment '%s'", raw));
        }
        return new Tuple<>(actual, enchantment.getLevel());
    }

    /**
     * Gets the corresponding namespaced key if the current version of Minecraft supports it.
     * Uses <i>minecraft</i> as the key for {@link #getNamespacedKey(String, String)}.
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
            Class<?> clazz = getNamespacedKeyClass();
            return new Refl<>(clazz, key, value).getObject();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("NamespacedKey did not exist in this version of Minecraft");
        }
    }

    private static @NotNull Class<?> getNamespacedKeyClass() throws ClassNotFoundException {
        return Class.forName("org.bukkit.NamespacedKey");
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
