package it.angrybear.yagl.particles;

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
}
