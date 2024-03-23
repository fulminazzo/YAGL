package it.angrybear.yagl.particles;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class Particle {
    private final String type;
    private final ParticleOption option;

    Particle(final @NotNull String type, final @Nullable ParticleOption option) {
        this.type = type;
        this.option = option;
    }
}
