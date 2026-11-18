package it.fulminazzo.yagl.content;

import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * A {@link it.fulminazzo.yagl.serializer.Serializer} for {@link ItemGUIContent} objects.
 *
 * @see ItemGUIContent
 * @see ObjectSerializer
 */
final class ItemGUIContentSerializer extends ObjectSerializer<ItemGUIContent> {

    @Override
    protected @Nullable ItemGUIContent initialize(final @NotNull DataNode node) throws SerializationException {
        return node.getAs(Item.class).map(ItemGUIContent::newInstance).orElse(null);
    }

    @Override
    protected void loadData(final @NotNull ItemGUIContent value, final @NotNull DataNode node) {
        // nothing else to set, already covered in initialization
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void dumpData(final @NotNull ItemGUIContent value, final @NotNull DataNode output) throws SerializationException {
        Serializer<Item> itemSerializer = (Serializer<Item>) mainSerializer().getSerializer(Item.class);
        Map<String, Object> raw = (Map<String, Object>) itemSerializer.serialize(value);
        if (raw != null)
            for (Map.Entry<String, Object> entry : raw.entrySet())
                output.set(entry.getKey(), entry.getValue());
    }

}
