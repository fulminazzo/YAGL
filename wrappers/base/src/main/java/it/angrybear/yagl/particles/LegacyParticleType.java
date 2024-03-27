package it.angrybear.yagl.particles;

import org.jetbrains.annotations.NotNull;

public class LegacyParticleType<P extends ParticleOption<?>> extends AParticleType<P> {

    private LegacyParticleType() {
        this(null);
    }

    private LegacyParticleType(Class<P> optionType) {
        super(optionType);
    }

    public static LegacyParticleType<?> valueOf(final @NotNull String name) {
        return valueOf(name, LegacyParticleType.class);
    }

    public static LegacyParticleType<?>[] values() {
        return values(LegacyParticleType.class);
    }
}
