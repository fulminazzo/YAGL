package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.yagl.serializer.SerializationException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A {@link WrapperSerializer} for {@link Sound} objects.
 *
 * @see Sound
 * @see WrapperSerializer
 */
public final class SoundSerializer extends WrapperSerializer<Sound> {

    @Override
    protected @NotNull Sound initialize(final @NotNull String name, final @NotNull List<Object> data) throws SerializationException {
        return new Sound(
                name,
                toFloat(data.get(0)),
                toFloat(data.get(1))
        );
    }

    @Override
    protected @NotNull List<Object> toDataList(final @NotNull Sound wrapper) {
        return List.of(
                wrapper.getVolume(),
                wrapper.getPitch()
        );
    }

    @Override
    protected @NotNull List<Object> getDefaultDataList() {
        return List.of(
                (float) Sound.DEFAULT_VOLUME,
                (float) Sound.DEFAULT_PITCH
        );
    }

}
