package it.fulminazzo.yagl.particle;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@SuppressWarnings({"unused", "unchecked"})
public final class LegacyParticleType<P extends ParticleOption<?>> extends AParticleType<P> {
    public static final LegacyParticleType<PrimitiveParticleOption<Integer>> BONE_MEAL_USE = new LegacyParticleType<>((Class<PrimitiveParticleOption<Integer>>) (Class<?>) PrimitiveParticleOption.class);
    public static final LegacyParticleType<?> CLOUD = new LegacyParticleType<>();
    public static final LegacyParticleType<?> COLOURED_DUST = new LegacyParticleType<>();
    /*
     * Minecraft 1.20+
     */
    public static final LegacyParticleType<PrimitiveParticleOption<Boolean>> COMPOSTER_FILL_ATTEMPT = new LegacyParticleType<>((Class<PrimitiveParticleOption<Boolean>>) (Class<?>) PrimitiveParticleOption.class);
    public static final LegacyParticleType<?> COPPER_WAX_OFF = new LegacyParticleType<>();
    public static final LegacyParticleType<?> COPPER_WAX_ON = new LegacyParticleType<>();
    public static final LegacyParticleType<?> CRIT = new LegacyParticleType<>();
    public static final LegacyParticleType<?> DRAGON_BREATH = new LegacyParticleType<>();
    public static final LegacyParticleType<?> DRIPPING_DRIPSTONE = new LegacyParticleType<>();
    public static final LegacyParticleType<PrimitiveParticleOption<String>> ELECTRIC_SPARK = new LegacyParticleType<>((Class<PrimitiveParticleOption<String>>) (Class<?>) PrimitiveParticleOption.class);
    public static final LegacyParticleType<?> ENDER_DRAGON_DESTROY_BLOCK = new LegacyParticleType<>();
    public static final LegacyParticleType<?> ENDER_SIGNAL = new LegacyParticleType<>();
    public static final LegacyParticleType<?> END_GATEWAY_SPAWN = new LegacyParticleType<>();
    public static final LegacyParticleType<?> END_PORTAL_FRAME_FILL = new LegacyParticleType<>();
    public static final LegacyParticleType<?> EXPLOSION = new LegacyParticleType<>();
    public static final LegacyParticleType<?> EXPLOSION_HUGE = new LegacyParticleType<>();
    public static final LegacyParticleType<?> EXPLOSION_LARGE = new LegacyParticleType<>();
    public static final LegacyParticleType<?> FIREWORKS_SPARK = new LegacyParticleType<>();
    public static final LegacyParticleType<?> FLAME = new LegacyParticleType<>();
    public static final LegacyParticleType<?> FLYING_GLYPH = new LegacyParticleType<>();
    public static final LegacyParticleType<?> FOOTSTEP = new LegacyParticleType<>();
    public static final LegacyParticleType<?> HAPPY_VILLAGER = new LegacyParticleType<>();
    public static final LegacyParticleType<?> HEART = new LegacyParticleType<>();
    public static final LegacyParticleType<ColorParticleOption> INSTANT_POTION_BREAK = new LegacyParticleType<>(ColorParticleOption.class);
    public static final LegacyParticleType<?> INSTANT_SPELL = new LegacyParticleType<>();
    public static final LegacyParticleType<PrimitiveParticleOption<String>> ITEM_BREAK = new LegacyParticleType<>((Class<PrimitiveParticleOption<String>>) (Class<?>) PrimitiveParticleOption.class);
    public static final LegacyParticleType<?> LARGE_SMOKE = new LegacyParticleType<>();
    public static final LegacyParticleType<?> LAVADRIP = new LegacyParticleType<>();
    public static final LegacyParticleType<?> LAVA_INTERACT = new LegacyParticleType<>();
    public static final LegacyParticleType<?> LAVA_POP = new LegacyParticleType<>();
    public static final LegacyParticleType<?> MAGIC_CRIT = new LegacyParticleType<>();
    public static final LegacyParticleType<?> MOBSPAWNER_FLAMES = new LegacyParticleType<>();
    public static final LegacyParticleType<?> NOTE = new LegacyParticleType<>();
    public static final LegacyParticleType<?> OXIDISED_COPPER_SCRAPE = new LegacyParticleType<>();
    public static final LegacyParticleType<?> PARTICLE_SMOKE = new LegacyParticleType<>();
    public static final LegacyParticleType<?> PORTAL = new LegacyParticleType<>();
    public static final LegacyParticleType<PotionParticleOption> POTION_BREAK = new LegacyParticleType<>(PotionParticleOption.class);
    public static final LegacyParticleType<?> POTION_SWIRL = new LegacyParticleType<>();
    public static final LegacyParticleType<?> POTION_SWIRL_TRANSPARENT = new LegacyParticleType<>();
    public static final LegacyParticleType<?> REDSTONE_TORCH_BURNOUT = new LegacyParticleType<>();
    public static final LegacyParticleType<?> SLIME = new LegacyParticleType<>();
    public static final LegacyParticleType<?> SMALL_SMOKE = new LegacyParticleType<>();
    public static final LegacyParticleType<PrimitiveParticleOption<String>> SMOKE = new LegacyParticleType<>((Class<PrimitiveParticleOption<String>>) (Class<?>) PrimitiveParticleOption.class);
    public static final LegacyParticleType<?> SNOWBALL_BREAK = new LegacyParticleType<>();
    public static final LegacyParticleType<?> SNOW_SHOVEL = new LegacyParticleType<>();
    public static final LegacyParticleType<?> SPELL = new LegacyParticleType<>();
    public static final LegacyParticleType<?> SPLASH = new LegacyParticleType<>();
    public static final LegacyParticleType<?> SPONGE_DRY = new LegacyParticleType<>();
    public static final LegacyParticleType<MaterialDataOption> TILE_BREAK = new LegacyParticleType<>(MaterialDataOption.class);
    public static final LegacyParticleType<MaterialDataOption> TILE_DUST = new LegacyParticleType<>(MaterialDataOption.class);
    public static final LegacyParticleType<PrimitiveParticleOption<Integer>> VILLAGER_PLANT_GROW = new LegacyParticleType<>((Class<PrimitiveParticleOption<Integer>>) (Class<?>) PrimitiveParticleOption.class);
    public static final LegacyParticleType<?> VILLAGER_THUNDERCLOUD = new LegacyParticleType<>();
    public static final LegacyParticleType<?> VOID_FOG = new LegacyParticleType<>();
    public static final LegacyParticleType<?> WATERDRIP = new LegacyParticleType<>();
    public static final LegacyParticleType<?> WITCH_MAGIC = new LegacyParticleType<>();

    private static int COUNTER = 0;
    private final int ord = COUNTER++;

    private LegacyParticleType() {
        this(null);
    }

    private LegacyParticleType(Class<P> optionType) {
        super(optionType);
    }

    public static LegacyParticleType<?> valueOf(final int index) {
        return valueOf(index, LegacyParticleType.class);
    }

    public static LegacyParticleType<?> valueOf(final @NotNull String name) {
        return valueOf(name, LegacyParticleType.class);
    }

    public static LegacyParticleType<?>[] legacyValues() {
        return Arrays.copyOf(values(), COMPOSTER_FILL_ATTEMPT.ord);
    }

    public static LegacyParticleType<?>[] values() {
        return values(LegacyParticleType.class);
    }

}
