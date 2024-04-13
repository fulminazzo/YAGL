package it.angrybear.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * An interface that holds key-value pairs of {@link String}s.
 */
public interface Metadatable {
    /**
     * A function used to convert the variables in {@link #apply(Object)}.
     */
    Function<@NotNull String, @NotNull String> VARIABLE_FORMAT = s -> "<" + s + ">";
    /**
     * A function used to parse the variables names.
     */
    Function<@NotNull String, @NotNull String> VARIABLE_PARSER = s -> {
        final String separator = "_";

        StringBuilder builder = new StringBuilder();
        for (String string : s.split("")) {
            if (string.matches("[A-Z]")) builder.append(separator);
            else if (string.matches("[\r\t\n -]")) {
                builder.append(separator);
                continue;
            }
            builder.append(string);
        }

        String output = builder.toString().toLowerCase();
        while (output.startsWith(separator))
            output = output.substring(separator.length());
        return output;
    };

    /**
     * Sets variable.
     *
     * @param name  the name
     * @param value the value
     * @return this metadatable
     */
    default @NotNull Metadatable setVariable(final @NotNull String name, final @NotNull String value) {
        variables().put(VARIABLE_PARSER.apply(name), value);
        return this;
    }

    /**
     * Unset variable metadatable.
     *
     * @param name the name
     * @return this metadatable
     */
    default @NotNull Metadatable unsetVariable(final @NotNull String name) {
        variables().remove(VARIABLE_PARSER.apply(name));
        return this;
    }

    /**
     * Checks if the current {@link Metadatable} has the given variable.
     *
     * @param name the name
     * @return true if it contains
     */
    default boolean hasVariable(final @NotNull String name) {
        return getVariable(VARIABLE_PARSER.apply(name)) != null;
    }

    /**
     * Gets variable.
     * Returns <i>null</i> in case of missing.
     *
     * @param name the name
     * @return the variable
     */
    default @Nullable String getVariable(final @NotNull String name) {
        return variables().get(VARIABLE_PARSER.apply(name));
    }

    /**
     * Copies all the variables from this metadatable to the given one.
     *
     * @param other   the other metadatable
     * @param replace if false, if the other already has the variable, it will not be replaced
     * @return this metadatable
     */
    default @NotNull Metadatable copyAll(final @NotNull Metadatable other, final boolean replace) {
        variables().forEach((k, v) -> {
            if (!other.hasVariable(k) || replace) other.setVariable(k, v);
        });
        return this;
    }

    /**
     * Uses {@link #copyAll(Metadatable, boolean)} to copy from the given {@link Metadatable} to this one.
     *
     * @param other   the other metadatable
     * @param replace if false, if this already has the variable, it will not be replaced
     * @return this metadatable
     */
    default @NotNull Metadatable copyFrom(final @NotNull Metadatable other, final boolean replace) {
        other.copyAll(this, replace);
        return this;
    }

    /**
     * Applies all the current variables to the given object fields,
     * by replacing in every string all the variables in the format {@link #VARIABLE_FORMAT}.
     *
     * @param <T>    the type parameter
     * @param object the object
     * @return the object parsed
     */
    @SuppressWarnings("unchecked")
    default <T> T apply(final T object) {
        if (object == null) return null;
        else if (object instanceof String) return (T) apply((String) object);
        else if (object instanceof Collection) return (T) apply((Collection<Object>) object);
        else if (object instanceof Map) return (T) apply((Map<Object, Object>) object);
        else if (!ReflectionUtils.isPrimitiveOrWrapper(object.getClass())) {
            final Refl<T> refl = new Refl<>(object);
            for (Field field : refl.getNonStaticFields()) {
                // Skip metadatable variables
                if (field.getName().equals("variables")) continue;
                Object o = refl.getFieldObject(field);
                refl.setFieldObject(field, apply(o));
            }
        }
        return object;
    }

    /**
     * Applies all the current variables to the given string.
     *
     * @param string the string
     * @return the string parsed
     */
    default String apply(@NotNull String string) {
        for (final String v : variables().keySet()) {
            final String s = getVariable(v);
            if (s == null) continue;
            final Matcher matcher = Pattern.compile("([^\\\\]|^)" + VARIABLE_FORMAT.apply(v)).matcher(string);
            while (matcher.find())
                string = string.substring(0, matcher.start()) + matcher.group(1) + s +
                        string.substring(matcher.end());
        }
        return string;
    }

    /**
     * Applies all the current variables to the given collection.
     *
     * @param collection the collection
     * @return the collection parsed
     */
    @SuppressWarnings("unchecked")
    default @NotNull Collection<Object> apply(Collection<Object> collection) {
        Class<?> clazz = collection.getClass();
        // In the case of creation with Arrays.asList()
        if (clazz.getCanonicalName().equals(Arrays.class.getCanonicalName() + ".ArrayList"))
            clazz = ArrayList.class;
        Class<Collection<Object>> finalClass = (Class<Collection<Object>>) clazz;
        return collection.stream().map(this::apply)
                .collect(Collectors.toCollection(() -> new Refl<>(finalClass, new Object[0]).getObject()));
    }

    /**
     * Applies all the current variables to the given map.
     *
     * @param map the map
     * @return the map parsed
     */
    default @NotNull Map<Object, Object> apply(final @NotNull Map<Object, Object> map) {
        final List<Object> keys = new ArrayList<>(map.keySet());
        for (Object key : keys) {
            if (key == null) continue;
            Object value = map.get(key);
            if (value != null) {
                map.remove(key);
                map.put(apply(key), apply(value));
            }
        }
        return map;
    }

    /**
     * Gets the current variables in form of {@link Map}.
     *
     * @return the map
     */
    @NotNull Map<String, String> variables();
}
