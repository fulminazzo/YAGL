package it.fulminazzo.yagl.serializer;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DataNodeTest {

    @Test
    void testThatGetSubNodeOrThrowDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("subnode", new HashMap<>());
        final DataNode node = new DataNode(data, "", "current");

        final DataNode subnode = node.getSubNodeOrThrow("subnode");
        assertEquals("current.subnode", subnode.getPath(), "Invalid subnode path");
        assertEquals("subnode", subnode.getKey(), "Invalid subnode key");
    }

    @Test
    void testThatGetSubNodeOrThrowThrowsForWrongType() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("subnode", "not a map");
        final DataNode node = new DataNode(data, "", "current");

        assertThrows(SerializationException.class, () -> node.getSubNodeOrThrow("subnode"));
    }

    @Test
    void testThatGetSubNodeOrThrowThrowsForMissingKey() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("subnode", new HashMap<>());
        final DataNode node = new DataNode(data, "", "current");

        assertThrows(SerializationException.class, () -> node.getSubNodeOrThrow("missing"));
    }

    @Test
    void testThatGetSubNodeDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("subnode", new HashMap<>());
        final DataNode node = new DataNode(data, "", "current");

        final Optional<DataNode> subnodeOpt = node.getSubNode("subnode");
        assertFalse(subnodeOpt.isEmpty(), "Subnode should exist");
        DataNode subnode = subnodeOpt.get();
        assertEquals("current.subnode", subnode.getPath(), "Invalid subnode path");
        assertEquals("subnode", subnode.getKey(), "Invalid subnode key");
    }

    @Test
    void testThatGetSubNodeThrowsForWrongType() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("subnode", "not a map");
        final DataNode node = new DataNode(data, "", "current");
        assertThrows(SerializationException.class, () -> node.getSubNode("subnode"));
    }

    @Test
    void testThatGetCollectionOrThrowDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("key", List.of("value"));
        final DataNode node = new DataNode(data, "", "current");
        assertEquals(List.of("value"), node.getCollectionOrThrow("key", String.class));
    }

    @Test
    void testThatGetCollectionOrThrowThrowsForMissingKey() {
        final DataNode node = new DataNode(new HashMap<>(), "", "current");
        assertThrows(SerializationException.class, () -> node.getCollectionOrThrow("missing", String.class));
    }

    @Test
    void testThatGetCollectionOrThrowThrowsForWrongType() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("key", List.of("value"));
        final DataNode node = new DataNode(data, "", "current");
        assertThrows(SerializationException.class, () -> node.getCollectionOrThrow("key", Integer.class));
    }

    @Test
    void testThatGetCollectionOrNullDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("key", List.of("value"));
        final DataNode node = new DataNode(data, "", "current");
        assertEquals(List.of("value"), node.getCollectionOrNull("key", String.class));
    }

    @Test
    void testThatGetCollectionOrNullDoesNotThrowForMissingKey() throws SerializationException {
        final DataNode node = new DataNode(new HashMap<>(), "", "current");
        assertNull(node.getCollectionOrNull("missing", String.class));
    }

    @Test
    void testThatGetCollectionOrNullThrowsForWrongType() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("key", List.of("value"));
        final DataNode node = new DataNode(data, "", "current");
        assertThrows(SerializationException.class, () -> node.getCollectionOrNull("key", Integer.class));
    }

    @Test
    void testThatGetCollectionDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("key", List.of("value"));
        final DataNode node = new DataNode(data, "", "current");
        Optional<Collection<String>> opt = node.getCollection("key", String.class);
        assertTrue(opt.isPresent(), "No value found");
        assertEquals(List.of("value"), opt.get());
    }

    @Test
    void testThatGetCollectionDoesNotThrowForMissingKey() throws SerializationException {
        final DataNode node = new DataNode(new HashMap<>(), "", "current");
        assertTrue(node.getCollection("missing", String.class).isEmpty());
    }

    @Test
    void testThatGetCollectionThrowsForWrongType() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("key", List.of("value"));
        final DataNode node = new DataNode(data, "", "current");
        assertThrows(SerializationException.class, () -> node.getCollection("key", Integer.class));
    }

    @Test
    void testThatGetOrThrowDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("key", "value");
        final DataNode node = new DataNode(data, "", "current");
        assertEquals("value", node.getOrThrow("key", String.class));
    }

    @Test
    void testThatGetOrThrowThrowsForMissingKey() {
        final DataNode node = new DataNode(new HashMap<>(), "", "current");
        assertThrows(SerializationException.class, () -> node.getOrThrow("missing", String.class));
    }

    @Test
    void testThatGetOrThrowThrowsForWrongType() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("key", "value");
        final DataNode node = new DataNode(data, "", "current");
        assertThrows(SerializationException.class, () -> node.getOrThrow("key", Integer.class));
    }

    @Test
    void testThatGetOrNullDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("key", "value");
        final DataNode node = new DataNode(data, "", "current");
        assertEquals("value", node.getOrNull("key", String.class));
    }

    @Test
    void testThatGetOrNullDoesNotThrowForMissingKey() throws SerializationException {
        final DataNode node = new DataNode(new HashMap<>(), "", "current");
        assertNull(node.getOrNull("missing", String.class));
    }

    @Test
    void testThatGetOrNullThrowsForWrongType() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("key", "value");
        final DataNode node = new DataNode(data, "", "current");
        assertThrows(SerializationException.class, () -> node.getOrNull("key", Integer.class));
    }

    @Test
    void testThatGetDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("key", "value");
        final DataNode node = new DataNode(data, "", "current");
        Optional<String> opt = node.get("key", String.class);
        assertTrue(opt.isPresent(), "No value found");
        assertEquals("value", opt.get());
    }

    @Test
    void testThatGetDoesNotThrowForMissingKey() throws SerializationException {
        final DataNode node = new DataNode(new HashMap<>(), "", "current");
        assertTrue(node.get("missing", String.class).isEmpty());
    }

    @Test
    void testThatGetThrowsForWrongType() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("key", "value");
        final DataNode node = new DataNode(data, "", "current");
        assertThrows(SerializationException.class, () -> node.get("key", Integer.class));
    }

    @Test
    void testThatGetMapOrThrowDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> data = new HashMap<>();
        HashMap<Object, Object> mapData = new HashMap<>();
        mapData.put("k1", "v1");
        data.put("key", mapData);
        final DataNode node = new DataNode(data, "", "current");

        HashMap<String, String> expected = new HashMap<>();
        expected.put("k1", "v1");
        assertEquals(expected, node.getMapOrThrow("key", String.class, String.class));
    }

    @Test
    void testThatGetMapOrThrowThrowsForMissingKey() {
        final DataNode node = new DataNode(new HashMap<>(), "", "current");
        assertThrows(SerializationException.class, () -> node.getMapOrThrow("missing", String.class, String.class));
    }

    @Test
    void testThatGetMapOrThrowThrowsForWrongType() {
        HashMap<Object, Object> data = new HashMap<>();
        HashMap<Object, Object> mapData = new HashMap<>();
        mapData.put("k1", "v1");
        data.put("key", mapData);
        final DataNode node = new DataNode(data, "", "current");
        assertThrows(SerializationException.class, () -> node.getMapOrThrow("key", Integer.class, Integer.class));
    }

    @Test
    void testThatGetMapOrNullDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> data = new HashMap<>();
        HashMap<Object, Object> mapData = new HashMap<>();
        mapData.put("k1", "v1");
        data.put("key", mapData);
        final DataNode node = new DataNode(data, "", "current");

        HashMap<String, String> expected = new HashMap<>();
        expected.put("k1", "v1");
        assertEquals(expected, node.getMapOrNull("key", String.class, String.class));
    }

    @Test
    void testThatGetMapOrNullDoesNotThrowForMissingKey() throws SerializationException {
        final DataNode node = new DataNode(new HashMap<>(), "", "current");
        assertNull(node.getMapOrNull("missing", String.class, String.class));
    }

    @Test
    void testThatGetMapOrNullThrowsForWrongType() {
        HashMap<Object, Object> data = new HashMap<>();
        HashMap<Object, Object> mapData = new HashMap<>();
        mapData.put("k1", "v1");
        data.put("key", mapData);
        final DataNode node = new DataNode(data, "", "current");
        assertThrows(SerializationException.class, () -> node.getMapOrNull("key", Integer.class, Integer.class));
    }

    @Test
    void testThatGetMapDoesNotThrowForExistingKey() throws SerializationException {
        HashMap<Object, Object> data = new HashMap<>();
        HashMap<Object, Object> mapData = new HashMap<>();
        mapData.put("k1", "v1");
        data.put("key", mapData);
        final DataNode node = new DataNode(data, "", "current");

        Optional<Map<String, String>> opt = node.getMap("key", String.class, String.class);
        assertTrue(opt.isPresent(), "No value found");
        HashMap<String, String> expected = new HashMap<>();
        expected.put("k1", "v1");
        assertEquals(expected, opt.get());
    }

    @Test
    void testThatGetMapDoesNotThrowForMissingKey() throws SerializationException {
        final DataNode node = new DataNode(new HashMap<>(), "", "current");
        assertTrue(node.getMap("missing", String.class, String.class).isEmpty());
    }

    @Test
    void testThatGetMapThrowsForWrongType() {
        HashMap<Object, Object> data = new HashMap<>();
        HashMap<Object, Object> mapData = new HashMap<>();
        mapData.put("k1", "v1");
        data.put("key", mapData);
        final DataNode node = new DataNode(data, "", "current");
        assertThrows(SerializationException.class, () -> node.getMap("key", Integer.class, Integer.class));
    }

}