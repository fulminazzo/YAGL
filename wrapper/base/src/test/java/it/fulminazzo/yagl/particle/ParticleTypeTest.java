package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.Color;
import it.fulminazzo.yagl.item.AbstractItem;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

@SuppressWarnings("deprecation")
class ParticleTypeTest {

    private static ParticleType<?>[] getTests() {
        return ParticleType.values();
    }

    private static Object[][] getEqualityTestParticles() {
        List<Particle[]> particles = new LinkedList<>();
        for (AParticleType<?> type : Stream.concat(Arrays.stream(ParticleType.values()),
                Arrays.stream(LegacyParticleType.values())).collect(Collectors.toList()))
            particles.add(new Particle[]{type.create(), new Particle(type.name(), null)});
        return particles.toArray(new Particle[0][2]);
    }

    @ParameterizedTest
    @MethodSource("getEqualityTestParticles")
    void testEqualityOfParticleTypes(Particle actual, Particle expected) {
        assertEquals(expected, actual);
    }

    private static Object[] inequalityTestParticles() {
        return new Object[]{
                ParticleType.SMOKE_LARGE, LegacyParticleType.BONE_MEAL_USE,
                null, mock(AParticleType.class), ParticleType.SHRIEK.create()
        };
    }

    @ParameterizedTest
    @MethodSource("inequalityTestParticles")
    void testInequalityOfParticleTypes(Object object) {
        assertNotEquals(ParticleType.ASH, object);
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
        AbstractItem item = mock(AbstractItem.class);
        strippedParticles.add(ParticleType.ITEM_CRACK.create(item));
        particles.add(ParticleType.ITEM_CRACK.create(new ItemParticleOption<>(item)));

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
    void testValueId(ParticleType<?> obj) {
        assertEquals(obj, ParticleType.valueOf(obj.ordinal()));
    }

    @ParameterizedTest
    @MethodSource("getTests")
    void testValueName(ParticleType<?> obj) {
        assertEquals(obj, ParticleType.valueOf(obj.name()));
    }

}
