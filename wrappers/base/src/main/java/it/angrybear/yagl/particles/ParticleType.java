package it.angrybear.yagl.particles;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "unchecked"})
public class ParticleType<P extends ParticleOption<?>> extends AParticleType<P> {
    public static final ParticleType<?> EXPLOSION_NORMAL = new ParticleType<>();
    public static final ParticleType<?> EXPLOSION_LARGE = new ParticleType<>();
    public static final ParticleType<?> EXPLOSION_HUGE = new ParticleType<>();
    public static final ParticleType<?> FIREWORKS_SPARK = new ParticleType<>();
    public static final ParticleType<?> WATER_BUBBLE = new ParticleType<>();
    public static final ParticleType<?> WATER_SPLASH = new ParticleType<>();
    public static final ParticleType<?> WATER_WAKE = new ParticleType<>();
    public static final ParticleType<?> SUSPENDED = new ParticleType<>();
    public static final ParticleType<?> SUSPENDED_DEPTH = new ParticleType<>();
    public static final ParticleType<?> CRIT = new ParticleType<>();
    public static final ParticleType<?> CRIT_MAGIC = new ParticleType<>();
    public static final ParticleType<?> SMOKE_NORMAL = new ParticleType<>();
    public static final ParticleType<?> SMOKE_LARGE = new ParticleType<>();
    public static final ParticleType<?> SPELL = new ParticleType<>();
    public static final ParticleType<?> SPELL_INSTANT = new ParticleType<>();
    public static final ParticleType<?> SPELL_MOB = new ParticleType<>();
    public static final ParticleType<?> SPELL_MOB_AMBIENT = new ParticleType<>();
    public static final ParticleType<?> SPELL_WITCH = new ParticleType<>();
    public static final ParticleType<?> DRIP_WATER = new ParticleType<>();
    public static final ParticleType<?> DRIP_LAVA = new ParticleType<>();
    public static final ParticleType<?> VILLAGER_ANGRY = new ParticleType<>();
    public static final ParticleType<?> VILLAGER_HAPPY = new ParticleType<>();
    public static final ParticleType<?> TOWN_AURA = new ParticleType<>();
    public static final ParticleType<?> NOTE = new ParticleType<>();
    public static final ParticleType<?> PORTAL = new ParticleType<>();
    public static final ParticleType<?> ENCHANTMENT_TABLE = new ParticleType<>();
    public static final ParticleType<?> FLAME = new ParticleType<>();
    public static final ParticleType<?> LAVA = new ParticleType<>();
    public static final ParticleType<?> CLOUD = new ParticleType<>();
    public static final ParticleType<DustParticleOption> REDSTONE = new ParticleType<>(DustParticleOption.class);
    public static final ParticleType<?> SNOWBALL = new ParticleType<>();
    public static final ParticleType<?> SNOW_SHOVEL = new ParticleType<>();
    public static final ParticleType<?> SLIME = new ParticleType<>();
    public static final ParticleType<?> HEART = new ParticleType<>();
    /**
     * This particle type requires the <i>item:base</i> module to be added.
     */
    public static final ParticleType<ItemParticleOption<?>> ITEM_CRACK = new ParticleType<>((Class<ItemParticleOption<?>>) (Class<?>) ItemParticleOption.class);
    public static final ParticleType<BlockDataOption> BLOCK_CRACK = new ParticleType<>(BlockDataOption.class);
    public static final ParticleType<BlockDataOption> BLOCK_DUST = new ParticleType<>(BlockDataOption.class);
    public static final ParticleType<?> WATER_DROP = new ParticleType<>();
    public static final ParticleType<?> MOB_APPEARANCE = new ParticleType<>();
    public static final ParticleType<?> DRAGON_BREATH = new ParticleType<>();
    public static final ParticleType<?> END_ROD = new ParticleType<>();
    public static final ParticleType<?> DAMAGE_INDICATOR = new ParticleType<>();
    public static final ParticleType<?> SWEEP_ATTACK = new ParticleType<>();
    public static final ParticleType<BlockDataOption> FALLING_DUST = new ParticleType<>(BlockDataOption.class);
    public static final ParticleType<?> TOTEM = new ParticleType<>();
    public static final ParticleType<?> SPIT = new ParticleType<>();
    public static final ParticleType<?> SQUID_INK = new ParticleType<>();
    public static final ParticleType<?> BUBBLE_POP = new ParticleType<>();
    public static final ParticleType<?> CURRENT_DOWN = new ParticleType<>();
    public static final ParticleType<?> BUBBLE_COLUMN_UP = new ParticleType<>();
    public static final ParticleType<?> NAUTILUS = new ParticleType<>();
    public static final ParticleType<?> DOLPHIN = new ParticleType<>();
    public static final ParticleType<?> SNEEZE = new ParticleType<>();
    public static final ParticleType<?> CAMPFIRE_COSY_SMOKE = new ParticleType<>();
    public static final ParticleType<?> CAMPFIRE_SIGNAL_SMOKE = new ParticleType<>();
    public static final ParticleType<?> COMPOSTER = new ParticleType<>();
    public static final ParticleType<?> FLASH = new ParticleType<>();
    public static final ParticleType<?> FALLING_LAVA = new ParticleType<>();
    public static final ParticleType<?> LANDING_LAVA = new ParticleType<>();
    public static final ParticleType<?> FALLING_WATER = new ParticleType<>();
    public static final ParticleType<?> DRIPPING_HONEY = new ParticleType<>();
    public static final ParticleType<?> FALLING_HONEY = new ParticleType<>();
    public static final ParticleType<?> LANDING_HONEY = new ParticleType<>();
    public static final ParticleType<?> FALLING_NECTAR = new ParticleType<>();
    public static final ParticleType<?> SOUL_FIRE_FLAME = new ParticleType<>();
    public static final ParticleType<?> ASH = new ParticleType<>();
    public static final ParticleType<?> CRIMSON_SPORE = new ParticleType<>();
    public static final ParticleType<?> WARPED_SPORE = new ParticleType<>();
    public static final ParticleType<?> SOUL = new ParticleType<>();
    public static final ParticleType<?> DRIPPING_OBSIDIAN_TEAR = new ParticleType<>();
    public static final ParticleType<?> FALLING_OBSIDIAN_TEAR = new ParticleType<>();
    public static final ParticleType<?> LANDING_OBSIDIAN_TEAR = new ParticleType<>();
    public static final ParticleType<?> REVERSE_PORTAL = new ParticleType<>();
    public static final ParticleType<?> WHITE_ASH = new ParticleType<>();
    public static final ParticleType<DustTransitionParticleOption> DUST_COLOR_TRANSITION = new ParticleType<>(DustTransitionParticleOption.class);
    /**
     * Because of the little use case that Vibration has
     * at the time of writing this comment, there is no
     * specific {@link ParticleOption} for it.
     * One can use a {@link PrimitiveParticleOption} and
     * pass the actual Vibration object to it.
     * <b>NOTE</b> it will not be serialized automatically.
     */
    public static final ParticleType<PrimitiveParticleOption<Object>> VIBRATION = new ParticleType<>((Class<PrimitiveParticleOption<Object>>) (Class<?>) PrimitiveParticleOption.class);
    public static final ParticleType<?> FALLING_SPORE_BLOSSOM = new ParticleType<>();
    public static final ParticleType<?> SPORE_BLOSSOM_AIR = new ParticleType<>();
    public static final ParticleType<?> SMALL_FLAME = new ParticleType<>();
    public static final ParticleType<?> SNOWFLAKE = new ParticleType<>();
    public static final ParticleType<?> DRIPPING_DRIPSTONE_LAVA = new ParticleType<>();
    public static final ParticleType<?> FALLING_DRIPSTONE_LAVA = new ParticleType<>();
    public static final ParticleType<?> DRIPPING_DRIPSTONE_WATER = new ParticleType<>();
    public static final ParticleType<?> FALLING_DRIPSTONE_WATER = new ParticleType<>();
    public static final ParticleType<?> GLOW_SQUID_INK = new ParticleType<>();
    public static final ParticleType<?> GLOW = new ParticleType<>();
    public static final ParticleType<?> WAX_ON = new ParticleType<>();
    public static final ParticleType<?> WAX_OFF = new ParticleType<>();
    public static final ParticleType<?> ELECTRIC_SPARK = new ParticleType<>();
    public static final ParticleType<?> SCRAPE = new ParticleType<>();
    public static final ParticleType<?> SONIC_BOOM = new ParticleType<>();
    public static final ParticleType<?> SCULK_SOUL = new ParticleType<>();
    public static final ParticleType<PrimitiveParticleOption<Float>> SCULK_CHARGE = new ParticleType<>((Class<PrimitiveParticleOption<Float>>) (Class<?>) PrimitiveParticleOption.class);
    public static final ParticleType<?> SCULK_CHARGE_POP = new ParticleType<>();
    public static final ParticleType<PrimitiveParticleOption<Integer>> SHRIEK = new ParticleType<>((Class<PrimitiveParticleOption<Integer>>) (Class<?>) PrimitiveParticleOption.class);
    public static final ParticleType<?> CHERRY_LEAVES = new ParticleType<>();
    public static final ParticleType<?> EGG_CRACK = new ParticleType<>();

    private ParticleType() {
        this(null);
    }

    private ParticleType(Class<P> optionType) {
        super(optionType);
    }

    public static ParticleType<?> valueOf(final int index) {
        return valueOf(index, ParticleType.class);
    }

    public static ParticleType<?> valueOf(final @NotNull String name) {
        return valueOf(name, ParticleType.class);
    }

    public static ParticleType<?>[] values() {
        return values(ParticleType.class);
    }
}
