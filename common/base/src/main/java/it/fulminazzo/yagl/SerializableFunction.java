package it.fulminazzo.yagl;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Base64;

/**
 * A general interface to allow {@link FunctionalInterface}s to serialize themselves.
 * <br>
 * <b>WARNING</b>: the interface requires that all fields and object bound to the {@link FunctionalInterface} are serializable.
 */
public interface SerializableFunction extends Serializable {

    /**
     * Serializes this object in Base64 using Java serialization.
     *
     * @return this function serialized
     * @throws IOException in case of any errors
     */
    default @NotNull String serialize() throws IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteStream)) {
            outputStream.writeObject(this);
            return Base64.getEncoder().encodeToString(byteStream.toByteArray());
        }
    }

    /**
     * Attempts to deserialize a {@link SerializableFunction} from a Base64 string.
     *
     * @param serialized the serialized function
     * @return the function
     * @throws IOException            in case of any errors
     * @throws ClassNotFoundException in case of any errors
     */
    static @NotNull SerializableFunction deserialize(final @NotNull String serialized) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(Base64.getDecoder().decode(serialized));
             ObjectInputStream inputStream = new ObjectInputStream(byteStream)) {
            return (SerializableFunction) inputStream.readObject();
        }
    }

}
