package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract implementation of {@link Serializer} for objects.
 *
 * @param <T> the type of the object to serialize
 */
public abstract class ObjectSerializer<T> implements Serializer<T> {

    /**
     * Initializes a new instance of the object.
     *
     * @param node the node to get data from
     * @return the value or {@code null} if the serializer allows it
     * @throws SerializationException in case of any creation or deserialization errors
     */
    protected abstract @Nullable T initialize(final @NotNull DataNode node) throws SerializationException;

    /**
     * Loads data from the given node into the given value.
     *
     * @param value the value to load data into
     * @param node  the node to get data from
     * @throws SerializationException in case of any deserialization errors
     */
    protected abstract void loadData(final @NotNull T value, final @NotNull DataNode node) throws SerializationException;

    /**
     * Dumps data from the given value into the given output map.
     *
     * @param value  the value to dump data from
     * @param output the output map to dump data into
     * @throws SerializationException in case of any serialization errors
     */
    protected abstract void dumpData(final @NotNull T value, final @NotNull Map<String, Object> output) throws SerializationException;

    /**
     * Checks whether it is possible to return {@code null} values in case of a null {@link DataNode}.
     *
     * @return {@code true} if null values are allowed, {@code false} otherwise
     */
    protected boolean allowNull() {
        return true;
    }

    @Override
    public @Nullable T deserialize(final @NotNull Node node) throws SerializationException {
        if (node.getRaw() == null && allowNull()) return null;
        DataNode dataNode = node.asDataNodeOrThrow();
        T value = initialize(dataNode);
        if (value != null) loadData(value, dataNode);
        return value;
    }

    @Override
    public @Nullable Object serialize(final @Nullable T value) throws SerializationException {
        if (value == null) return null;
        Map<String, Object> output = new HashMap<>();
        dumpData(value, output);
        return output;
    }

}
