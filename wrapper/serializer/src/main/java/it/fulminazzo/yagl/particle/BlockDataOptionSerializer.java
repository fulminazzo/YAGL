package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.serializer.Node;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A serializer for {@link BlockDataOption}.
 *
 * @see BlockDataOption
 * @see Serializer
 */
public final class BlockDataOptionSerializer implements Serializer<BlockDataOption> {

    @Override
    public @Nullable BlockDataOption deserialize(final @NotNull Node node) throws SerializationException {
        return node.getAs(String.class).map(BlockDataOption::new).orElse(null);
    }

    @Override
    public @Nullable Object serialize(final @Nullable BlockDataOption value) {
        return value == null ? null : value.getOption();
    }

}
