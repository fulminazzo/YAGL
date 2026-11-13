package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A general exception that can be thrown during serialization.
 *
 * @see Serializer
 */
public final class SerializationException extends Exception {
    private static final long serialVersionUID = 8920347559357277592L;

    /**
     * Instantiates a new Serialization exception.
     *
     * @param message the message
     * @param args    the arguments to format in the message
     */
    public SerializationException(final @NotNull String message, final @NotNull Object @Nullable ... args) {
        super(String.format(message, args));
    }

}
