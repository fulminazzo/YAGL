package it.fulminazzo.yagl.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A general class that represents a wrapper for another object.
 */
public abstract class Wrapper {

    /**
     * Gets the name of this object.
     *
     * @return the name
     */
    public abstract @NotNull String getName();

    /**
     * Compares this {@link Wrapper} with the given one.
     *
     * @param wrapper the wrapper
     * @return true, if they have the same name
     */
    public boolean isSimilar(final @Nullable Wrapper wrapper) {
        return wrapper != null && getClass().equals(wrapper.getClass()) && getName().equalsIgnoreCase(wrapper.getName());
    }

}
