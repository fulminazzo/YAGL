package it.fulminazzo.yagl;

import it.fulminazzo.yagl.reflect.Reflect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.lang.reflect.Array;
import java.util.*;

/**
 * A special class that allows transforms a general class into a pseudo-enum implementation
 * by providing the common enum methods: {@link #name()} and {@link #ordinal()}.
 */
@SuppressWarnings("unchecked")
public abstract class ClassEnum {
    private static final @NotNull ClassValue<EnumRegistry<? extends ClassEnum>> CACHE_MAP = new ClassValue<>() {

        @Override
        protected EnumRegistry<?> computeValue(final @NotNull Class<?> type) {
            return new EnumRegistry<>(type);
        }

    };

    /**
     * Returns the index of the current object.
     *
     * @return the index
     */
    public @Range(from = 0, to = Integer.MAX_VALUE) int ordinal() {
        return getCache().ordinal(this);
    }

    /**
     * Gets the name of the current object.
     * Can be {@code null} if the value is not declared in the same class as a static field.
     *
     * @return the name
     */
    public String name() {
        return getCache().name(this);
    }

    /**
     * Support function for implementing the {@code valueOf(String)} static function for all {@code ClassEnum} subclasses.
     *
     * @param index the ordinal of the enum constant
     * @param type  the type of the enum constant
     * @param <E>   the type of the class enum
     * @return the enum constant
     */
    protected static <E extends ClassEnum> @NotNull E valueOf(final @Range(from = 0, to = Integer.MAX_VALUE) int index,
                                                              final @NotNull Class<E> type) {
        return getCache(type).valueOf(index);
    }

    /**
     * Support function for implementing the {@code valueOf(String)} static function for all {@code ClassEnum} subclasses.
     *
     * @param name the name of the enum constant
     * @param type the type of the enum constant
     * @param <E>  the type of the class enum
     * @return the enum constant
     */
    protected static <E extends ClassEnum> @NotNull E valueOf(final @NotNull String name, final @NotNull Class<E> type) {
        return getCache(type).valueOf(name);
    }

    /**
     *
     * Support function for implementing the {@code values()} static function for all {@code ClassEnum} subclasses.
     *
     * @param type the type of the enum constants
     * @param <E>  the type of the class enum
     * @return the enum constants
     */
    protected static <E extends ClassEnum> E @NotNull [] values(final @NotNull Class<E> type) {
        return getCache(type).values();
    }

    private <E extends ClassEnum> @NotNull EnumRegistry<E> getCache() {
        return (EnumRegistry<E>) getCache(getClass());
    }

    private static <E extends ClassEnum> @NotNull EnumRegistry<E> getCache(final @NotNull Class<E> type) {
        return (EnumRegistry<E>) CACHE_MAP.get(type);
    }

    private static final class EnumRegistry<E extends ClassEnum> {
        private final @NotNull Class<E> type;
        private final @NotNull List<E> values;
        private final @NotNull Map<E, String> valueNameMap;
        private final @NotNull Map<String, E> nameValueMap;

        /**
         * Instantiates a new Enum registry.
         *
         * @param type the type of the class
         */
        public EnumRegistry(final Class<?> type) {
            this.type = (Class<E>) type;
            this.values = new ArrayList<>();
            this.valueNameMap = new IdentityHashMap<>();
            this.nameValueMap = new HashMap<>();

            Reflect reflect = Reflect.on(type);
            reflect.getStaticFields().stream()
                    .filter(f -> f.getType().isAssignableFrom(type))
                    .forEach(f -> {
                        E value = reflect.get(f).get();
                        values.add(value);
                        String fieldName = f.getName().toUpperCase(Locale.ROOT);
                        valueNameMap.put(value, fieldName);
                        nameValueMap.put(fieldName, value);
                    });
        }

        /**
         * Gets the corresponding index of the value.
         *
         * @param value the value
         * @return the name
         */
        public @Range(from = 0, to = Integer.MAX_VALUE) int ordinal(final @NotNull E value) {
            return values.indexOf(value);
        }

        /**
         * Gets the corresponding name of the value.
         *
         * @param value the value
         * @return the name
         */
        public @NotNull String name(final @NotNull E value) {
            String name = valueNameMap.get(value);
            if (name == null)
                throw new IllegalArgumentException("No enum name found for " + value);
            return name;
        }

        public @NotNull E valueOf(final @Range(from = 0, to = Integer.MAX_VALUE) int index) {
            try {
                return values.get(index);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("No enum constant " + type.getName() + " has ordinal " + index);
            }
        }

        public @NotNull E valueOf(final @NotNull String name) {
            String fieldName = name.toUpperCase(Locale.ROOT);
            if (nameValueMap.containsKey(fieldName)) return nameValueMap.get(fieldName);
            else throw new IllegalArgumentException("No enum constant " + type.getName() + "." + name);
        }

        public E @NotNull [] values() {
            return values.toArray(s -> (E[]) Array.newInstance(type, s));
        }

    }

}
