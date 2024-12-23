package it.fulminazzo.yagl;

import it.fulminazzo.yagl.particles.Particle;
import it.fulminazzo.yagl.utils.EnumUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Since Minecraft <b>1.20.6</b>, {@link org.bukkit.Particle} have their official <b>Minecraft key</b> as name for the enum.
 * This class enables conversion between {@link Particle} and {@link org.bukkit.Particle} by converting {@link Particle#getType()}
 * to the names contained in this list.
 */
enum ParticleConverter {
    ASH("ASH"),
    BLOCK_CRACK("BLOCK"),
    BLOCK_DUST("BLOCK"),
    BLOCK_MARKER("BLOCK_MARKER"),
    BUBBLE_COLUMN_UP("BUBBLE_COLUMN_UP"),
    BUBBLE_POP("BUBBLE_POP"),
    CAMPFIRE_COSY_SMOKE("CAMPFIRE_COSY_SMOKE"),
    CAMPFIRE_SIGNAL_SMOKE("CAMPFIRE_SIGNAL_SMOKE"),
    CHERRY_LEAVES("CHERRY_LEAVES"),
    CLOUD("CLOUD"),
    COMPOSTER("COMPOSTER"),
    CRIMSON_SPORE("CRIMSON_SPORE"),
    CRIT("CRIT"),
    CRIT_MAGIC("ENCHANTED_HIT"),
    CURRENT_DOWN("CURRENT_DOWN"),
    DAMAGE_INDICATOR("DAMAGE_INDICATOR"),
    DOLPHIN("DOLPHIN"),
    DRAGON_BREATH("DRAGON_BREATH"),
    DRIPPING_DRIPSTONE_LAVA("DRIPPING_DRIPSTONE_LAVA"),
    DRIPPING_DRIPSTONE_WATER("DRIPPING_DRIPSTONE_WATER"),
    DRIPPING_HONEY("DRIPPING_HONEY"),
    DRIPPING_OBSIDIAN_TEAR("DRIPPING_OBSIDIAN_TEAR"),
    DRIP_LAVA("DRIPPING_LAVA"),
    DRIP_WATER("DRIPPING_WATER"),
    DUST_COLOR_TRANSITION("DUST_COLOR_TRANSITION"),
    DUST_PLUME("DUST_PLUME"),
    EGG_CRACK("EGG_CRACK"),
    ELECTRIC_SPARK("ELECTRIC_SPARK"),
    ENCHANTMENT_TABLE("ENCHANT"),
    END_ROD("END_ROD"),
    EXPLOSION_HUGE("EXPLOSION_EMITTER"),
    EXPLOSION_LARGE("EXPLOSION"),
    EXPLOSION_NORMAL("POOF"),
    FALLING_DRIPSTONE_LAVA("FALLING_DRIPSTONE_LAVA"),
    FALLING_DRIPSTONE_WATER("FALLING_DRIPSTONE_WATER"),
    FALLING_DUST("FALLING_DUST"),
    FALLING_HONEY("FALLING_HONEY"),
    FALLING_LAVA("FALLING_LAVA"),
    FALLING_NECTAR("FALLING_NECTAR"),
    FALLING_OBSIDIAN_TEAR("FALLING_OBSIDIAN_TEAR"),
    FALLING_SPORE_BLOSSOM("FALLING_SPORE_BLOSSOM"),
    FALLING_WATER("FALLING_WATER"),
    FIREWORKS_SPARK("FIREWORK"),
    FLAME("FLAME"),
    FLASH("FLASH"),
    GLOW("GLOW"),
    GLOW_SQUID_INK("GLOW_SQUID_INK"),
    GUST("GUST"),
    GUST_DUST("GUST_DUST"),
    GUST_EMITTER("GUST_EMITTER"),
    HEART("HEART"),
    ITEM_CRACK("ITEM"),
    LANDING_HONEY("LANDING_HONEY"),
    LANDING_LAVA("LANDING_LAVA"),
    LANDING_OBSIDIAN_TEAR("LANDING_OBSIDIAN_TEAR"),
    LAVA("LAVA"),
    MOB_APPEARANCE("ELDER_GUARDIAN"),
    NAUTILUS("NAUTILUS"),
    NOTE("NOTE"),
    PORTAL("PORTAL"),
    REDSTONE("DUST"),
    REVERSE_PORTAL("REVERSE_PORTAL"),
    SCRAPE("SCRAPE"),
    SCULK_CHARGE("SCULK_CHARGE"),
    SCULK_CHARGE_POP("SCULK_CHARGE_POP"),
    SCULK_SOUL("SCULK_SOUL"),
    SHRIEK("SHRIEK"),
    SLIME("ITEM_SLIME"),
    SMALL_FLAME("SMALL_FLAME"),
    SMOKE_LARGE("LARGE_SMOKE"),
    SMOKE_NORMAL("SMOKE"),
    SNEEZE("SNEEZE"),
    SNOWBALL("ITEM_SNOWBALL"),
    SNOWFLAKE("SNOWFLAKE"),
    SNOW_SHOVEL("ITEM_SNOWBALL"),
    SONIC_BOOM("SONIC_BOOM"),
    SOUL("SOUL"),
    SOUL_FIRE_FLAME("SOUL_FIRE_FLAME"),
    SPELL("EFFECT"),
    SPELL_INSTANT("INSTANT_EFFECT"),
    SPELL_MOB("ENTITY_EFFECT"),
    SPELL_MOB_AMBIENT("AMBIENT_ENTITY_EFFECT"),
    SPELL_WITCH("WITCH"),
    SPIT("SPIT"),
    SPORE_BLOSSOM_AIR("SPORE_BLOSSOM_AIR"),
    SQUID_INK("SQUID_INK"),
    SUSPENDED("UNDERWATER"),
    SUSPENDED_DEPTH("UNDERWATER"),
    SWEEP_ATTACK("SWEEP_ATTACK"),
    TOTEM("TOTEM_OF_UNDYING"),
    TOWN_AURA("MYCELIUM"),
    VIBRATION("VIBRATION"),
    VILLAGER_ANGRY("ANGRY_VILLAGER"),
    VILLAGER_HAPPY("HAPPY_VILLAGER"),
    WARPED_SPORE("WARPED_SPORE"),
    WATER_BUBBLE("BUBBLE"),
    WATER_DROP("RAIN"),
    WATER_SPLASH("SPLASH"),
    WATER_WAKE("FISHING"),
    WAX_OFF("WAX_OFF"),
    WAX_ON("WAX_ON"),
    WHITE_ASH("WHITE_ASH"),
    WHITE_SMOKE("WHITE_SMOKE"),
    ;

    private final String particleName;

    ParticleConverter(final @NotNull String particleName) {
        this.particleName = particleName;
    }

    /**
     * Converts the given {@link Particle} to a {@link org.bukkit.Particle}.
     * If it fails one time, it will try to convert the type using the fields contained in this enum.
     * If it fails again it throws a {@link IllegalArgumentException}.
     *
     * @param particle the particle
     * @return the converted particle
     */
    public static org.bukkit.Particle convertToBukkit(final @NotNull Particle particle) {
        try {
            return EnumUtils.valueOf(org.bukkit.Particle.class, particle.getType());
        } catch (IllegalArgumentException e) {
            try {
                ParticleConverter converter = ParticleConverter.valueOf(particle.getType());
                return EnumUtils.valueOf(org.bukkit.Particle.class, converter.particleName);
            } catch (IllegalArgumentException ex) {
                throw e;
            }
        }
    }

}
