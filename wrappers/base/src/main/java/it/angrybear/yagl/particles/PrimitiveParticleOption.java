package it.angrybear.yagl.particles;

import lombok.Getter;

/**
 * A {@link ParticleOption} capable of holding a single object.
 *
 * @param <T> the type parameter
 */
@Getter
public class PrimitiveParticleOption<T> extends ParticleOption {
    private final T value;

    /**
     * Instantiates a new Primitive particle option.
     *
     * @param value the value
     */
    public PrimitiveParticleOption(T value) {
        this.value = value;
    }
}
