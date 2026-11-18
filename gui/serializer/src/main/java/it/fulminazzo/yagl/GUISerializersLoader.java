package it.fulminazzo.yagl;

import it.fulminazzo.yagl.action.*;
import it.fulminazzo.yagl.content.requirement.RequirementChecker;
import it.fulminazzo.yagl.content.requirement.RequirementCheckerSerializer;
import it.fulminazzo.yagl.serializer.SerializersLoader;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import org.jetbrains.annotations.NotNull;

public final class GUISerializersLoader implements SerializersLoader {

    @Override
    public void load(final @NotNull YaglSerializer mainSerializer) {
        mainSerializer
                .registerSerializer(GUIAction.class, GUIActionSerializer.getInstance())
                .registerSerializer(BiGUIAction.class, BiGUIActionSerializer.getInstance())
                .registerSerializer(GUIItemAction.class, GUIItemActionSerializer.getInstance())
                .registerSerializer(RequirementChecker.class, RequirementCheckerSerializer.getInstance())
        ;
    }

}
