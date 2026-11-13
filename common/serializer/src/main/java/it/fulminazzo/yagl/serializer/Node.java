package it.fulminazzo.yagl.serializer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * Identifies a general node containing a value.
 *
 * @see Serializer
 */
@Getter(AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class Node {
    protected @Nullable Object data;
    @NotNull String path;
    @NotNull String key;

    /**
     * Attempts to convert this node to a {@link DataNode}.
     *
     * @return the data node
     * @throws SerializationException if the value is not a valid data node or was {@code null}
     */
    public @NotNull DataNode asDataNodeOrThrow() throws SerializationException {
        return asDataNode().orElseThrow(() -> new SerializationException(
                "Could not find data node for key '%s' in path '%s'", key, getPath()
        ));
    }

    /**
     * Attempts to convert this node to a {@link DataNode}.
     *
     * @return the data node or an empty optional
     * @throws SerializationException if the value is not a valid data node
     */
    public @NotNull Optional<DataNode> asDataNode() throws SerializationException {
        try {
            return data == null ? Optional.empty() : Optional.of(new DataNode((Map<?, ?>) data, getPath(), key));
        } catch (ClassCastException e) {
            throw new SerializationException(
                    "Invalid type for key '%s' in path '%s': expected data node but got %s",
                    key, getPath(), data.getClass().getName()
            );
        }
    }

    /**
     * Attempts to retrieve the internal value casting it to the requested type.
     *
     * @param type the expected type of the value
     * @param <T>  the type of the value
     * @return the value
     * @throws SerializationException if the value is not of the expected type or was {@code null}
     */
    public <T> @NotNull T getAsOrThrow(final @NotNull Class<T> type) throws SerializationException {
        return getAs(type).orElseThrow(() ->
                new SerializationException("Could not find required key '%s' in path '%s'", key, path)
        );
    }

    /**
     * Attempts to retrieve the internal value casting it to the requested type.
     *
     * @param type the expected type of the value
     * @param <T>  the type of the value
     * @return the value or {@code null}
     * @throws SerializationException if the value is not of the expected type
     */
    public <T> @Nullable T getAsOrNull(final @NotNull Class<T> type) throws SerializationException {
        return getAs(type).orElse(null);
    }

    /**
     * Attempts to retrieve the internal value casting it to the requested type.
     *
     * @param type the expected type of the value
     * @param <T>  the type of the value
     * @return the value or an empty optional
     * @throws SerializationException if the value is not of the expected type
     */
    public <T> @NotNull Optional<T> getAs(final @NotNull Class<T> type) throws SerializationException {
        Object raw = data;
        if (raw == null) return Optional.empty();
        try {
            return Optional.of(type.cast(raw));
        } catch (ClassCastException e) {
            throw new SerializationException(
                    "Invalid type for key '%s' in path '%s': expected %s but got %s",
                    key, path, type.getName(), raw.getClass().getName()
            );
        }
    }

    /**
     * Gets the raw internal value.
     *
     * @return the raw internal value
     */
    public @Nullable Object getRaw() {
        return data;
    }

}
