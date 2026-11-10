package it.fulminazzo.yagl.metadatable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

/**
 * An interface that holds key-value pairs of {@link String}s.
 */
public interface Metadatable {
    /**
     * A function used to parse the names of the variables.
     */
    Function<@NotNull String, @NotNull String> VARIABLE_PARSER = s -> {
        final String separator = "_";

        StringBuilder builder = new StringBuilder();
        for (char c :  s.toCharArray()) {
            if (c >= 'A' && c <= 'Z') builder.append(separator);
            else if (Character.isWhitespace(c) || c == '-') {
                builder.append(separator);
                continue;
            }
            builder.append(c);
        }

        String output = builder.toString().toLowerCase();
        while (output.startsWith(separator))
            output = output.substring(separator.length());
        return output;
    };

    /**
     * Sets a variable with the given name.
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
     * Unset the variable with the given name.
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
     * Returns {@code null} in case it is missing.
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
     * Applies all the current variables to the given object fields
     * by replacing in every string all the variables in the format {@code <variable>}.
     *
     * @param <T>    the type of the object
     * @param object the object
     * @return the object parsed
     */
    default <T> T apply(final T object) {
        return new MetadatableHelper(this).apply(object);
    }

    /**
     * Gets the current variables in the form of {@link Map}.
     *
     * @return the map
     */
    @NotNull Map<String, String> variables();

}
