package it.fulminazzo.yagl.particles;

/**
 * A {@link ParticleOption} capable of holding a single object.
 *
 * @param <T> the type parameter
 */
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
    public PrimitiveParticleOption(T value) {
        this.value = value;
    }

    @Override
    public T getOption() {
        return this.value;
    }
}
