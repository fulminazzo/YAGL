package it.fulminazzo.yagl.content;

import it.fulminazzo.yagl.serializer.Node;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A serializer for {@link CustomItemGUIContent} that delegates to {@link GUIContentSerializer}.
 *
 * @see CustomItemGUIContent
 * @see GUIContentSerializer
 */
@SuppressWarnings("rawtypes")
public final class CustomItemGUIContentSerializer implements Serializer<CustomItemGUIContent> {
    private final @NotNull GUIContentSerializer delegate;

    public CustomItemGUIContentSerializer() {
        this.delegate = new GUIContentSerializer();
    }

    @Override
    public @Nullable CustomItemGUIContent deserialize(final @NotNull Node node) throws SerializationException {
        return (CustomItemGUIContent) this.delegate.deserialize(node);
    }

    @Override
    public @Nullable Object serialize(final @Nullable CustomItemGUIContent value) throws SerializationException {
        return this.delegate.serialize(value);
    }

}
