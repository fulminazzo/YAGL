package it.fulminazzo.yagl.serializer;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Serializer} for {@link Map} objects.
 * <br>
 * Supports only serialization.
 */
@RequiredArgsConstructor
final class MapSerializer implements Serializer<Map<?, ?>> {
    private final @NotNull YaglSerializer mainSerializer;

    @Override
    public @Nullable Map<?, ?> deserialize(final @NotNull Node node) throws SerializationException {
        throw new UnsupportedOperationException(String.format("%1$s#deserialize is not supported for general map types. " +
                "Please use %1$s#deserializeMap instead", mainSerializer.getClass().getSimpleName()));
    }

    @Override
    public @Nullable Object serialize(final @Nullable Map<?, ?> value) throws SerializationException {
        if (value == null) return null;
        Map<Object, Object> result = new HashMap<>();
        for (Map.Entry<?, ?> entry : value.entrySet()) result.put(mainSerializer.serialize(entry.getKey()), mainSerializer.serialize(entry.getValue()));
        return result;
    }

}
