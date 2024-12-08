package it.fulminazzo.yagl.viewers;

import org.jetbrains.annotations.NotNull;

/**
 * An exception thrown when executing tasks that requires an online player fails.
 */
public class PlayerOfflineException extends RuntimeException {

    /**
     * Instantiates a new Player offline exception.
     *
     * @param name the name
     */
    public PlayerOfflineException(final @NotNull String name) {
        super(String.format("'%s' is not online", name));
    }
}
