package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.Color;
import it.fulminazzo.yagl.structure.Pair;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link ParticleType#REDSTONE}.
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public final class DustParticleOption extends ParticleOption<Pair<Color, Float>> {
    private final @NotNull Color color;
    private final float size;

    @SuppressWarnings("unused")
    private DustParticleOption() {
        this(Color.BLACK, -1);
    }

    /**
     * Instantiates a new Dust particle option.
     *
     * @param color the color
     * @param size  the size
     */
    public DustParticleOption(final @NotNull Color color, final float size) {
        this.color = color;
        this.size = size;
    }

    @Override
    public @NotNull Pair<Color, Float> getOption() {
        return new Pair<>(this.color, this.size);
    }

}
