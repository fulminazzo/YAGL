package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class YaglSerializerTest {

    private static @NotNull Object[][] getSerializationDataTest() {
        return new Object[][]{
                {null, null},
                {1, 1},
                {2.0, 2.0},
                {3.0f, 3.0f},
                {4L, 4L},
                {true, true},
                {"Hello", "Hello"},
                {List.of(1, 2, 3), List.of(1, 2, 3)},
                {Map.of("Hello", "world"), Map.of("Hello", "world")},
                {new PersianCat("Billy"), new HashMap<String, Object>(Map.of(
                        "name", "Billy",
                        "type", "PERSIAN",
                        "age", 0
                )) {{
                    put("color", null);
                }}},
                {new ItalianGreet(), "ITALIAN_GREET"}
        };
    }

    @ParameterizedTest
    @MethodSource("getSerializationDataTest")
    void testThatDeserializeWorks(final @Nullable Object expected, final @Nullable Object rawData) throws SerializationException {
        YaglSerializer serializer = YaglSerializer.getInstance();
        Class<?> type = expected == null ? String.class : expected.getClass();
        Object serialized = serializer.deserialize(rawData, type);
        assertEquals(expected, serialized);
    }

    @ParameterizedTest
    @MethodSource("getSerializationDataTest")
    void testThatSerializeWorks(final @Nullable Object original, final @Nullable Object expected) throws SerializationException {
        YaglSerializer serializer = YaglSerializer.getInstance();
        Object serialized = serializer.serialize(original);
        assertEquals(expected, serialized);
    }

    @Test
    void testThatInstanceCorrectlyLoadedSerializers() throws SerializationException {
        YaglSerializer serializer = YaglSerializer.getInstance();
        assertNotNull(serializer.getSerializer(Cat.class), "A serializer for Cat should be registered");
        assertNotNull(serializer.getSerializer(GreetFunction.class), "A serializer for GreetFunction should be registered");
    }

    private static @NotNull Object[][] getSerializerDataTest() {
        return new Object[][]{
                {Cat.class, CatSerializer.class},
                {SiameseCat.class, CatSerializer.class},
                {PersianCat.class, CatSerializer.class},
                {GreetFunction.class, SerializableFunctionSerializer.class},
                {ItalianGreet.class, SerializableFunctionSerializer.class},
                {CustomGreet.class, SerializableFunctionSerializer.class}
        };
    }

    @ParameterizedTest
    @MethodSource("getSerializerDataTest")
    void testThatGetSerializerCorrectlyReturnsSerializerFor(
            final @NotNull Class<?> type,
            final @NotNull Class<Serializer<?>> expectedSerializerType
    ) throws SerializationException {
        Serializer<?> serializer = YaglSerializer.getInstance().getSerializer(type);
        assertNotNull(serializer);
        assertEquals(expectedSerializerType, serializer.getClass());
    }

}