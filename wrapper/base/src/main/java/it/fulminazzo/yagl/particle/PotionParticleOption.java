package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.wrapper.Potion;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link LegacyParticleType#POTION_BREAK}.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class PotionParticleOption extends ParticleOption<Potion> {
    private final @NotNull Potion potion;

    @Override
    public @NotNull Potion getOption() {
        return this.potion;
    }

}
