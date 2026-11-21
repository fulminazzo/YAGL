package it.fulminazzo.yagl;

import it.fulminazzo.yagl.serializer.Node;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A serializer for {@link Color}.
 *
 * @see Color
 * @see Serializer
 */
public final class ColorSerializer implements Serializer<Color> {

    @Override
    public @Nullable Color deserialize(final @NotNull Node node) throws SerializationException {
        return node.getAs(String.class)
                .map(s -> {
                    try {
                        return Color.valueOf(s);
                    } catch (IllegalArgumentException e) {
                        return Color.fromARGB(s);
                    }
                })
                .orElse(null);
    }

    @Override
    public @Nullable Object serialize(final @Nullable Color value) {
        return value == null
                ? null
                : value.isKnownEnum() ?
                value.name() :
                value.toARGB();
    }

}
