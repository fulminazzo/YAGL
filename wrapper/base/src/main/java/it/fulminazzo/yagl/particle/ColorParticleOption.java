package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.Color;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link LegacyParticleType#INSTANT_POTION_BREAK}.
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public final class ColorParticleOption extends ParticleOption<Color> {
    private final @NotNull Color color;

    /**
     * Instantiates a new Color particle option.
     *
     * @param color the color
     */
    public ColorParticleOption(final @NotNull Color color) {
        this.color = color;
    }

    @Override
    public @NotNull Color getOption() {
        return this.color;
    }

}
