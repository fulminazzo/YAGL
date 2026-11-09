package it.fulminazzo.yagl;

import it.fulminazzo.yagl.reflect.Reflect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * A special class that allows transforms a general class into a pseudo-enum implementation
 * by providing the common enum methods: {@link #name()} and {@link #ordinal()}.
 */
public abstract class ClassEnum {
    private static final @NotNull ClassValue<EnumRegistry> CACHE_MAP = new ClassValue<>() {

        @Override
        protected EnumRegistry computeValue(final @NotNull Class<?> type) {
            return new EnumRegistry(type);
        }

    };

    /**
     * Returns the index of the current object.
     *
     * @return the index
     */
    public int ordinal() {
        return getCache().ordinal(this);
    }

    /**
     * Gets the name of the current object.
     *
     * @return the name
     */
    public @Nullable String name() {
        return getCache().name(this);
    }

    private @NotNull EnumRegistry getCache() {
        return CACHE_MAP.get(getClass());
    }

    private static final class EnumRegistry {
        private final @NotNull LinkedList<ClassEnum> values;
        private final @NotNull Map<ClassEnum, String> nameMap;

        /**
         * Instantiates a new Enum registry.
         *
         * @param type the type of the class
         */
        public EnumRegistry(final @NotNull Class<?> type) {
            this.values = new LinkedList<>();
            this.nameMap = new HashMap<>();

            Reflect reflect = Reflect.on(type);
            reflect.getStaticFields().stream()
                    .filter(f -> f.getType().isAssignableFrom(type))
                    .forEach(f -> {
                        ClassEnum value = reflect.get(f).get();
                        values.add(value);
                        nameMap.put(value, f.getName());
                    });
        }

        /**
         * Gets the corresponding index of the value.
         *
         * @param value the value
         * @return the name
         */
        public int ordinal(final @NotNull ClassEnum value) {
            return values.indexOf(value);
        }

        /**
         * Gets the corresponding name of the value.
         *
         * @param value the value
         * @return the name
         */
        public @Nullable String name(final @NotNull ClassEnum value) {
            return nameMap.get(value);
        }

    }

}
