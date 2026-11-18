package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
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
    static final @NotNull String ENCHANTMENTS_PATH = "enchants";

    @Override
    protected @Nullable EnchantmentItemComponent initialize(final @NotNull DataNode node) throws SerializationException {
        List<?> rawData = node.getOrNull(ENCHANTMENTS_PATH, List.class);
        if (rawData == null) return null;
        Collection<Enchantment> enchantments = mainSerializer().deserializeCollection(rawData, Enchantment.class);
        if (enchantments == null) return null;
        return new EnchantmentItemComponent(enchantments);
    }

    @Override
    protected void loadData(final @NotNull EnchantmentItemComponent value, final @NotNull DataNode node) {
        // no data to load after initialization
    }

    @Override
    protected void dumpData(final @NotNull EnchantmentItemComponent value, final @NotNull Map<String, Object> output) throws SerializationException {
        output.put(ENCHANTMENTS_PATH, mainSerializer().serialize(value.getEnchantments()));
    }

}
