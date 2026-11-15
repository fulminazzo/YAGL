package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.serializer.SerializersLoader;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * Registers test-only serializers for types that have no production serializer.
 */
@SuppressWarnings("deprecation")
public final class TestSerializersLoader implements SerializersLoader {

    @Override
    public void load(final @NotNull YaglSerializer mainSerializer) {
        mainSerializer.registerSerializer(AbstractItem.class, new AbstractItemSerializer());
    }

}
