package it.angrybear.yagl.particles;

import it.angrybear.yagl.Color;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.parsers.WrappersYAGLParser;
import it.angrybear.yagl.wrappers.Potion;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;
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
                new ColorParticleOption(Color.RED),
                new DustParticleOption(Color.WHITE, 3f),
                new DustTransitionParticleOption(Color.BLACK, Color.WHITE, 3f),
                new BlockDataOption("oak_fence", "east=false,north=false,south=false,waterlogged=false,west=false"),
                new MaterialDataOption("oak_fence", 2),
                new PotionParticleOption(new Potion("strength", 2, true)),
                new ItemParticleOption(new Item("STONE")),
        };
    }

    @ParameterizedTest
    @MethodSource("getTestOptions")
    void testOptions(ParticleOption<?> expected) throws IOException {
        WrappersYAGLParser.addAllParsers();
        FileConfiguration.addParsers(new CallableYAMLParser<>(Item.class, s -> new Item()));

        File file = new File("build/resources/test/options.yml");
        if (file.exists()) FileUtils.deleteFile(file);
        FileUtils.createNewFile(file);

        FileConfiguration configuration = new FileConfiguration(file);
        configuration.set("option", expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        ParticleOption<?> actual = configuration.get("option", expected.getClass());

        Field[] fields = expected.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj1 = ReflectionUtils.get(field, expected);
            Object obj2 = ReflectionUtils.get(field, actual);
            assertEquals(obj1.getClass(), obj2.getClass());
            assertEquals(obj1, obj2);
        }
    }
}