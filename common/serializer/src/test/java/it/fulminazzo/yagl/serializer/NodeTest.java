package it.fulminazzo.yagl.serializer;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void testThatGetAsCollectionOrThrowDoesNotThrowForExistingKey() throws SerializationException {
        final Node node = new Node(List.of("value"), "current", "key");
        assertEquals(List.of("value"), node.getAsCollectionOrThrow(String.class));
    }

    @Test
    void testThatGetAsCollectionOrThrowThrowsForMissingKey() {
        final Node node = new Node(null, "current", "key");
        assertThrows(SerializationException.class, () -> node.getAsCollectionOrThrow(String.class));
    }

    @Test
    void testThatGetAsCollectionOrThrowThrowsForWrongType() {
        final Node node = new Node(List.of("value"), "current", "key");
        assertThrows(SerializationException.class, () -> node.getAsCollectionOrThrow(Integer.class));
    }

    @Test
    void testThatGetAsCollectionOrNullDoesNotThrowForExistingKey() throws SerializationException {
        final Node node = new Node(List.of("value"), "current", "key");
        assertEquals(List.of("value"), node.getAsCollectionOrNull(String.class));
    }

    @Test
    void testThatGetAsCollectionOrNullDoesNotThrowForMissingKey() throws SerializationException {
        final Node node = new Node(null, "current", "key");
        assertNull(node.getAsCollectionOrNull(String.class));
    }

    @Test
    void testThatGetAsCollectionOrNullThrowsForWrongType() {
        final Node node = new Node(List.of("value"), "current", "key");
        assertThrows(SerializationException.class, () -> node.getAsCollectionOrNull(Integer.class));
    }

    @Test
    void testThatGetAsCollectionDoesNotThrowForExistingKey() throws SerializationException {
        final Node node = new Node(List.of("value"), "current", "key");
        Optional<Collection<String>> opt = node.getAsCollection(String.class);
        assertTrue(opt.isPresent(), "No value found");
        assertEquals(List.of("value"), opt.get());
    }

    @Test
    void testThatGetAsCollectionDoesNotThrowForMissingKey() throws SerializationException {
        final Node node = new Node(null, "current", "key");
        assertTrue(node.getAsCollection(String.class).isEmpty());
    }

    @Test
    void testThatGetAsCollectionThrowsForWrongType() {
        final Node node = new Node(List.of("value"), "current", "key");
        assertThrows(SerializationException.class, () -> node.getAsCollection(Integer.class));
    }

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

    @Test
    void testThatGetAsMapOrThrowDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> mapData = new HashMap<>();
        mapData.put("k1", "v1");
        final Node node = new Node(mapData, "current", "key");

        HashMap<String, String> expected = new HashMap<>();
        expected.put("k1", "v1");
        assertEquals(expected, node.getAsMapOrThrow(String.class, String.class));
    }

    @Test
    void testThatGetAsMapOrThrowThrowsForMissingKey() {
        final Node node = new Node(null, "current", "key");
        assertThrows(SerializationException.class, () -> node.getAsMapOrThrow(String.class, String.class));
    }

    @Test
    void testThatGetAsMapOrThrowThrowsForWrongType() {
        HashMap<Object, Object> mapData = new HashMap<>();
        mapData.put("k1", "v1");
        final Node node = new Node(mapData, "current", "key");
        assertThrows(SerializationException.class, () -> node.getAsMapOrThrow(Integer.class, Integer.class));
    }

    @Test
    void testThatGetAsMapOrNullDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> mapData = new HashMap<>();
        mapData.put("k1", "v1");
        final Node node = new Node(mapData, "current", "key");

        HashMap<String, String> expected = new HashMap<>();
        expected.put("k1", "v1");
        assertEquals(expected, node.getAsMapOrNull(String.class, String.class));
    }

    @Test
    void testThatGetAsMapOrNullDoesNotThrowForMissingKey() throws SerializationException {
        final Node node = new Node(null, "current", "key");
        assertNull(node.getAsMapOrNull(String.class, String.class));
    }

    @Test
    void testThatGetAsMapOrNullThrowsForWrongType() {
        HashMap<Object, Object> mapData = new HashMap<>();
        mapData.put("k1", "v1");
        final Node node = new Node(mapData, "current", "key");
        assertThrows(SerializationException.class, () -> node.getAsMapOrNull(Integer.class, Integer.class));
    }

    @Test
    void testThatGetAsMapDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> mapData = new HashMap<>();
        mapData.put("k1", "v1");
        final Node node = new Node(mapData, "current", "key");

        Optional<Map<String, String>> opt = node.getAsMap(String.class, String.class);
        assertTrue(opt.isPresent(), "No value found");
        HashMap<String, String> expected = new HashMap<>();
        expected.put("k1", "v1");
        assertEquals(expected, opt.get());
    }

    @Test
    void testThatGetAsMapDoesNotThrowForMissingKey() throws SerializationException {
        final Node node = new Node(null, "current", "key");
        assertTrue(node.getAsMap(String.class, String.class).isEmpty());
    }

    @Test
    void testThatGetAsMapThrowsForWrongType() {
        HashMap<Object, Object> mapData = new HashMap<>();
        mapData.put("k1", "v1");
        final Node node = new Node(mapData, "current", "key");
        assertThrows(SerializationException.class, () -> node.getAsMap(Integer.class, Integer.class));
    }

}