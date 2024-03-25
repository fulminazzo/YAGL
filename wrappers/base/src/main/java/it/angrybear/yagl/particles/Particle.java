package it.angrybear.yagl.particles;

import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a general particle with an associated {@link ParticleOption}.
 */
@Getter
public class Particle {
    private final String type;
    private final ParticleOption<?> option;

    /**
     * Instantiates a new Particle.
     *
     * @param type   the type
     * @param option the option
     */
    Particle(final @NotNull String type, final @Nullable ParticleOption<?> option) {
        this.type = type;
        this.option = option;
    }

    /**
     * Gets option using {@link ParticleOption#getOption()}.
     *
     * @param <O> the type parameter
     * @return the option
     */
    @SuppressWarnings("unchecked")
    public <O> O getOption() {
        return this.option == null ? null : (O) this.option.getOption();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Particle) return ReflectionUtils.equalsFields(this, o);
        return super.equals(o);
    }
}
