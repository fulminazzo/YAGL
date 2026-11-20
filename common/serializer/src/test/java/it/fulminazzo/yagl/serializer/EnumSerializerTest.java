package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumSerializerTest {

    @Test
    void testInvalidDeserializeThrowsSerializationException() {
        Serializer<Color> serializer = new EnumSerializer<>(Color.class);
        Node node = new Node("INVALID", "test", "key");
        assertThrows(SerializationException.class, () -> serializer.deserialize(node));
    }

    @ParameterizedTest
    @EnumSource(Color.class)
    void testDeserialize(final @NotNull Color color) throws SerializationException {
        Serializer<Color> serializer = new EnumSerializer<>(color.getClass());
        Node node = new Node(color.name(), "test", "key");
        Object result = serializer.deserialize(node);
        assertEquals(color, result);
    }

    @ParameterizedTest
    @EnumSource(Color.class)
    void testSerialize(final @NotNull Color color) throws SerializationException {
        Serializer<Color> serializer = new EnumSerializer<>(color.getClass());
        Object result = serializer.serialize(color);
        assertEquals(color.name(), result);
    }

    private enum Color {RED, GREEN, BLUE}

}
