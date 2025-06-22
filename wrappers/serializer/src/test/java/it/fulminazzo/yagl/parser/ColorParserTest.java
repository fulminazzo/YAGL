package it.fulminazzo.yagl.parser;

import it.fulminazzo.yagl.Color;
import it.fulminazzo.yagl.ParserTestHelper;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorParserTest extends ParserTestHelper<Color> {

    @BeforeAll
    static void setAllUp() {
        FileConfiguration.addParsers(new ColorParser());
    }

    private static Color[] getTestColors() {
        return Stream.concat(Arrays.stream(Color.values()), Stream.of(Color.fromARGB("#ff00aa"))).toArray(Color[]::new);
    }

    @ParameterizedTest
    @MethodSource("getTestColors")
    void testSaveColors(Color color) throws IOException {
        File file = new File("build/resources/test/colors.yml");
        if (file.exists()) FileUtils.deleteFile(file);
        FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        configuration.set("color", color);
        configuration.save();

        configuration = new FileConfiguration(file);
        String name = color.name();
        if (name == null) name = color.toARGB();
        assertEquals(name, configuration.getString("color"));
        assertEquals(color, configuration.get("color", Color.class));
    }

    @Override
    protected Class<?> getParser() {
        return ColorParser.class;
    }

}
