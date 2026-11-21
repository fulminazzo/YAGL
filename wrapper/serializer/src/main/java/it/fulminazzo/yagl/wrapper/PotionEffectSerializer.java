package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.yagl.serializer.SerializationException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A serializer for {@link PotionEffect} objects.
 *
 * @see PotionEffect
 * @see WrapperSerializer
 */
public final class PotionEffectSerializer extends WrapperSerializer<PotionEffect> {

    @Override
    protected @NotNull PotionEffect initialize(final @NotNull String name, final @NotNull List<Object> data) throws SerializationException {
        return new PotionEffect(
                name,
                toDouble(data.get(0)),
                toInt(data.get(1)),
                toBoolean(data.get(2)),
                toBoolean(data.get(3))
        );
    }

    @Override
    protected @NotNull List<Object> toDataList(final @NotNull PotionEffect wrapper) {
        return List.of(
                wrapper.getDuration(),
                wrapper.getAmplifier(),
                wrapper.isShowingParticles(),
                wrapper.isShowingIcon()
        );
    }

    @Override
    protected @NotNull List<Object> getDefaultDataList() {
        return List.of(
                PotionEffect.DEFAULT_DURATION,
                PotionEffect.MIN_AMPLIFIER,
                PotionEffect.DEFAULT_SHOWING_PARTICLES,
                PotionEffect.DEFAULT_SHOWING_ICON
        );
    }

}
