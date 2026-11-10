package it.fulminazzo.yagl.particle;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A {@link ParticleOption} capable of holding a single object.
 *
 * @param <T> the type of the object
 */
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class PrimitiveParticleOption<T> extends ParticleOption<T> {
    private final T value;

    @SuppressWarnings("unused")
    private PrimitiveParticleOption() {
        this(null);
    }

    @Override
    public T getOption() {
        return this.value;
    }

}
