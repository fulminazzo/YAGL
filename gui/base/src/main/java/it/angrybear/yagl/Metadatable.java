package it.angrybear.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

/**
 * An interface that holds key-value pairs of {@link String}s.
 */
public interface Metadatable extends Iterable<String> {
    /**
     * A function used to convert the variables in {@link #apply(Object)}.
     */
    Function<@NotNull String, @NotNull String> VARIABLE_FORMAT = s -> "%" + s + "%";
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
     * @param object the object
     * @return the object parsed
     */
    default <T> T apply(final T object) {
        if (object == null) return null;

        final Refl<T> refl = new Refl<>(object);
        for (Field field : refl.getNonStaticFields())
            if (String.class.isAssignableFrom(field.getType())) {
                String o = refl.getFieldObject(field);
                if (o == null) continue;
                for (String v : this) {
                    String s = getVariable(v);
                    if (s != null)
                        o = o.replace(VARIABLE_FORMAT.apply(v), s);
                }
                refl.setFieldObject(field, o);
            }

        return refl.getObject();
    }

    /**
     * Applies all the current variables to the given string.
     *
     * @param string the string
     * @return the string parsed
     */
    default String apply(@NotNull String string) {
        for (final String v : this) {
            final String s = getVariable(v);
            if (s != null)
                string = string.replace(VARIABLE_FORMAT.apply(v), s);
        }
        return string;
    }

    /**
     * Gets the current variables in form of {@link Map}.
     *
     * @return the map
     */
    @NotNull Map<String, String> variables();

    @Override
    default @NotNull Iterator<String> iterator() {
        return variables().keySet().iterator();
    }
}
