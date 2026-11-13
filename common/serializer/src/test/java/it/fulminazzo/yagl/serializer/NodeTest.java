package it.fulminazzo.yagl.serializer;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void testThatGetAsOrThrowDoesNotThrowForExistingKey() throws SerializationException {
        final Node node = new Node("value", "current", "key");
        assertEquals("value", node.getAsOrThrow(String.class));
    }

    @Test
    void testThatGetAsOrThrowThrowsForMissingKey() {
        final Node node = new Node(null, "current", "key");
        assertThrows(SerializationException.class, () -> node.getAsOrThrow(String.class));
    }

    @Test
    void testThatGetAsOrThrowThrowsForWrongType() {
        final Node node = new Node("value", "current", "key");
        assertThrows(SerializationException.class, () -> node.getAsOrThrow(Integer.class));
    }

    @Test
    void testThatGetAsOrNullDoesNotThrowForExistingKey() throws SerializationException {
        final Node node = new Node("value", "current", "key");
        assertEquals("value", node.getAsOrNull(String.class));
    }

    @Test
    void testThatGetAsOrNullDoesNotThrowForMissingKey() throws SerializationException {
        final Node node = new Node(null, "current", "key");
        assertNull(node.getAsOrNull(String.class));
    }

    @Test
    void testThatGetAsOrNullThrowsForWrongType() {
        final Node node = new Node("value", "current", "key");
        assertThrows(SerializationException.class, () -> node.getAsOrNull(Integer.class));
    }

    @Test
    void testThatGetAsDoesNotThrowForExistingKey() throws SerializationException {
        final Node node = new Node("value", "current", "key");
        Optional<String> opt = node.getAs(String.class);
        assertTrue(opt.isPresent(), "No value found");
        assertEquals("value", opt.get());
    }

    @Test
    void testThatGetAsDoesNotThrowForMissingKey() throws SerializationException {
        final Node node = new Node(null, "current", "key");
        assertTrue(node.getAs(String.class).isEmpty());
    }

    @Test
    void testThatGetAsThrowsForWrongType() {
        final Node node = new Node("value", "current", "key");
        assertThrows(SerializationException.class, () -> node.getAs(Integer.class));
    }

}