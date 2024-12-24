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
     * @param clazz the clazz
     * @param parameters  the parameters
     */
    protected Wrapper(final @NotNull Class<O> clazz, final Object @NotNull ... parameters) {
        this.internalObject = new Refl<>(clazz, parameters);
    }

    /**
     * Gets the wrapped object.
     *
     * @return the object
     */
    public @NotNull O getWrapped() {
        return this.internalObject.getObject();
    }

}
