package it.fulminazzo.yagl.serializer;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DataNodeTest {

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

}