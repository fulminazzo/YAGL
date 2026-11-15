package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PrimitiveParticleOptionSerializerTest extends SerializerTestHelper<PrimitiveParticleOption<Object>> {

    @SuppressWarnings("unchecked")
    protected PrimitiveParticleOptionSerializerTest() {
        super((Class<PrimitiveParticleOption<Object>>) (Class<?>) PrimitiveParticleOption.class, new SingleParticleOptionSerializer<>(
                (Class<PrimitiveParticleOption<Object>>) (Class<?>) PrimitiveParticleOption.class,
                PrimitiveParticleOption::new
        ));
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testDeserialize(final @NotNull Object expected, final @NotNull Object contents) throws IOException {
        String fileName = expected.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        File file = getLoadFile(fileName);
        file.getParentFile().mkdirs();
        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(contents.toString().getBytes());
        }

        final PrimitiveParticleOption<Object> actual = load(fileName);
        assertNotNull(actual);
        assertEquals(expected, actual.getOption());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testSerialize(final @NotNull Object expected, final @NotNull Object contents) throws IOException {
        String fileName = expected.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        final PrimitiveParticleOption<Object> particleOption = new PrimitiveParticleOption<>(expected);
        Object raw = readRaw(dump(particleOption, fileName), contents.getClass());
        assertEquals(contents, raw);
    }

    private static @NotNull Object[][] testData() {
        return new Object[][]{
                {10, 10},
                {3.14, 3.14},
                {"Hello, world!", "Hello, world!"}
        };
    }

}
