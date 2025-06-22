package it.fulminazzo.yagl.particles;

import it.fulminazzo.yagl.Color;
import it.fulminazzo.fulmicollection.structures.tuples.Triple;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link ParticleType#DUST_COLOR_TRANSITION}.
 */
public final class DustTransitionParticleOption extends ParticleOption<Triple<Color, Color, Float>> {
    private final Color from;
    private final Color to;
    private final float size;

    @SuppressWarnings("unused")
    private DustTransitionParticleOption() {
        this(Color.BLACK, Color.BLACK, -1);
    }

    /**
     * Instantiates a new Dust transition particle option.
     *
     * @param from  the color to start from
     * @param to the color to end with
     * @param size  the size
     */
    public DustTransitionParticleOption(final @NotNull Color from, final @NotNull Color to, final float size) {
        this.from = from;
        this.to = to;
        this.size = size;
    }

    @Override
    public Triple<Color, Color, Float> getOption() {
        return new Triple<>(this.from, this.to, this.size);
    }
}
