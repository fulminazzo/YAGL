package it.fulminazzo.yagl.testing;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a wrapper for an object.
 * This is required for Minecraft version 1.20.6+ tests since
 * many objects are not an abstract classes anymore.
 *
 * @param <O> the type parameter
 */
public abstract class Wrapper<O> {
    protected final Refl<O> internalObject;

    /**
     * Instantiates a new Wrapper.
     *
     * @param clazz      the clazz
     * @param parameters the parameters
     */
    public Wrapper(final @NotNull Class<O> clazz, final Object @NotNull ... parameters) {
        this(new Refl<>(clazz, parameters));
    }

    /**
     * Instantiates a new Wrapper.
     *
     * @param internalObject the internal object
     */
    public Wrapper(final @NotNull Refl<O> internalObject) {
        this.internalObject = internalObject;
    }

    /**
     * Gets the wrapped object.
     *
     * @param <T> the type to convert the internal object to
     * @return the object
     */
    @SuppressWarnings("unchecked")
    public @NotNull <T> T getWrapped() {
        return (T) this.internalObject.getObject();
    }

}
