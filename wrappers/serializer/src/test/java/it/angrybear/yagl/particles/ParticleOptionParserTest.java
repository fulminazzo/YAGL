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

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParticleOptionParserTest {

    private static ParticleOption<?>[] getTestOptions() {
        return new ParticleOption[]{
                new PrimitiveParticleOption<>(3),
                new PrimitiveParticleOption<>(3f),
                new PrimitiveParticleOption<>("Aint happening"),
                new DustParticleOption(Color.WHITE, 3f),
                new DustTransitionParticleOption(Color.BLACK, Color.WHITE, 3f),
                new BlockDataOption("oak_fence", "east=false,north=false,south=false,waterlogged=false,west=false")
        };
    }

    @ParameterizedTest
    @MethodSource("getTestOptions")
    void testOptions(ParticleOption<?> expected) throws IOException {
        FileConfiguration.addParsers(new ColorParser());
        ParticleOptionParser.addAllParsers();

        File file = new File("build/resources/test/options.yml");
        if (file.exists()) FileUtils.deleteFile(file);
        FileUtils.createNewFile(file);

        FileConfiguration configuration = new FileConfiguration(file);
        configuration.set("option", expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        ParticleOption<?> actual = configuration.get("option", expected.getClass());

        Field[] fields = expected.getClass().getDeclaredFields();
        for (Field field : fields)
            try {
                field.setAccessible(true);
                Object obj1 = field.get(expected);
                Object obj2 = field.get(actual);
                assertEquals(obj1.getClass(), obj2.getClass());
                assertEquals(obj1, obj2);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
    }
}