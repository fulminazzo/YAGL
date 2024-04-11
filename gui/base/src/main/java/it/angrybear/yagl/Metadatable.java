package it.angrybear.yagl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

/**
 * An interface that holds key-value pairs of {@link String}s.
 */
public interface Metadatable extends Iterable<String> {
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
    @NotNull Metadatable setVariable(final @NotNull String name, final @NotNull String value);

    /**
     * Unset variable metadatable.
     *
     * @param name the name
     * @return this metadatable
     */
    @NotNull Metadatable unsetVariable(final @NotNull String name);

    /**
     * Gets variable.
     * Returns <i>null</i> in case of missing.
     *
     * @param name the name
     * @return the variable
     */
    @Nullable String getVariable(final @NotNull String name);

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
