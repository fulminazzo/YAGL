package it.fulminazzo.yagl;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

/**
 * A general interface for various functions.
 * Provides a {@link #serialize()} method for serializing purposes.
 */
public interface SerializableFunction extends Serializable {

    /**
     * Serializes this object in Base64 using Java serialization.
     *
     * @return this function serialized
     */
    default String serialize() {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteStream)) {
            outputStream.writeObject(this);
            return Base64.getEncoder().encodeToString(byteStream.toByteArray());
        } catch (IOException e) {
            throw new SerializationException(this, e);
        }
    }

    /**
     * An exception thrown by {@link SerializableFunction#serialize()} in case of failed serialization.
     */
    final class SerializationException extends RuntimeException {
        private static final long serialVersionUID = 4295873290297237267L;

        /**
         * Instantiates a new Serialization exception.
         *
         * @param target the target that failed to serialize
         * @param cause  the cause
         */
        public SerializationException(final @NotNull SerializableFunction target, final @NotNull Throwable cause) {
            super("Failed to serialize " + target.getClass().getCanonicalName(), cause);
        }

    }

}
