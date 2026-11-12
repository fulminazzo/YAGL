package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.Color;
import it.fulminazzo.yagl.structure.Pair;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link ParticleType#REDSTONE}.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class DustParticleOption extends ParticleOption<Pair<Color, Float>> {
    private final @NotNull Color color;
    private final float size;

    @Override
    public @NotNull Pair<Color, Float> getOption() {
        return new Pair<>(this.color, this.size);
    }

}
