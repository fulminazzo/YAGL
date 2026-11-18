package it.fulminazzo.yagl;

import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.action.GUIActionSerializer;
import it.fulminazzo.yagl.action.GUIItemAction;
import it.fulminazzo.yagl.action.GUIItemActionSerializer;
import it.fulminazzo.yagl.serializer.SerializersLoader;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import org.jetbrains.annotations.NotNull;

public final class GUISerializersLoader implements SerializersLoader {

    @Override
    public void load(final @NotNull YaglSerializer mainSerializer) {
        mainSerializer
                .registerSerializer(GUIAction.class, GUIActionSerializer.getInstance())
                .registerSerializer(GUIItemAction.class, GUIItemActionSerializer.getInstance())
        ;
    }

}
