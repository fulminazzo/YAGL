package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class YaglSerializerTest {
    private static final Map<String, Object> CAT_SERIALIZED_DATA = Map.of(
            "name", "Billy",
            TypedSerializer.TYPE_PATH, "PERSIAN",
            "age", 0
    );

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
                {new PersianCat("Billy"), CAT_SERIALIZED_DATA},
                {new ItalianGreet(), "ITALIAN_GREET"},
                {List.of(new PersianCat("Billy"), new ItalianGreet()), List.of(CAT_SERIALIZED_DATA, "ITALIAN_GREET")},
                {Map.of(new PersianCat("Billy"), new ItalianGreet()), new LinkedHashMap<>(Map.of(CAT_SERIALIZED_DATA, "ITALIAN_GREET"))}
        };
    }

    @Test
    void testThatDeserializeCollectionWorks() throws SerializationException {
        YaglSerializer serializer = YaglSerializer.getInstance();
        List<?> rawData = List.of(CAT_SERIALIZED_DATA);
        List<?> expected = List.of(new PersianCat("Billy"));
        assertEquals(expected, serializer.deserializeCollection(rawData, Cat.class, "", ""));
    }

    @Test
    void testThatDeserializeMapWorksWithCustomKeyAndValue() throws SerializationException {
        YaglSerializer serializer = YaglSerializer.getInstance();
        Map<?, ?> rawData = new LinkedHashMap<>(Map.of(CAT_SERIALIZED_DATA, "ITALIAN_GREET"));
        Map<?, ?> expected = new LinkedHashMap<>(Map.of(new PersianCat("Billy"), new ItalianGreet()));
        assertEquals(expected, serializer.deserializeMap(rawData, Cat.class, GreetFunction.class, "", ""));
    }

    @Test
    void testThatDeserializeMapWorksWithSimpleTypes() throws SerializationException {
        YaglSerializer serializer = YaglSerializer.getInstance();
        Map<?, ?> rawData = Map.of("key1", 1, "key2", 2);
        Map<?, ?> expected = Map.of("key1", 1, "key2", 2);
        assertEquals(expected, serializer.deserializeMap(rawData, String.class, Integer.class, "", ""));
    }

    @Test
    void testThatDeserializeMapReturnsNullForNullData() throws SerializationException {
        YaglSerializer serializer = YaglSerializer.getInstance();
        assertEquals(null, serializer.deserializeMap(null, String.class, Integer.class, "", ""));
    }

    @Test
    void testThatDeserializeMapThrowsForNonMapData() {
        YaglSerializer serializer = YaglSerializer.getInstance();
        org.junit.jupiter.api.Assertions.assertThrows(
                SerializationException.class,
                () -> serializer.deserializeMap(List.of(1, 2, 3), String.class, Integer.class, "", "")
        );
    }

    @ParameterizedTest
    @MethodSource("getSerializationDataTest")
    void testThatDeserializeWorks(final @Nullable Object expected, final @Nullable Object rawData) throws SerializationException {
        assumeTrue(expected == null || (!Collection.class.isAssignableFrom(expected.getClass()) && !Map.class.isAssignableFrom(expected.getClass())));
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