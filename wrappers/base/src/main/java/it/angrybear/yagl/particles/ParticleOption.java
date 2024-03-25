package it.angrybear.yagl.particles;

import it.fulminazzo.fulmicollection.utils.ReflectionUtils;

/**
 * Represents a general option for a particle.
 * This can either be an integer, float, or a full-fledged object.
 *
 * @param <O> the type parameter
 */
abstract class ParticleOption<O> {

    /**
     * Gets option to send the particle.
     *
     * @return the option
     */
    public abstract O getOption();

    @Override
    public boolean equals(Object o) {
        if (o instanceof ParticleOption) return ReflectionUtils.equalsFields(this, o);
        return super.equals(o);
    }
}
