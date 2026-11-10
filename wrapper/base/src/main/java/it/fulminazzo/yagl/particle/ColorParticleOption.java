package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.Color;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link LegacyParticleType#INSTANT_POTION_BREAK}.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class ColorParticleOption extends ParticleOption<Color> {
    private final @NotNull Color color;

    @Override
    public @NotNull Color getOption() {
        return this.color;
    }

}
