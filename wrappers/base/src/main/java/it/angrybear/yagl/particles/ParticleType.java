package it.angrybear.yagl.particles;

import it.angrybear.yagl.ClassEnum;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused", "unchecked"})
public class ParticleType<P extends ParticleOption<?>> extends ClassEnum {
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
    //TODO:
//    ITEM_CRACK(ItemStack.class) = new ParticleType<>();
    //TODO:
//    BLOCK_CRACK(BlockData.class) = new ParticleType<>();
    //TODO:
//    BLOCK_DUST(BlockData.class) = new ParticleType<>();
    public static final ParticleType<?> WATER_DROP = new ParticleType<>();
    public static final ParticleType<?> MOB_APPEARANCE = new ParticleType<>();
    public static final ParticleType<?> DRAGON_BREATH = new ParticleType<>();
    public static final ParticleType<?> END_ROD = new ParticleType<>();
    public static final ParticleType<?> DAMAGE_INDICATOR = new ParticleType<>();
    public static final ParticleType<?> SWEEP_ATTACK = new ParticleType<>();
    //TODO:
//    FALLING_DUST(BlockData.class) = new ParticleType<>();
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
    //TODO:
//    VIBRATION(Vibration.class) = new ParticleType<>();
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

    @Getter(AccessLevel.PACKAGE)
    private final Class<P> optionType;

    public ParticleType(Class<P> optionType) {
        this.optionType = optionType;
    }

    public ParticleType() {
        this(null);
    }

    public Particle createParticle() {
        return createParticle(null);
    }

    public Particle createParticle(final @Nullable P particleOption) {
        return new Particle(name(), particleOption);
    }

    public static ParticleType<?> valueOf(final @NotNull String name) {
        return valueOf(name, ParticleType.class);
    }

    public static ParticleType<?>[] values() {
        return values(ParticleType.class);
    }
}
