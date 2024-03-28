package it.angrybear.yagl.particles;

import it.angrybear.yagl.Color;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParticleTypeTest {

    private static ParticleType<?>[] getTests() {
        return ParticleType.values();
    }

    private static Object[][] getTestParticles() {
        List<Particle> strippedParticles = new ArrayList<>();
        List<Particle> particles = new ArrayList<>();
        for (ParticleType<?> type : ParticleType.values()) {
            strippedParticles.add(type.create(new Object[0]));
            particles.add(type.create());
        }
        strippedParticles.add(ParticleType.SCULK_CHARGE.create(10f));
        particles.add(ParticleType.SCULK_CHARGE.create(new PrimitiveParticleOption<>(10f)));
        strippedParticles.add(ParticleType.SHRIEK.create(11));
        particles.add(ParticleType.SHRIEK.create(new PrimitiveParticleOption<>(11)));
        strippedParticles.add(ParticleType.REDSTONE.create(Color.RED, 12f));
        particles.add(ParticleType.REDSTONE.create(new DustParticleOption(Color.RED, 12f)));
        strippedParticles.add(ParticleType.DUST_COLOR_TRANSITION.create(Color.RED, Color.BLUE, 12f));
        particles.add(ParticleType.DUST_COLOR_TRANSITION.create(new DustTransitionParticleOption(Color.RED, Color.BLUE, 12f)));

        List<Particle[]> tmp = new LinkedList<>();
        for (int i = 0; i < strippedParticles.size(); i++)
            tmp.add(new Particle[]{particles.get(i), strippedParticles.get(i)});
        return tmp.toArray(new Particle[tmp.size()][]);
    }

    @ParameterizedTest
    @MethodSource("getTestParticles")
    void testStrippedCreateMethod(Particle expected, Particle actual) {
        assertEquals(expected.getOption(), (Object) actual.getOption());
    }

    @ParameterizedTest
    @MethodSource("getTests")
    void testEnumMethods(ParticleType<?> obj) {
        assertEquals(obj, ParticleType.valueOf(obj.name()));
    }
}