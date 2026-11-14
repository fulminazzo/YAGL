package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.yagl.serializer.SerializationException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
        Object category = data.get(2);
        return new Sound(
                name,
                toFloat(data.get(0)),
                toFloat(data.get(1)),
                category == null ? null : category.toString()
        );
    }

    @Override
    protected @NotNull List<Object> toDataList(final @NotNull Sound wrapper) {
        List<Object> dataList = new ArrayList<>();
        dataList.add(wrapper.getVolume());
        dataList.add(wrapper.getPitch());
        dataList.add(wrapper.getCategory());
        return dataList;
    }

    @Override
    protected @NotNull List<Object> getDefaultDataList() {
        List<Object> defaultList = new ArrayList<>();
        defaultList.add((float) Sound.DEFAULT_VOLUME);
        defaultList.add((float) Sound.DEFAULT_PITCH);
        defaultList.add(null);
        return defaultList;
    }

}
