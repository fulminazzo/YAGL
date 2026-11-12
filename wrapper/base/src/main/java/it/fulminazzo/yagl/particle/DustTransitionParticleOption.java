package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.Color;
import it.fulminazzo.yagl.structure.Triple;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link ParticleType#DUST_COLOR_TRANSITION}.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class DustTransitionParticleOption extends ParticleOption<Triple<Color, Color, Float>> {
    private final @NotNull Color from;
    private final @NotNull Color to;
    private final float size;

    @Override
    public @NotNull Triple<Color, Color, Float> getOption() {
        return new Triple<>(this.from, this.to, this.size);
    }

}
