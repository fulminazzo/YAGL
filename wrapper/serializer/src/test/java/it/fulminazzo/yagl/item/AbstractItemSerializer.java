package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.serializer.Node;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Serializer} for {@link AbstractItem} objects used in tests.
 */
@SuppressWarnings("deprecation")
public final class AbstractItemSerializer implements Serializer<AbstractItem> {

    @Override
    public @Nullable AbstractItem deserialize(final @NotNull Node node) throws SerializationException {
        String name = node.getAsOrNull(String.class);
        if (name == null) return null;
        return new Item(name);
    }

    @Override
    public @Nullable Object serialize(final @Nullable AbstractItem value) {
        if (value == null) return null;
        if (value instanceof Item) {
            return ((Item) value).getName();
        }
        return null;
    }

}
