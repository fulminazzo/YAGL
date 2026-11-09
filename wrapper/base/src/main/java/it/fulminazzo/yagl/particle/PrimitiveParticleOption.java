package it.fulminazzo.yagl.particle;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A {@link ParticleOption} capable of holding a single object.
 *
 * @param <T> the type parameter
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public final class PrimitiveParticleOption<T> extends ParticleOption<T> {
    private final T value;

    @SuppressWarnings("unused")
    private PrimitiveParticleOption() {
        this(null);
    }

    /**
     * Instantiates a new Primitive particle option.
     *
     * @param value the value
     */
    public PrimitiveParticleOption(final T value) {
        this.value = value;
    }

    @Override
    public T getOption() {
        return this.value;
    }

}
