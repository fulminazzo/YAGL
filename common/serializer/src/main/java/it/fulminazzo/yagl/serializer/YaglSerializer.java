package it.fulminazzo.yagl.serializer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A serializer that can convert objects to and from a serialized representation.
 * <br>
 * Holds together many {@link Serializer}s of the YAGL project.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class YaglSerializer {
    private static @Nullable YaglSerializer instance;

    private final @NotNull Map<Class<?>, Serializer<?>> serializers = new ConcurrentHashMap<>();
    private final @NotNull Serializer<?> baseTypesSerializer = new IdentitySerializer();

    /**
     * Converts the given raw data into an object of the given type if a {@link Serializer} is registered for it.
     *
     * @param rawData the raw data
     * @param type    the type
     * @param <T>     the type of the object
     * @return the deserialized object
     * @throws SerializationException in case of any error
     */
    public <T> @Nullable T deserialize(final @Nullable Object rawData, final @NotNull Class<T> type) throws SerializationException {
        return deserialize(rawData, type, "", "");
    }

    /**
     * Converts the given raw data into an object of the given type if a {@link Serializer} is registered for it.
     *
     * @param rawData the raw data
     * @param type    the type
     * @param path    the current path to the raw data.
     *                This information is only useful if the raw data has been extracted from a configuration file.
     *                Otherwise, it can be set as an empty string.
     * @param key     the current key of the raw data.
     *                This information is only useful if the raw data has been extracted from a configuration file.
     *                Otherwise, it can be set as an empty string.
     * @param <T>     the type of the object
     * @return the deserialized object
     * @throws SerializationException in case of any error
     */
    public <T> @Nullable T deserialize(
            final @Nullable Object rawData,
            final @NotNull Class<T> type,
            final @NotNull String path,
            final @NotNull String key
    ) throws SerializationException {
        Object raw = getSerializer(type).deserialize(new Node(rawData, path, key));
        try {
            // Special case for double and float
            if (raw instanceof Double && type == Float.class) return type.cast(((Double) raw).floatValue());
            else return type.cast(raw);
        } catch (ClassCastException e) {
            throw new SerializationException(
                    "Invalid type for key '%s' in path '%s': expected %s but got %s",
                    key, path, type.getName(), raw == null ? "null" : raw.getClass().getName()
            );
        }
    }

    /**
     * Converts the given object to a serialized representation.
     *
     * @param target the object to serialize
     * @param <T>    the type of the object
     * @return the serialized representation of the object
     * @throws SerializationException in case of any error
     */
    @SuppressWarnings("unchecked")
    public <T> @Nullable Object serialize(final @Nullable T target) throws SerializationException {
        if (target == null) return null;
        else {
            Serializer<T> serializer = (Serializer<T>) getSerializer(target.getClass());
            return serializer.serialize(target);
        }
    }

    /**
     * Registers a serializer for the given type.
     *
     * @param type       the type
     * @param serializer the serializer
     * @param <T>        the type
     * @return this serializer
     */
    public <T> @NotNull YaglSerializer registerSerializer(final @NotNull Class<T> type, final @NotNull Serializer<T> serializer) {
        serializers.put(type, serializer);
        return this;
    }

    /**
     * Tries to get the most appropriate serializer for the given type.
     *
     * @param type the type
     * @return the serializer
     * @throws SerializationException if no serializer could be found
     */
    public @NotNull Serializer<?> getSerializer(final @NotNull Class<?> type) throws SerializationException {
        Package typePackage = type.getPackage();
        if (typePackage != null && typePackage.getName().startsWith("java."))
            return baseTypesSerializer;

        Serializer<?> current = serializers.get(type);
        if (current != null) return current;
        int score = Integer.MAX_VALUE;
        for (Map.Entry<Class<?>, Serializer<?>> entry : serializers.entrySet()) {
            int serializerScore = scoreType(type, entry.getKey());
            if (serializerScore != -1 && serializerScore < score) {
                current = entry.getValue();
                score = serializerScore;
            }
        }
        if (current == null)
            throw new SerializationException("No serializer found for type: %s ", type.getCanonicalName());
        return current;
    }

    /**
     * Gets a new instance of the serializer.
     *
     * @return the instance
     */
    public static @NotNull YaglSerializer getInstance() {
        if (instance == null) instance = initialize();
        return instance;
    }

    private static @NotNull YaglSerializer initialize() {
        YaglSerializer instance = new YaglSerializer();
        for (SerializersLoader serializersLoader : ServiceLoader.load(SerializersLoader.class, YaglSerializer.class.getClassLoader()))
            serializersLoader.load(instance);
        return instance;
    }

    private static int scoreType(final @NotNull Class<?> type, final @NotNull Class<?> reference) {
        if (type.equals(reference)) return 0;
        Class<?> superclass = type.getSuperclass();
        if (superclass != null) {
            int parentScore = scoreType(superclass, reference);
            if (parentScore != -1)
                return parentScore * 100;
        }
        for (Class<?> interfaceType : type.getInterfaces()) {
            int interfaceScore = scoreType(interfaceType, reference);
            if (interfaceScore != -1)
                return interfaceScore * 100;
        }
        return -1;
    }

}
