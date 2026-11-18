package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
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
     * Updates the value at the given key.
     *
     * @param key   the name of the key
     * @param value the value to be stored, serialized using {@link YaglSerializer}
     * @throws SerializationException in case of any errors
     */
    public void set(final @NotNull String key, final @Nullable Object value) throws SerializationException {
        set(key, value, null);
    }

    /**
     * Updates the value at the given key.
     *
     * @param key   the name of the key
     * @param value the value to be stored, serialized using {@link YaglSerializer}
     * @param defaultValue if the value is the same as the default value, it will not be stored to preserve space
     * @throws SerializationException in case of any errors
     * @param <T> the type of the value
     */
    public <T> void set(final @NotNull String key, final @Nullable T value, final @Nullable T defaultValue) throws SerializationException {
        if (defaultValue == null || !defaultValue.equals(value))
            getActualData().put(key, YaglSerializer.getInstance().serialize(value));
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
     * Attempts to retrieve the value corresponding to the given key from the internal data as a {@link Collection}.
     *
     * @param key          the name of the key
     * @param elementsType the expected type of the elements.
     *                     Custom types are supported thanks to custom {@link Serializer} registered in {@link YaglSerializer}.
     * @param <T>          the type of the elements
     * @return the collection corresponding to the given key
     * @throws SerializationException if the value or the elements are not of the expected type, or they were not found
     */
    public <T> @NotNull Collection<T> getCollectionOrThrow(
            final @NotNull String key,
            final @NotNull Class<T> elementsType
    ) throws SerializationException {
        return getNode(key).getAsCollectionOrThrow(elementsType);
    }

    /**
     * Attempts to retrieve the value corresponding to the given key from the internal data as a {@link Collection}.
     *
     * @param key          the name of the key
     * @param elementsType the expected type of the elements.
     *                     Custom types are supported thanks to custom {@link Serializer} registered in {@link YaglSerializer}.
     * @param <T>          the type of the elements
     * @return the collection corresponding to the given key, or {@code null} if the key is not present
     * @throws SerializationException if the value or the elements are not of the expected type
     */
    public <T> @Nullable Collection<T> getCollectionOrNull(
            final @NotNull String key,
            final @NotNull Class<T> elementsType
    ) throws SerializationException {
        return getNode(key).getAsCollectionOrNull(elementsType);
    }

    /**
     * Attempts to retrieve the value corresponding to the given key from the internal data as a {@link Collection}.
     *
     * @param key          the name of the key
     * @param elementsType the expected type of the elements.
     *                     Custom types are supported thanks to custom {@link Serializer} registered in {@link YaglSerializer}.
     * @param <T>          the type of the elements
     * @return the value corresponding to the given key, or an empty optional if the key is not present
     * @throws SerializationException if the value or the elements are not of the expected type
     */
    public <T> @NotNull Optional<Collection<T>> getCollection(
            final @NotNull String key,
            final @NotNull Class<T> elementsType
    ) throws SerializationException {
        return getNode(key).getAsCollection(elementsType);
    }

    /**
     * Attempts to retrieve the value corresponding to the given key from the internal data as a {@link Map}.
     *
     * @param key       the name of the key
     * @param keyType   the expected type of the map keys.
     *                  Custom types are supported thanks to custom {@link Serializer} registered in {@link YaglSerializer}.
     * @param valueType the expected type of the map values.
     *                  Custom types are supported thanks to custom {@link Serializer} registered in {@link YaglSerializer}.
     * @param <K>       the type of the map keys
     * @param <V>       the type of the map values
     * @return the map corresponding to the given key
     * @throws SerializationException if the value or the entries are not of the expected type, or they were not found
     */
    public <K, V> @NotNull Map<K, V> getMapOrThrow(
            final @NotNull String key,
            final @NotNull Class<K> keyType,
            final @NotNull Class<V> valueType
    ) throws SerializationException {
        return getNode(key).getAsMapOrThrow(keyType, valueType);
    }

    /**
     * Attempts to retrieve the value corresponding to the given key from the internal data as a {@link Map}.
     *
     * @param key       the name of the key
     * @param keyType   the expected type of the map keys.
     *                  Custom types are supported thanks to custom {@link Serializer} registered in {@link YaglSerializer}.
     * @param valueType the expected type of the map values.
     *                  Custom types are supported thanks to custom {@link Serializer} registered in {@link YaglSerializer}.
     * @param <K>       the type of the map keys
     * @param <V>       the type of the map values
     * @return the map corresponding to the given key, or {@code null} if the key is not present
     * @throws SerializationException if the value or the entries are not of the expected type
     */
    public <K, V> @Nullable Map<K, V> getMapOrNull(
            final @NotNull String key,
            final @NotNull Class<K> keyType,
            final @NotNull Class<V> valueType
    ) throws SerializationException {
        return getNode(key).getAsMapOrNull(keyType, valueType);
    }

    /**
     * Attempts to retrieve the value corresponding to the given key from the internal data as a {@link Map}.
     *
     * @param key       the name of the key
     * @param keyType   the expected type of the map keys.
     *                  Custom types are supported thanks to custom {@link Serializer} registered in {@link YaglSerializer}.
     * @param valueType the expected type of the map values.
     *                  Custom types are supported thanks to custom {@link Serializer} registered in {@link YaglSerializer}.
     * @param <K>       the type of the map keys
     * @param <V>       the type of the map values
     * @return the value corresponding to the given key, or an empty optional if the key is not present
     * @throws SerializationException if the value or the entries are not of the expected type
     */
    public <K, V> @NotNull Optional<Map<K, V>> getMap(
            final @NotNull String key,
            final @NotNull Class<K> keyType,
            final @NotNull Class<V> valueType
    ) throws SerializationException {
        return getNode(key).getAsMap(keyType, valueType);
    }

    /**
     * Attempts to retrieve the value corresponding to the given key from the internal data.
     *
     * @param key  the name of the key
     * @param type the expected type of the value.
     *             Custom types are supported thanks to custom {@link Serializer} registered in {@link YaglSerializer}.
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
     * @param type the expected type of the value.
     *             Custom types are supported thanks to custom {@link Serializer} registered in {@link YaglSerializer}.
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
     * @param type the expected type of the value.
     *             Custom types are supported thanks to custom {@link Serializer} registered in {@link YaglSerializer}.
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

    @SuppressWarnings({"DataFlowIssue", "unchecked"})
    private @NotNull Map<Object, Object> getActualData() {
        return (Map<Object, Object>) data;
    }

}
