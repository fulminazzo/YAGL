package it.angrybear.yagl.particles;

import it.angrybear.yagl.Color;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link LegacyParticleType#INSTANT_POTION_BREAK}.
 */
public class ColorParticleOption extends ParticleOption<Color> {
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
    public Color getOption() {
        return this.color;
    }
}
