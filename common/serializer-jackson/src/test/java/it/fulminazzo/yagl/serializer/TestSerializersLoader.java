package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;

public final class TestSerializersLoader implements SerializersLoader {

    @Override
    public void load(final @NotNull YaglSerializer mainSerializer) {
        mainSerializer.registerSerializer(Person.class, new PersonSerializer());
    }

}
