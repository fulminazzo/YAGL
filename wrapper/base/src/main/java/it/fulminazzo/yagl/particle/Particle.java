package it.fulminazzo.yagl.particle;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a general particle with an associated {@link ParticleOption}.
 */
@Getter
public final class Particle extends FieldEquable {
    private final @NotNull String type;
    private final @Nullable ParticleOption<?> option;

    /**
     * Instantiates a new Particle.
     *
     * @param type   the type
     * @param option the option
     */
    Particle(final @NotNull String type, final @Nullable ParticleOption<?> option) {
        this.type = type;
        this.option = option;
    }

    /**
     * Gets option using {@link ParticleOption#getOption()}.
     *
     * @param <O> the type parameter
     * @return the option
     */
    @SuppressWarnings("unchecked")
    public <O> @Nullable O getOption() {
        return this.option == null ? null : (O) this.option.getOption();
    }
}
