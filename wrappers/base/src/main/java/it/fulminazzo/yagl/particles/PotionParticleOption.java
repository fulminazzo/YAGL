package it.fulminazzo.yagl.particles;

import it.fulminazzo.yagl.wrappers.Potion;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link LegacyParticleType#POTION_BREAK}.
 */
public class PotionParticleOption extends ParticleOption<Potion> {
    private final @NotNull Potion potion;

    public PotionParticleOption(@NotNull Potion potion) {
        this.potion = potion;
    }

    @Override
    public Potion getOption() {
        return this.potion;
    }
}
