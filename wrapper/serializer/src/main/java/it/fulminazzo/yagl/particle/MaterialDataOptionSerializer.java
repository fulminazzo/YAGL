package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.serializer.Node;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import it.fulminazzo.yagl.structure.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Serializer} for {@link MaterialDataOption}.
 */
public final class MaterialDataOptionSerializer implements Serializer<MaterialDataOption> {

    @Override
    public @Nullable MaterialDataOption deserialize(final @NotNull Node node) throws SerializationException {
        return node.getAs(String.class).map(MaterialDataOption::new).orElse(null);
    }

    @Override
    public @Nullable Object serialize(final @Nullable MaterialDataOption value) {
        if (value == null) return null;
        Pair<String, Integer> option = value.getOption();
        return String.format("%s[%s]", option.getFirst(), option.getSecond());
    }

}
