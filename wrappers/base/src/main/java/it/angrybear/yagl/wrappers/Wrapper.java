package it.angrybear.yagl.wrappers;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import it.fulminazzo.fulmicollection.objects.Printable;
import org.jetbrains.annotations.Nullable;

/**
 * A general class that represents a wrapper for another object.
 */
public abstract class Wrapper extends FieldEquable {

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

    @Override
    public String toString() {
        return Printable.convertToJson(this);
    }
}
