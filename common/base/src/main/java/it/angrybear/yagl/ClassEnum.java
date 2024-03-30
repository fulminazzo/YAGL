package it.angrybear.yagl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

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
     * Gets the object from its index, returned by {@link #ordinal()}..
     *
     * @param <T>   the type parameter
     * @param index the index
     * @param clazz the clazz
     * @return the t
     */
    protected static <T extends ClassEnum> T valueOf(final @Range(from = 0, to = Integer.MAX_VALUE) int index,
                                                     final @NotNull Class<T> clazz) {
        return values(clazz)[index];
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

    private static class EnumCache<T extends ClassEnum> {
        private final Class<T> clazz;
        private final Map<String, T> values;
        private int ordinal;

        private EnumCache(Class<T> clazz) {
            this.clazz = clazz;
            this.values = new LinkedHashMap<>();
            this.ordinal = 0;
        }

        public int getAndIncrementOrdinal() {
            return this.ordinal++;
        }
    }
}
