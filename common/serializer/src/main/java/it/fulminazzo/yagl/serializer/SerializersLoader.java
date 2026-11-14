package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;

/**
 * An object for registering several serializers to {@link YaglSerializer}.
 */
public interface SerializersLoader {

    /**
     * Registers several serializers to {@link YaglSerializer}.
     *
     * @param mainSerializer the main serializer
     */
    void load(final @NotNull YaglSerializer mainSerializer);


}
