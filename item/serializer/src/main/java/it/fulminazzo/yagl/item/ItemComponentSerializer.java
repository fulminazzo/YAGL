package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.TypedSerializer;

/**
 * A serializer for {@link ItemComponent} implementations.
 * <br>
 * Custom components can be registered with {@link ItemComponentSerializer#registerSerializer(Class, ObjectSerializer)}.
 * <br>
 * This serializer can be obtained with {@link it.fulminazzo.yagl.ItemSerializersLoader#getItemComponentSerializer()}.
 */
public final class ItemComponentSerializer extends TypedSerializer<ItemComponent> {

    public ItemComponentSerializer() {
        super(ItemComponent.class);
    }

}
