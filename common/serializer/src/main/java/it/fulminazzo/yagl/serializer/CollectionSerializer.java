package it.fulminazzo.yagl.serializer;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A {@link Serializer} for {@link Collection} objects.
 * <br>
 * Supports only serialization.
 */
@RequiredArgsConstructor
final class CollectionSerializer implements Serializer<Collection<?>> {
    private final @NotNull YaglSerializer mainSerializer;

    @Override
    public @Nullable Collection<?> deserialize(final @NotNull Node node) throws SerializationException {
        throw new UnsupportedOperationException(String.format("%1$s#deserialize is not supported for general collection types. " +
                "Please use %1$s#deserializeCollection instead", mainSerializer.getClass().getSimpleName()));
    }

    @Override
    public @Nullable Object serialize(final @Nullable Collection<?> value) throws SerializationException {
        if (value == null) return null;
        List<Object> result = new ArrayList<>();
        for (Object item : value) result.add(mainSerializer.serialize(item));
        return result;
    }

}
