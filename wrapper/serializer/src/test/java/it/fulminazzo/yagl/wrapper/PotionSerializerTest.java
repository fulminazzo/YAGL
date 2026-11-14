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

public class PotionSerializerTest extends SerializerTestHelper<Potion> {

    protected PotionSerializerTest() {
        super(Potion.class, new PotionSerializer());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testDeserialize(final @NotNull String fileName, final @NotNull Potion expected, final @NotNull String contents) throws IOException {
        File file = getLoadFile(fileName);
        file.getParentFile().mkdirs();
        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(contents.getBytes());
        }

        Potion actual = load(fileName);
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLevel(), actual.getLevel());
        assertEquals(expected.isSplash(), actual.isSplash());
        assertEquals(expected.isExtended(), actual.isExtended());
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testSerialize(final @NotNull String fileName, final @NotNull Potion expected, final @NotNull String contents) throws IOException {
        String raw = readRawString(dump(expected, fileName));
        assertEquals(contents, raw);
    }

    private static @NotNull Object[][] testData() {
        return new Object[][]{
                new Object[]{
                        "default_level",
                        new Potion("test"),
                        "test"
                },
                new Object[]{
                        "level_2",
                        new Potion("test", 2),
                        "test" + WrapperSerializer.DATA_SEPARATOR + "2"
                },
                new Object[]{
                        "splash",
                        new Potion("test", 2, true),
                        "test" + WrapperSerializer.DATA_SEPARATOR + "2" + WrapperSerializer.DATA_SEPARATOR + "true"
                },
                new Object[]{
                        "extended",
                        new Potion("test", 2, true, true),
                        "test" + WrapperSerializer.DATA_SEPARATOR + "2" + WrapperSerializer.DATA_SEPARATOR + "true" + WrapperSerializer.DATA_SEPARATOR + "true"
                }
        };
    }

}
