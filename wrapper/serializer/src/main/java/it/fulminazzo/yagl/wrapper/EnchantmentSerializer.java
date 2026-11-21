package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.yagl.serializer.SerializationException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A serializer for {@link Enchantment} objects.
 *
 * @see Enchantment
 * @see WrapperSerializer
 */
public final class EnchantmentSerializer extends WrapperSerializer<Enchantment> {

    @Override
    protected @NotNull Enchantment initialize(final @NotNull String name, final @NotNull List<Object> data) throws SerializationException {
        return new Enchantment(
                name,
                toInt(data.get(0))
        );
    }

    @Override
    protected @NotNull List<Object> toDataList(final @NotNull Enchantment wrapper) {
        return List.of(
                wrapper.getLevel()
        );
    }

    @Override
    protected @NotNull List<Object> getDefaultDataList() {
        return List.of(
                Enchantment.MIN_LEVEL
        );
    }

}
