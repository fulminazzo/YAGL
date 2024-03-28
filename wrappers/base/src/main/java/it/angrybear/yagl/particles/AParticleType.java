package it.angrybear.yagl.particles;

import it.angrybear.yagl.ClassEnum;
import it.fulminazzo.fulmicollection.objects.Refl;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A general class used as a backend for {@link ParticleType} and {@link LegacyParticleType}.
 *
 * @param <P> the type parameter
 */
abstract class AParticleType<P extends ParticleOption<?>> extends ClassEnum {

    @Getter(AccessLevel.PACKAGE)
    private final Class<P> optionType;

    /**
     * Instantiates a new particle type.
     *
     * @param optionType the {@link ParticleOption} type
     */
    AParticleType(Class<P> optionType) {
        this.optionType = optionType;
    }

    /**
     * Creates a {@link Particle} with no {@link ParticleOption} specified.
     *
     * @return the particle
     */
    public @NotNull Particle create() {
        return create((P) null);
    }

    /**
     * Creates a {@link Particle} using the {@link ParticleOption} in {@link #optionType}.
     * The parameters passed will be forwarded to the {@link ParticleOption} constructor.
     * <b>NOTE:</b> they are NOT checked, therefore it is up to the user to provide the correct ones.
     *
     * @param parameters the parameters
     * @return the particle
     */
    public @NotNull Particle create(Object @NotNull ... parameters) {
        if (parameters.length == 0) return create();
        else return create(new Refl<>(getOptionType(), parameters).getObject());
    }

    /**
     * Creates a {@link Particle} with the {@link ParticleOption} in {@link #optionType}.
     * If null, this method might accept the passed parameter, but it will be ignored upon adaptation.
     *
     * @param particleOption the particle option
     * @return the particle
     */
    public @NotNull Particle create(final @Nullable P particleOption) {
        return new Particle(name(), particleOption);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Particle) return name().equalsIgnoreCase(((Particle) o).getType());
        return super.equals(o);
    }
}
