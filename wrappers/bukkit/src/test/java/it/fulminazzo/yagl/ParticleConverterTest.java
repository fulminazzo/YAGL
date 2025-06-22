package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.jbukkit.annotations.After1_;
import it.fulminazzo.yagl.particle.Particle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@After1_(13)
class ParticleConverterTest extends BukkitUtils {

    @BeforeAll
    static void setAllUp() {
        setupServer();
    }

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
    }

    @ParameterizedTest
    @EnumSource(ParticleConverter.class)
    void simulateNewerVersionsFunctioning(ParticleConverter converter) {
        Particle particle = new Refl<>(Particle.class, converter.name(), null).getObject();
        try (MockedStatic<org.bukkit.Particle> ignored = mockStatic(org.bukkit.Particle.class)) {
            when(org.bukkit.Particle.valueOf(any())).thenAnswer(a -> {
                String name = a.getArgument(0);
                if (name.equals(converter.getParticleName())) return org.bukkit.Particle.FLAME;
                else throw new IllegalArgumentException("Unknown particle: " + name);
            });

            Object converted = ParticleConverter.convertToBukkit(particle);
            assertNotNull(converted, "Converted should have not been null");
        }
    }

}