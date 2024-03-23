package it.angrybear.yagl.particles;

/**
 * A {@link ParticleOption} capable of holding a single object.
 *
 * @param <T> the type parameter
 */
public class PrimitiveParticleOption<T> extends ParticleOption<T> {
    private final T value;

    /**
     * Instantiates a new Primitive particle option.
     *
     * @param value the value
     */
    public PrimitiveParticleOption(T value) {
        this.value = value;
    }

    @Override
    public T getOption() {
        return this.value;
    }
}
