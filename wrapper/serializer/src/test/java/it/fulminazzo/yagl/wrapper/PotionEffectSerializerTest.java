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

public class PotionEffectSerializerTest extends SerializerTestHelper<PotionEffect> {

    protected PotionEffectSerializerTest() {
        super(PotionEffect.class, new PotionEffectSerializer());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testDeserialize(final @NotNull String fileName, final @NotNull PotionEffect expected, final @NotNull String contents) throws IOException {
        File file = getLoadFile(fileName);
        file.getParentFile().mkdirs();
        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(contents.getBytes());
        }

        PotionEffect actual = load(fileName);
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDuration(), actual.getDuration());
        assertEquals(expected.getAmplifier(), actual.getAmplifier());
        assertEquals(expected.isShowingParticles(), actual.isShowingParticles());
        assertEquals(expected.isShowingIcon(), actual.isShowingIcon());
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testSerialize(final @NotNull String fileName, final @NotNull PotionEffect expected, final @NotNull String contents) throws IOException {
        String raw = readRawString(dump(expected, fileName));
        assertEquals(contents, raw);
    }

    private static @NotNull Object[][] testData() {
        return new Object[][]{
                new Object[]{
                        "default_duration",
                        new PotionEffect("test"),
                        "test"
                },
                new Object[]{
                        "default_amplifier",
                        new PotionEffect("test", 17.5),
                        "test" + WrapperSerializer.DATA_SEPARATOR + "17.5"
                },
                new Object[]{
                        "hidden_particles",
                        new PotionEffect("test", 17.5, 4),
                        "test" + WrapperSerializer.DATA_SEPARATOR + "17.5" + WrapperSerializer.DATA_SEPARATOR + "4"
                },
                new Object[]{
                        "hidden_icon",
                        new PotionEffect("test", 17.5, 4, false),
                        "test" + WrapperSerializer.DATA_SEPARATOR + "17.5" + WrapperSerializer.DATA_SEPARATOR + "4" + WrapperSerializer.DATA_SEPARATOR + "false"
                },
                new Object[]{
                        "complete",
                        new PotionEffect("test", 17.5, 4, false, false),
                        "test" + WrapperSerializer.DATA_SEPARATOR + "17.5" + WrapperSerializer.DATA_SEPARATOR + "4" + WrapperSerializer.DATA_SEPARATOR + "false" + WrapperSerializer.DATA_SEPARATOR + "false"
                }
        };
    }

}
