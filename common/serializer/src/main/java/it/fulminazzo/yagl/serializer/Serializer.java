package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A serialization interface for a Java type.
 *
 * @param <T> the type of the object to serialize
 */
public interface Serializer<T> {

    /**
     * Deserializes the given node to the Java type.
     *
     * @param node the node
     * @return the value or {@code null} if the serializer allows it
     * @throws SerializationException in case of any deserialization errors (like casting or missing keys)
     */
    @Nullable T deserialize(final @NotNull Node node) throws SerializationException;

    /**
     * Serializes the given value to a node.
     *
     * @param value the value
     * @return the serialized value
     * @throws SerializationException in case of any serialization errors
     */
    @Nullable Object serialize(final @Nullable T value) throws SerializationException;

}
