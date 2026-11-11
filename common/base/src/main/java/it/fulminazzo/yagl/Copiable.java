package it.fulminazzo.yagl;

import it.fulminazzo.yagl.reflect.Reflect;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;

/**
 * Marker abstract class for objects that can be copied.
 *
 * @param <C> the type parameter to identify this object
 */
public abstract class Copiable<C> {

    /**
     * Copies the current object data to the provided copy.
     *
     * @param copy the object to copy data to
     * @param <T>  the type of the copy
     * @return the copy updated
     */
    protected abstract <T extends C> @NotNull T copyDataTo(final @NotNull T copy);

    /**
     * Creates a new copy of this object.
     *
     * @return the copy
     */
    @SuppressWarnings("unchecked")
    public @NotNull C copy() {
        return copy((Class<C>) getClass());
    }

    /**
     * Copies the current object to a new one by initializing the provided class.
     * The class should be either the same class or an inheritor of the copying object type.
     * If an interface is provided, the suffix "Impl" will be appended to look for the implementation class.
     * If no suitable implementation is found, a {@link it.fulminazzo.yagl.reflect.ReflectException} will be thrown.
     * <br>
     * <b>WARNING</b>: The provided class must have a default no-argument constructor.
     *
     * @param type the type of the copy
     * @param <T>  the type of the copy
     * @return the copy
     */
    public <T extends C> @NotNull T copy(final @NotNull Class<T> type) {
        String canonicalName = type.getCanonicalName();
        if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) canonicalName += "Impl";
        T copy = Reflect.on(canonicalName).init().get();
        return copyDataTo(copy);
    }

}
