package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnchantmentSerializerTest extends SerializerTestHelper<Enchantment> {

    protected EnchantmentSerializerTest() {
        super(Enchantment.class, new EnchantmentSerializer());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testDeserialize(final @NotNull String fileName, final @NotNull Enchantment expected, final @NotNull String contents) throws IOException {
        File file = getLoadFile(fileName);
        file.getParentFile().mkdirs();
        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(contents.getBytes());
        }

        Enchantment actual = load(fileName);
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLevel(), actual.getLevel());
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testSerialize(final @NotNull String fileName, final @NotNull Enchantment expected, final @NotNull String contents) throws IOException {
        String raw = readRawString(dump(expected, fileName));
        assertEquals(contents, raw);
    }

    private static @NotNull Object[][] testData() {
        return new Object[][]{
                new Object[]{
                        "default_level",
                        new Enchantment("test"),
                        "test"
                },
                new Object[]{
                        "custom_level",
                        new Enchantment("test", 5),
                        "test" + WrapperSerializer.DATA_SEPARATOR + "5"
                }
        };
    }

}
