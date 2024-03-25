package it.angrybear.yagl.particles;

import it.angrybear.yagl.Color;
import it.fulminazzo.fulmicollection.structures.Tuple;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link ParticleType#REDSTONE}.
 */
@Getter
public class DustParticleOption extends ParticleOption<Tuple<Color, Float>> {
    private final Color color;
    private final float size;

    private DustParticleOption() {
        this(null, -1);
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
    public Tuple<Color, Float> getOption() {
        return new Tuple<>(this.color, this.size);
    }
}
