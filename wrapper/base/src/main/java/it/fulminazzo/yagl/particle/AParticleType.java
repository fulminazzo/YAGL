package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.ClassEnum;
import it.fulminazzo.yagl.reflect.Reflect;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A general class used as a backend for {@link ParticleType} and {@link LegacyParticleType}.
 *
 * @param <P> the type of the supported option
 */
abstract class AParticleType<P extends ParticleOption<?>> extends ClassEnum {
    @Getter(AccessLevel.PACKAGE)
    private final Class<P> optionType;

    /**
     * Instantiates a new particle type.
     *
     * @param optionType the {@link ParticleOption} type
     */
    AParticleType(final @NotNull Class<P> optionType) {
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
     * <b>NOTE:</b> they are NOT checked; it is up to the user to provide the correct ones.
     *
     * @param parameters the parameters
     * @return the particle
     */
    public @NotNull Particle create(final Object @NotNull ... parameters) {
        if (parameters.length == 0) return create();
        else {
            P option = Reflect.on(getOptionType()).init(parameters).get();
            return create(option);
        }
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
    public boolean equals(final @Nullable Object other) {
        if (other == null || !other.getClass().equals(getClass())) return false;
        AParticleType<?> otherType = (AParticleType<?>) other;
        return otherType.name().equals(name()) && Objects.equals(otherType.getOptionType(), getOptionType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name(), getOptionType());
    }

    @Override
    public @NotNull String toString() {
        return name();
    }

}
