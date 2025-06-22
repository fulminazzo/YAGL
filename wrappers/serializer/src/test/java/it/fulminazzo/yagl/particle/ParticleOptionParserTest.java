package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.Color;
import it.fulminazzo.yagl.ParserTestHelper;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.parser.WrappersYAGLParser;
import it.fulminazzo.yagl.wrapper.Potion;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.CallableYAMLParser;
import it.fulminazzo.yamlparser.parsers.annotations.PreventSaving;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ParticleOptionParserTest {

    @BeforeAll
    static void setAllUp() {
        WrappersYAGLParser.addAllParsers();
        FileConfiguration.addParsers(new CallableYAMLParser<>(Item.class, s -> new Item()));
    }

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
                new ItemParticleOption<>(new Item("STONE")),
        };
    }

    @ParameterizedTest
    @MethodSource("getTestOptions")
    void testOptions(ParticleOption<?> expected) throws IOException {
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
            Object obj1 = ReflectionUtils.getOrThrow(field, expected);
            Object obj2 = ReflectionUtils.getOrThrow(field, actual);
            assertEquals(obj1.getClass(), obj2.getClass());
            assertEquals(obj1, obj2);
        }
    }

    @Test
    void testLoadNull() {
        assertDoesNotThrow(() -> new ParticleOptionParser<>(DustParticleOption.class).getLoader()
                .apply(mock(IConfiguration.class), "path"));
    }

    @Test
    void testSaveNull() {
        assertDoesNotThrow(() -> new ParticleOptionParser<>(DustParticleOption.class).getDumper()
                .accept(mock(IConfiguration.class), "path", null));
    }

    private static Object[] getParsers() {
        return new Object[]{
                ColorOptionParser.class, PotionParticleOptionParser.class,
                ItemParticleOptionParser.class, BlockDataOptionParser.class,
                MaterialDataOptionParser.class
        };
    }

    @ParameterizedTest
    @MethodSource("getParsers")
    <T> void testLoadNullFromParser(Class<?> clazz) {
        ParserTestHelper<T> helper = new ParserTestHelper<T>() {
            @Override
            protected Class<?> getParser() {
                return clazz;
            }
        };
        new Refl<>(helper).invokeMethod("testLoadNull");
    }

    @ParameterizedTest
    @MethodSource("getParsers")
    <T> void testSaveNullFromParser(Class<?> clazz) {
        ParserTestHelper<T> helper = new ParserTestHelper<T>() {
            @Override
            protected Class<?> getParser() {
                return clazz;
            }
        };
        new Refl<>(helper).invokeMethod("testSaveNull");
    }

    @Test
    void testSaveAndLoadWithImmutableFields() throws IOException {
        FileConfiguration.addParsers(new ParticleOptionParser<>(MockParticleOption.class));

        ParticleOption<?> expected = new MockParticleOption(30);

        File file = new File("build/resources/test/immutable-fields.yml");
        if (file.exists()) FileUtils.deleteFile(file);
        FileUtils.createNewFile(file);

        FileConfiguration configuration = new FileConfiguration(file);
        configuration.set("option", expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        ParticleOption<?> actual = configuration.get("option", expected.getClass());

        assertInstanceOf(MockParticleOption.class, actual);
        MockParticleOption mock = (MockParticleOption) actual;

        assertEquals(0, mock.shouldNotBeLoaded, "Field should be to its default value (0)");
        assertEquals(30, MockParticleOption.SHOULD_NOT_BE_LOADED, "Static field should not have been changed");
    }

    private static class MockParticleOption extends ParticleOption<String> {
        static int SHOULD_NOT_BE_LOADED = 1;
        @PreventSaving
        int shouldNotBeLoaded;

        public MockParticleOption() {

        }

        public MockParticleOption(int value) {
            SHOULD_NOT_BE_LOADED = value;
            this.shouldNotBeLoaded = value;
        }

        @Override
        public String getOption() {
            return "Hello world";
        }

    }

}
