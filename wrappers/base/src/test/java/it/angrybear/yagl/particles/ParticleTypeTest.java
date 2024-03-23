package it.angrybear.yagl.particles;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParticleTypeTest {

    private static ParticleType<?>[] getTests() {
        return ParticleType.values();
    }

    @ParameterizedTest
    @MethodSource("getTests")
    void testEnumMethods(ParticleType<?> obj) {
        assertEquals(obj, ParticleType.valueOf(obj.name()));
    }
}