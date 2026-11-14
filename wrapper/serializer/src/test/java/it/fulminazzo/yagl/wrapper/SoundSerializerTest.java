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

public class SoundSerializerTest extends SerializerTestHelper<Sound> {

    protected SoundSerializerTest() {
        super(Sound.class, new SoundSerializer());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testDeserialize(final @NotNull String fileName, final @NotNull Sound expected, final @NotNull String contents) throws IOException {
        File file = getLoadFile(fileName);
        file.getParentFile().mkdirs();
        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(contents.getBytes());
        }

        Sound actual = load(fileName);
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getVolume(), actual.getVolume());
        assertEquals(expected.getPitch(), actual.getPitch());
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testSerialize(final @NotNull String fileName, final @NotNull Sound expected, final @NotNull String contents) throws IOException {
        String raw = readRawString(dump(expected, fileName));
        assertEquals(contents, raw);
    }

    private static @NotNull Object[][] testData() {
        return new Object[][]{
                new Object[]{
                        "default_volume",
                        new Sound("test"),
                        "test"
                },
                new Object[]{
                        "custom_volume",
                        new Sound("test", 2.5f),
                        "test" + WrapperSerializer.DATA_SEPARATOR + "2.5"
                },
                new Object[]{
                        "custom_pitch",
                        new Sound("test", 2.5f, 0.5f),
                        "test" + WrapperSerializer.DATA_SEPARATOR + "2.5" + WrapperSerializer.DATA_SEPARATOR + "0.5"
                },
                new Object[]{
                        "custom_category",
                        new Sound("test", 2.5f, 0.5f, "category"),
                        "test" + WrapperSerializer.DATA_SEPARATOR + "2.5" + WrapperSerializer.DATA_SEPARATOR + "0.5" + WrapperSerializer.DATA_SEPARATOR + "category"
                }
        };
    }

}
