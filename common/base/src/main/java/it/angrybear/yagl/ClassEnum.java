package it.angrybear.yagl;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * An abstract class that allows to "convert" classes to enums by providing the most three common methods:
 * {@link #name()}, {@link #values(Class)} and {@link #valueOf(String, Class)}.
 */
@SuppressWarnings("unchecked")
public abstract class ClassEnum {
    private static final Map<Class<?>, Integer> CLASSES_ORDINALS = new HashMap<>();
    private final int index;

    /**
     * Instantiates a new Class enum.
     */
    public ClassEnum() {
        final Class<?> clazz = getClass();
        this.index = CLASSES_ORDINALS.getOrDefault(clazz, 0);
        CLASSES_ORDINALS.put(clazz, this.index + 1);
    }

    /**
     * Returns the index of the current object.
     *
     * @return the index
     */
    public int ordinal() {
        return this.index;
    }

    /**
     * Gets the name of the current object.
     *
     * @return the name
     */
    public String name() {
        Class<?> clazz = getClass();
        for (Field field : clazz.getDeclaredFields())
            if (field.getType().equals(clazz))
                try {
                    Object o = field.get(clazz);
                    if (o.equals(this)) return field.getName();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
        throw new IllegalStateException("Unreachable code");
    }

    /**
     * Gets the object from its name.
     *
     * @param <T>   the type parameter
     * @param name  the name
     * @param clazz the clazz
     * @return the object
     */
    protected static <T extends ClassEnum> T valueOf(final @NotNull String name, final @NotNull Class<T> clazz) {
        for (Field field : clazz.getDeclaredFields())
            if (field.getType().equals(clazz))
                try {
                    field.setAccessible(true);
                    T t = (T) field.get(clazz);
                    if (t.name().equalsIgnoreCase(name)) return t;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
        return null;
    }

    /**
     * Gets all the object in an array.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the objects
     */
    protected static <T extends ClassEnum> T[] values(final @NotNull Class<T> clazz) {
        List<T> types = new LinkedList<>();
        for (Field field : clazz.getDeclaredFields())
            if (field.getType().equals(clazz))
                try {
                    field.setAccessible(true);
                    types.add((T) field.get(clazz));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
        T[] ts = (T[]) Array.newInstance(clazz, types.size());
        for (int i = 0; i < types.size(); i++)
            ts[i] = types.get(i);
        return ts;
    }
}
