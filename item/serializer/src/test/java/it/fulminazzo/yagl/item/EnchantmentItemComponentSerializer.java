package it.fulminazzo.yagl.item;

import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.wrapper.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        return node.getCollection(ENCHANTMENTS_PATH, Enchantment.class)
                .map(EnchantmentItemComponent::new)
                .orElse(null);
    }

    @Override
    protected void loadData(final @NotNull EnchantmentItemComponent value, final @NotNull DataNode node) {
        // no data to load after initialization
    }

    @Override
    protected void dumpData(final @NotNull EnchantmentItemComponent value, final @NotNull Map<String, Object> output) {
        output.put(ENCHANTMENTS_PATH, value.getEnchantments());
    }

}
