package it.fulminazzo.yagl;

import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GUISerializersLoaderTest {

    private static Class<?>[] testTypes() {
        return new Class<?>[]{
        };
    }

    @ParameterizedTest
    @MethodSource("testTypes")
    void testThatSerializerIsRegisteredFor(final @NotNull Class<?> type) throws SerializationException {
        assertNotNull(YaglSerializer.getInstance().getSerializer(type));
    }

}