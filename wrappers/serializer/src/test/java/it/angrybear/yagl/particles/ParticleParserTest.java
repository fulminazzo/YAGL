package it.angrybear.yagl.particles;

import it.angrybear.yagl.Color;
import it.angrybear.yagl.parsers.WrappersYAGLParser;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ParticleParserTest {

    private static AParticleType<?>[] getTestOptions() {
        return Stream.concat(Arrays.stream(ParticleType.values()), Arrays.stream(LegacyParticleType.values()))
                .toArray(AParticleType[]::new);
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @MethodSource("getTestOptions")
    void testTypes(AParticleType<?> type) throws IOException {
        Particle expected = type.create();
        if (type.equals(ParticleType.BLOCK_CRACK) || type.equals(ParticleType.BLOCK_DUST) || type.equals(ParticleType.FALLING_DUST))
            expected = ((ParticleType<BlockDataOption>) type).create(new BlockDataOption("oak_log[axis=y]"));
        if (type.equals(ParticleType.SHRIEK))
            expected = ParticleType.SHRIEK.create(new PrimitiveParticleOption<>(3));
        if (type.equals(ParticleType.SCULK_CHARGE))
            expected = ParticleType.SCULK_CHARGE.create(new PrimitiveParticleOption<>(2.0f));
        if (type.equals(ParticleType.REDSTONE))
            expected = ParticleType.REDSTONE.create(new DustParticleOption(Color.RED, 4.0f));
        if (type.equals(ParticleType.DUST_COLOR_TRANSITION))
            expected = ParticleType.DUST_COLOR_TRANSITION.create(
                    new DustTransitionParticleOption(Color.WHITE, Color.BLACK, 5.0f));

        WrappersYAGLParser.addAllParsers();

        File file = new File("build/resources/test/particles.yml");
        if (!file.exists()) FileUtils.createNewFile(file);

        FileConfiguration configuration = new FileConfiguration(file);
        configuration.set(expected.getType(), expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        Particle actual = configuration.get(expected.getType(), Particle.class);

        Field[] fields = expected.getClass().getDeclaredFields();
        for (Field field : fields)
            try {
                field.setAccessible(true);
                Object obj1 = field.get(expected);
                Object obj2 = field.get(actual);
                if (obj1 == null) assertNull(obj2);
                else {
                    assertEquals(obj1.getClass(), obj2.getClass());
                    assertEquals(obj1, obj2);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
    }
}