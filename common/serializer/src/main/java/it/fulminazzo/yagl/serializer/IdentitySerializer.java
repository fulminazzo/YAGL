package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A simple serializer that returns the value as is.
 * <br>
 * This is useful for types that are supported by major serializer libraries (like primitives, collections or maps).
 */
final class IdentitySerializer implements Serializer<Object> {

    @Override
    public @Nullable Object deserialize(final @NotNull Node node) {
        return node.getRaw();
    }

    @Override
    public @Nullable Object serialize(final @Nullable Object value) {
        return value;
    }

}
