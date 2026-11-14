package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.yagl.serializer.SerializationException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A {@link WrapperSerializer} for {@link Potion} objects.
 *
 * @see Potion
 * @see WrapperSerializer
 */
public final class PotionSerializer extends WrapperSerializer<Potion> {

    @Override
    protected @NotNull Potion initialize(final @NotNull String name, final @NotNull List<Object> data) throws SerializationException {
        return new Potion(
                name,
                toInt(data.get(0)),
                toBoolean(data.get(1)),
                toBoolean(data.get(2))
        );
    }

    @Override
    protected @NotNull List<Object> toDataList(final @NotNull Potion wrapper) {
        return List.of(
                wrapper.getLevel(),
                wrapper.isSplash(),
                wrapper.isExtended()
        );
    }

    @Override
    protected @NotNull List<Object> getDefaultDataList() {
        return List.of(
                Potion.MIN_LEVEL,
                Potion.DEFAULT_SPLASH,
                Potion.DEFAULT_EXTENDED
        );
    }

}
