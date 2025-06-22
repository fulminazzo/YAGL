package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.wrapper.Potion;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link LegacyParticleType#POTION_BREAK}.
 */
public final class PotionParticleOption extends ParticleOption<Potion> {
    private final @NotNull Potion potion;

    public PotionParticleOption(@NotNull Potion potion) {
        this.potion = potion;
    }

    @Override
    public Potion getOption() {
        return this.potion;
    }
}
