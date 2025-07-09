package it.fulminazzo.yagl;

import it.fulminazzo.yagl.particle.Particle;
import it.fulminazzo.yagl.util.EnumUtils;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Since Minecraft <b>1.20.6</b>, {@link org.bukkit.Particle} have their official <b>Minecraft key</b> as name for the enum.
 * This class enables conversion between {@link Particle} and {@link org.bukkit.Particle} by converting {@link Particle#getType()}
 * to the names contained in this list.
 */
enum ParticleConverter {
    BLOCK_CRACK("BLOCK"),
    BLOCK_DUST("BLOCK"),
    CRIT_MAGIC("ENCHANTED_HIT"),
    DRIP_LAVA("DRIPPING_LAVA"),
    DRIP_WATER("DRIPPING_WATER"),
    ENCHANTMENT_TABLE("ENCHANT"),
    EXPLOSION_HUGE("EXPLOSION_EMITTER"),
    EXPLOSION_LARGE("EXPLOSION"),
    EXPLOSION_NORMAL("POOF"),
    FIREWORKS_SPARK("FIREWORK"),
    ITEM_CRACK("ITEM"),
    MOB_APPEARANCE("ELDER_GUARDIAN"),
    REDSTONE("DUST"),
    SLIME("ITEM_SLIME"),
    SMOKE_LARGE("LARGE_SMOKE"),
    SMOKE_NORMAL("SMOKE"),
    SNOWBALL("ITEM_SNOWBALL"),
    SNOW_SHOVEL("ITEM_SNOWBALL"),
    SPELL("EFFECT"),
    SPELL_INSTANT("INSTANT_EFFECT"),
    SPELL_MOB("ENTITY_EFFECT"),
    SPELL_MOB_AMBIENT("AMBIENT_ENTITY_EFFECT"),
    SPELL_WITCH("WITCH"),
    SUSPENDED("UNDERWATER"),
    SUSPENDED_DEPTH("UNDERWATER"),
    TOTEM("TOTEM_OF_UNDYING"),
    TOWN_AURA("MYCELIUM"),
    VILLAGER_ANGRY("ANGRY_VILLAGER"),
    VILLAGER_HAPPY("HAPPY_VILLAGER"),
    WATER_BUBBLE("BUBBLE"),
    WATER_DROP("RAIN"),
    WATER_SPLASH("SPLASH"),
    WATER_WAKE("FISHING"),
    ;

    @Getter(AccessLevel.PACKAGE)
    private final @NotNull String particleName;

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
    public static @NotNull Object convertToBukkit(final @NotNull Particle particle) {
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
