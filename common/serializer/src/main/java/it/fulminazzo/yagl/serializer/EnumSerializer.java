package it.fulminazzo.yagl.serializer;

import it.fulminazzo.yagl.util.EnumUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A serializer for enum types.
 */
public final class EnumSerializer<E extends Enum<E>> implements Serializer<E> {
    private final @NotNull Class<E> enumType;

    /**
     * Instantiates a new Enum serializer.
     *
     * @param enumType the enum type
     */
    @SuppressWarnings("unchecked")
    public EnumSerializer(final @NotNull Class<?> enumType) {
        this.enumType = (Class<E>) enumType;
    }

    @Override
    public @Nullable E deserialize(final @NotNull Node node) throws SerializationException {
        String raw = node.getAsOrNull(String.class);
        if (raw == null) return null;
        try {
            return EnumUtils.valueOf(this.enumType, raw);
        } catch (IllegalArgumentException e) {
            throw new SerializationException("Invalid value '%s' specified for %s", raw, this.enumType.getSimpleName());
        }
    }

    @Override
    public @Nullable Object serialize(final @Nullable E value) throws SerializationException {
        return value == null ? null : value.name();
    }

}
