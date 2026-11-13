package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * Special implementation of {@link Node} containing a chunk of unserialized data.
 *
 * @see Serializer
 */
public final class DataNode extends Node {

    /**
     * Instantiates a new {@link DataNode} with the given data, path, and key.
     *
     * @param data the data to be stored in the node
     * @param path the path of the node
     * @param key  the key of the node
     */
    DataNode(final @Nullable Map<?, ?> data, final @NotNull String path, final @NotNull String key) {
        super(data, path, key);
    }

    /**
     * Attempts to retrieve the sub-node corresponding to the given key from the internal data.
     *
     * @param key the name of the key
     * @return the sub-node corresponding to the given key, or an empty optional if the key is not found
     * @throws SerializationException if the value is not a valid data node or was {@code null}
     */
    public @NotNull DataNode getSubNodeOrThrow(final @NotNull String key) throws SerializationException {
        return new Node(getActualData().get(key), getPath(), key).asDataNodeOrThrow();
    }

    /**
     * Attempts to retrieve the sub-node corresponding to the given key from the internal data.
     *
     * @param key the name of the key
     * @return the sub-node corresponding to the given key, or an empty optional if the key is not found
     * @throws SerializationException if the value is not of the expected type
     */
    public @NotNull Optional<DataNode> getSubNode(final @NotNull String key) throws SerializationException {
        return new Node(getActualData().get(key), getPath(), key).asDataNode();
    }

    /**
     * Attempts to retrieve the value corresponding to the given key from the internal data.
     *
     * @param key  the name of the key
     * @param type the expected type of the value
     * @param <T>  the type of the value
     * @return the value corresponding to the given key
     * @throws SerializationException if the value is not of the expected type or was not found
     */
    public <T> @NotNull T getOrThrow(final @NotNull String key, final @NotNull Class<T> type) throws SerializationException {
        return getNode(key).getAsOrThrow(type);
    }

    /**
     * Attempts to retrieve the value corresponding to the given key from the internal data.
     *
     * @param key  the name of the key
     * @param type the expected type of the value
     * @param <T>  the type of the value
     * @return the value corresponding to the given key, or {@code null} if the key is not present
     * @throws SerializationException if the value is not of the expected type
     */
    public <T> @Nullable T getOrNull(final @NotNull String key, final @NotNull Class<T> type) throws SerializationException {
        return getNode(key).getAsOrNull(type);
    }

    /**
     * Attempts to retrieve the value corresponding to the given key from the internal data.
     *
     * @param key  the name of the key
     * @param type the expected type of the value
     * @param <T>  the type of the value
     * @return the value corresponding to the given key, or an empty optional if the key is not present
     * @throws SerializationException if the value is not of the expected type
     */
    public <T> @NotNull Optional<T> get(final @NotNull String key, final @NotNull Class<T> type) throws SerializationException {
        return getNode(key).getAs(type);
    }

    /**
     * Attempts to retrieve the value corresponding to the given key from the internal data.
     *
     * @param key the name of the key
     * @return the node corresponding to the given key
     */
    public @NotNull Node getNode(final @NotNull String key) {
        return new Node(getActualData().get(key), getPath(), key);
    }

    @Override
    protected @NotNull String getPath() {
        String basePath = super.getPath();
        if (!basePath.isEmpty()) basePath += ".";
        return basePath + getKey();
    }

    @SuppressWarnings("DataFlowIssue")
    private @NotNull Map<?, ?> getActualData() {
        return (Map<?, ?>) data;
    }

}
