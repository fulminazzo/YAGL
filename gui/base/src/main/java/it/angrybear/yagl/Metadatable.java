package it.angrybear.yagl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;

/**
 * An interface that holds key-value pairs of {@link String}s.
 */
public interface Metadatable extends Iterable<String> {

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
