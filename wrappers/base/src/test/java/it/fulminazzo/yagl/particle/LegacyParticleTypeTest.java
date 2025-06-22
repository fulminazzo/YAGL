package it.fulminazzo.yagl.particle;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LegacyParticleTypeTest {

    private static LegacyParticleType<?>[] getTests() {
        return LegacyParticleType.values();
    }

    @ParameterizedTest
    @MethodSource("getTests")
    void testValueId(LegacyParticleType<?> obj) {
        assertEquals(obj, LegacyParticleType.valueOf(obj.ordinal()));
    }

    @ParameterizedTest
    @MethodSource("getTests")
    void testValueName(LegacyParticleType<?> obj) {
        assertEquals(obj, LegacyParticleType.valueOf(obj.name()));
    }

}