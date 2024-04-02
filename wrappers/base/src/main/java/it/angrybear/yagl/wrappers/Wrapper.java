package it.angrybear.yagl.wrappers;

import it.fulminazzo.fulmicollection.objects.Printable;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.jetbrains.annotations.Nullable;

/**
 * A general class that represents a wrapper for another object.
 */
public abstract class Wrapper {

    /**
     * Gets name.
     *
     * @return the name
     */
    public abstract String getName();

    /**
     * Compare this {@link Wrapper} with the given one
     *
     * @param wrapper the wrapper
     * @return true, if they have the same name
     */
    public boolean isSimilar(final @Nullable Wrapper wrapper) {
        if (wrapper == null) return false;
        return getClass().equals(wrapper.getClass()) && getName().equalsIgnoreCase(wrapper.getName());
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public abstract String getName();

    /**
     * Compare this {@link Wrapper} with the given one
     *
     * @param wrapper the wrapper
     * @return true, if they have the same name
     */
    public boolean isSimilar(final @Nullable Wrapper wrapper) {
        if (wrapper == null) return false;
        return getClass().equals(wrapper.getClass()) && getName().equalsIgnoreCase(wrapper.getName());
    }

    /**
     * Compare this wrapper with the given one.
     *
     * @param wrapper the wrapper
     * @return true if they are equal
     */
    public boolean equals(final @Nullable Wrapper wrapper) {
        if (wrapper == null) return false;
        return getClass().equals(wrapper.getClass()) && ReflectionUtils.equalsFields(this, wrapper);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Wrapper) return equals((Wrapper) o);
        return super.equals(o);
    }

    @Override
    public String toString() {
        return Printable.convertToJson(this);
    }
}
