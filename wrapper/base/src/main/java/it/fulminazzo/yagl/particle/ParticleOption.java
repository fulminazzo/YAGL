package it.fulminazzo.yagl.particle;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A general option usually bound to a particle.
 * This can either be an {@code int}, a {@code float} or even a fully-fledged object.
 *
 * @param <O> the type of the option
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class ParticleOption<O> {

    /**
     * Gets the option needed from the particle.
     *
     * @return the option
     */
    public abstract O getOption();

}
