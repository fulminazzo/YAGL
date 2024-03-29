package it.angrybear.yagl.particles;

import it.angrybear.yagl.Color;
import it.fulminazzo.fulmicollection.structures.Triple;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link ParticleType#DUST_COLOR_TRANSITION}.
 */
public class DustTransitionParticleOption extends ParticleOption<Triple<Color, Color, Float>> {
    private final Color from;
    private final Color to;
    private final float size;

    @SuppressWarnings("unused")
    private DustTransitionParticleOption() {
        this(null, null, -1);
    }

    /**
     * Instantiates a new Dust transition particle option.
     *
     * @param from  the from
     * @param color the color
     * @param size  the size
     */
    public DustTransitionParticleOption(final @NotNull Color from, final @NotNull Color color, final float size) {
        this.from = from;
        to = color;
        this.size = size;
    }

    @Override
    public Triple<Color, Color, Float> getOption() {
        return new Triple<>(this.from, this.to, this.size);
    }
}
