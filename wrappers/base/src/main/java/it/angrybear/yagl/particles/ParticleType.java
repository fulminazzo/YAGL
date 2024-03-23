package it.angrybear.yagl.particles;

import org.jetbrains.annotations.Nullable;

public class ParticleType<P extends ParticleOption> {
    public static ParticleType<?> EXPLOSION_NORMAL = new ParticleType<>();
    public static ParticleType<?> EXPLOSION_LARGE = new ParticleType<>();
    public static ParticleType<?> EXPLOSION_HUGE = new ParticleType<>();
    public static ParticleType<?> FIREWORKS_SPARK = new ParticleType<>();
    public static ParticleType<?> WATER_BUBBLE = new ParticleType<>();
    public static ParticleType<?> WATER_SPLASH = new ParticleType<>();
    public static ParticleType<?> WATER_WAKE = new ParticleType<>();
    public static ParticleType<?> SUSPENDED = new ParticleType<>();
    public static ParticleType<?> SUSPENDED_DEPTH = new ParticleType<>();
    public static ParticleType<?> CRIT = new ParticleType<>();
    public static ParticleType<?> CRIT_MAGIC = new ParticleType<>();
    public static ParticleType<?> SMOKE_NORMAL = new ParticleType<>();
    public static ParticleType<?> SMOKE_LARGE = new ParticleType<>();
    public static ParticleType<?> SPELL = new ParticleType<>();
    public static ParticleType<?> SPELL_INSTANT = new ParticleType<>();
    public static ParticleType<?> SPELL_MOB = new ParticleType<>();
    public static ParticleType<?> SPELL_MOB_AMBIENT = new ParticleType<>();
    public static ParticleType<?> SPELL_WITCH = new ParticleType<>();
    public static ParticleType<?> DRIP_WATER = new ParticleType<>();
    public static ParticleType<?> DRIP_LAVA = new ParticleType<>();
    public static ParticleType<?> VILLAGER_ANGRY = new ParticleType<>();
    public static ParticleType<?> VILLAGER_HAPPY = new ParticleType<>();
    public static ParticleType<?> TOWN_AURA = new ParticleType<>();
    public static ParticleType<?> NOTE = new ParticleType<>();
    public static ParticleType<?> PORTAL = new ParticleType<>();
    public static ParticleType<?> ENCHANTMENT_TABLE = new ParticleType<>();
    public static ParticleType<?> FLAME = new ParticleType<>();
    public static ParticleType<?> LAVA = new ParticleType<>();
    public static ParticleType<?> CLOUD = new ParticleType<>();
    //TODO:
//    REDSTONE(DustOptions.class) = new ParticleType<>();
    public static ParticleType<?> SNOWBALL = new ParticleType<>();
    public static ParticleType<?> SNOW_SHOVEL = new ParticleType<>();
    public static ParticleType<?> SLIME = new ParticleType<>();
    public static ParticleType<?> HEART = new ParticleType<>();
    //TODO:
//    ITEM_CRACK(ItemStack.class) = new ParticleType<>();
    //TODO:
//    BLOCK_CRACK(BlockData.class) = new ParticleType<>();
    //TODO:
//    BLOCK_DUST(BlockData.class) = new ParticleType<>();
    public static ParticleType<?> WATER_DROP = new ParticleType<>();
    public static ParticleType<?> MOB_APPEARANCE = new ParticleType<>();
    public static ParticleType<?> DRAGON_BREATH = new ParticleType<>();
    public static ParticleType<?> END_ROD = new ParticleType<>();
    public static ParticleType<?> DAMAGE_INDICATOR = new ParticleType<>();
    public static ParticleType<?> SWEEP_ATTACK = new ParticleType<>();
    //TODO:
//    FALLING_DUST(BlockData.class) = new ParticleType<>();
    public static ParticleType<?> TOTEM = new ParticleType<>();
    public static ParticleType<?> SPIT = new ParticleType<>();
    public static ParticleType<?> SQUID_INK = new ParticleType<>();
    public static ParticleType<?> BUBBLE_POP = new ParticleType<>();
    public static ParticleType<?> CURRENT_DOWN = new ParticleType<>();
    public static ParticleType<?> BUBBLE_COLUMN_UP = new ParticleType<>();
    public static ParticleType<?> NAUTILUS = new ParticleType<>();
    public static ParticleType<?> DOLPHIN = new ParticleType<>();
    public static ParticleType<?> SNEEZE = new ParticleType<>();
    public static ParticleType<?> CAMPFIRE_COSY_SMOKE = new ParticleType<>();
    public static ParticleType<?> CAMPFIRE_SIGNAL_SMOKE = new ParticleType<>();
    public static ParticleType<?> COMPOSTER = new ParticleType<>();
    public static ParticleType<?> FLASH = new ParticleType<>();
    public static ParticleType<?> FALLING_LAVA = new ParticleType<>();
    public static ParticleType<?> LANDING_LAVA = new ParticleType<>();
    public static ParticleType<?> FALLING_WATER = new ParticleType<>();
    public static ParticleType<?> DRIPPING_HONEY = new ParticleType<>();
    public static ParticleType<?> FALLING_HONEY = new ParticleType<>();
    public static ParticleType<?> LANDING_HONEY = new ParticleType<>();
    public static ParticleType<?> FALLING_NECTAR = new ParticleType<>();
    public static ParticleType<?> SOUL_FIRE_FLAME = new ParticleType<>();
    public static ParticleType<?> ASH = new ParticleType<>();
    public static ParticleType<?> CRIMSON_SPORE = new ParticleType<>();
    public static ParticleType<?> WARPED_SPORE = new ParticleType<>();
    public static ParticleType<?> SOUL = new ParticleType<>();
    public static ParticleType<?> DRIPPING_OBSIDIAN_TEAR = new ParticleType<>();
    public static ParticleType<?> FALLING_OBSIDIAN_TEAR = new ParticleType<>();
    public static ParticleType<?> LANDING_OBSIDIAN_TEAR = new ParticleType<>();
    public static ParticleType<?> REVERSE_PORTAL = new ParticleType<>();
    public static ParticleType<?> WHITE_ASH = new ParticleType<>();
    //TODO:
//    DUST_COLOR_TRANSITION(DustTransition.class) = new ParticleType<>();
    //TODO:
//    VIBRATION(Vibration.class) = new ParticleType<>();
    public static ParticleType<?> FALLING_SPORE_BLOSSOM = new ParticleType<>();
    public static ParticleType<?> SPORE_BLOSSOM_AIR = new ParticleType<>();
    public static ParticleType<?> SMALL_FLAME = new ParticleType<>();
    public static ParticleType<?> SNOWFLAKE = new ParticleType<>();
    public static ParticleType<?> DRIPPING_DRIPSTONE_LAVA = new ParticleType<>();
    public static ParticleType<?> FALLING_DRIPSTONE_LAVA = new ParticleType<>();
    public static ParticleType<?> DRIPPING_DRIPSTONE_WATER = new ParticleType<>();
    public static ParticleType<?> FALLING_DRIPSTONE_WATER = new ParticleType<>();
    public static ParticleType<?> GLOW_SQUID_INK = new ParticleType<>();
    public static ParticleType<?> GLOW = new ParticleType<>();
    public static ParticleType<?> WAX_ON = new ParticleType<>();
    public static ParticleType<?> WAX_OFF = new ParticleType<>();
    public static ParticleType<?> ELECTRIC_SPARK = new ParticleType<>();
    public static ParticleType<?> SCRAPE = new ParticleType<>();
    public static ParticleType<?> SONIC_BOOM = new ParticleType<>();
    public static ParticleType<?> SCULK_SOUL = new ParticleType<>();
    //TODO:
//    SCULK_CHARGE(Float.class) = new ParticleType<>();
    public static ParticleType<?> SCULK_CHARGE_POP = new ParticleType<>();
    //TODO:
//    SHRIEK(Integer.class) = new ParticleType<>();
    public static ParticleType<?> CHERRY_LEAVES = new ParticleType<>();
    public static ParticleType<?> EGG_CRACK = new ParticleType<>();
    ;

    private final Class<P> particleOption;

    ParticleType() {
        this(null);
    }

    ParticleType(Class<P> particleOption) {
        this.particleOption = particleOption;
    }

    public Particle createParticle() {
        return createParticle(null);
    }

    public Particle createParticle(final @Nullable P particleOption) {
        return new Particle(name(), particleOption);
    }
}
