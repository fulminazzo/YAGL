package it.angrybear.yagl.particles;

import it.angrybear.yagl.ClassEnum;
import it.fulminazzo.fulmicollection.objects.Refl;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AParticleType<P extends ParticleOption<?>> extends ClassEnum {

    @Getter(AccessLevel.PACKAGE)
    private final Class<P> optionType;

    AParticleType(Class<P> optionType) {
        this.optionType = optionType;
    }

    public Particle create() {
        return create((P) null);
    }

    public Particle create(Object @NotNull ... objects) {
        if (objects.length == 0) return create();
        else return create(new Refl<>(getOptionType(), objects).getObject());
    }

    public Particle create(final @Nullable P particleOption) {
        return new Particle(name(), particleOption);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Particle) return name().equalsIgnoreCase(((Particle) o).getType());
        return super.equals(o);
    }
}
