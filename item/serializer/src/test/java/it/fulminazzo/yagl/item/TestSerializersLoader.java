package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.serializer.SerializersLoader;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import org.jetbrains.annotations.NotNull;

//TODO: temporary
public final class TestSerializersLoader implements SerializersLoader {

    @Override
    public void load(final @NotNull YaglSerializer mainSerializer) {
        mainSerializer.registerSerializer(Item.class, new TemporaryItemSerializer());
    }

}
