package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import it.fulminazzo.yagl.wrapper.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A {@link it.fulminazzo.yagl.serializer.Serializer} for {@link EnchantmentItemComponent}.
 *
 * @see EnchantmentItemComponent
 * @see ObjectSerializer
 */
public class EnchantmentItemComponentSerializer extends ObjectSerializer<EnchantmentItemComponent> {

    @Override
    protected @Nullable EnchantmentItemComponent initialize(final @NotNull DataNode node) throws SerializationException {
        List<?> rawData = node.getOrNull("enchants", List.class);
        if (rawData == null) return null;
        Collection<Enchantment> enchantments = YaglSerializer.getInstance().deserializeCollection(rawData, Enchantment.class);
        if (enchantments == null) return null;
        return new EnchantmentItemComponent(enchantments);
    }

    @Override
    protected void loadData(final @NotNull EnchantmentItemComponent value, final @NotNull DataNode node) {
        // no data to load after initialization
    }

    @Override
    protected void dumpData(final @NotNull EnchantmentItemComponent value, final @NotNull Map<String, Object> output) throws SerializationException {
        output.put("enchants", YaglSerializer.getInstance().serialize(value.getEnchantments()));
    }

}
