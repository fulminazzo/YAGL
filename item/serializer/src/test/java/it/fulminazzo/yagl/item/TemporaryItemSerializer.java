package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.Node;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

//TODO: temporary
public final class TemporaryItemSerializer implements Serializer<Item> {

    @Override
    public @Nullable Item deserialize(final @NotNull Node node) throws SerializationException {
        DataNode dataNode = node.asDataNodeOrThrow();
        String material = dataNode.getOrNull("material", String.class);
        if (material == null) return null;
        Integer amount = dataNode.getOrNull("amount", Integer.class);
        Item item = Item.newItem().setMaterial(material);
        if (amount != null) item.setAmount(amount);
        return item;
    }

    @Override
    public @Nullable Object serialize(final @Nullable Item value) {
        if (value == null) return null;
        Map<String, Object> result = new HashMap<>();
        String material = value.getMaterial();
        if (material != null) result.put("material", material);
        result.put("amount", value.getAmount());
        return result;
    }

}
