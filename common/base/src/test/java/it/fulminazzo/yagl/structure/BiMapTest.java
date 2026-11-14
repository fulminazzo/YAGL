package it.fulminazzo.yagl.structure;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BiMapTest {

    @Test
    void testEmptyBiMapIsInitiallyEmpty() {
        BiMap<String, Integer> biMap = new BiMap<>();
        assertTrue(biMap.isEmpty());
        assertEquals(0, biMap.size());
    }

    @Test
    void testPutAndGet() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put("one", 1);
        biMap.put("two", 2);
        biMap.put("three", 3);

        assertEquals(3, biMap.size());
        assertFalse(biMap.isEmpty());
        assertEquals(1, biMap.get("one"));
        assertEquals(2, biMap.get("two"));
        assertEquals(3, biMap.get("three"));
        assertNull(biMap.get("four"));
    }

    @Test
    void testPutReturnsPreviousValue() {
        BiMap<String, Integer> biMap = new BiMap<>();
        assertNull(biMap.put("one", 1));
        assertEquals(1, biMap.put("one", 2));
        assertEquals(2, biMap.get("one"));
    }

    @Test
    void testGetByValue() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put("one", 1);
        biMap.put("two", 2);

        assertEquals("one", biMap.getByValue(1));
        assertEquals("two", biMap.getByValue(2));
        assertNull(biMap.getByValue(3));
    }

    @Test
    void testBidirectionalUniqueness_ValueConflict() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put("one", 1);
        biMap.put("two", 1);

        assertEquals(1, biMap.size());
        assertEquals(1, biMap.get("two"));
        assertNull(biMap.get("one"));
        assertEquals("two", biMap.getByValue(1));
    }

    @Test
    void testBidirectionalUniqueness_KeyConflict() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put("one", 1);
        biMap.put("one", 2);

        assertEquals(1, biMap.size());
        assertEquals(2, biMap.get("one"));
        assertEquals("one", biMap.getByValue(2));
        assertNull(biMap.getByValue(1));
    }

    @Test
    void testContainsKeyAndContainsValue() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put("one", 1);

        assertTrue(biMap.containsKey("one"));
        assertFalse(biMap.containsKey("two"));
        assertTrue(biMap.containsValue(1));
        assertFalse(biMap.containsValue(2));
    }

    @Test
    void testPutAll() {
        BiMap<String, Integer> biMap = new BiMap<>();
        Map<String, Integer> initial = new HashMap<>();
        initial.put("one", 1);
        initial.put("two", 2);
        initial.put("three", 3);

        biMap.putAll(initial);

        assertEquals(3, biMap.size());
        assertEquals(1, biMap.get("one"));
        assertEquals(2, biMap.get("two"));
        assertEquals(3, biMap.get("three"));
        assertEquals("one", biMap.getByValue(1));
        assertEquals("two", biMap.getByValue(2));
        assertEquals("three", biMap.getByValue(3));
    }

    @Test
    void testRemoveByKey() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put("one", 1);
        biMap.put("two", 2);

        assertEquals(1, biMap.remove("one"));
        assertEquals(1, biMap.size());
        assertNull(biMap.get("one"));
        assertNull(biMap.getByValue(1));
        assertEquals(2, biMap.get("two"));
    }

    @Test
    void testRemoveNonExistentKey() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put("one", 1);

        assertNull(biMap.remove("nonexistent"));
        assertEquals(1, biMap.size());
    }

    @Test
    void testClear() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put("one", 1);
        biMap.put("two", 2);

        biMap.clear();

        assertTrue(biMap.isEmpty());
        assertEquals(0, biMap.size());
        assertNull(biMap.get("one"));
        assertNull(biMap.getByValue(1));
    }

    @Test
    void testKeySet() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put("one", 1);
        biMap.put("two", 2);

        assertTrue(biMap.keySet().contains("one"));
        assertTrue(biMap.keySet().contains("two"));
        assertFalse(biMap.keySet().contains("three"));
        assertEquals(2, biMap.keySet().size());
    }

    @Test
    void testEntrySet() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put("one", 1);
        biMap.put("two", 2);

        assertEquals(2, biMap.entrySet().size());
        assertTrue(biMap.entrySet().contains(Map.entry("one", 1)));
        assertTrue(biMap.entrySet().contains(Map.entry("two", 2)));
    }

    @Test
    void testPutNullKeyAndValue() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put(null, null);

        assertEquals(1, biMap.size());
        assertNull(biMap.get(null));
        assertNull(biMap.getByValue(null));
    }

    @Test
    void testPutNullKeyWithNonNullValue() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put(null, 42);

        assertEquals(1, biMap.size());
        assertEquals(42, biMap.get(null));
        assertNull(biMap.getByValue(42));
    }

    @Test
    void testPutNonNullKeyWithNullValue() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put("key", null);

        assertEquals(1, biMap.size());
        assertNull(biMap.get("key"));
        assertEquals("key", biMap.getByValue(null));
    }

    @Test
    void testConsistencyAfterMultipleConflicts() {
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put("a", 1);
        biMap.put("b", 2);
        biMap.put("c", 3);

        // "d" takes value 1 from "a"
        biMap.put("d", 1);
        assertNull(biMap.get("a"));
        assertEquals(1, biMap.get("d"));

        // "a" takes value 2 from "b"
        biMap.put("a", 2);
        assertNull(biMap.get("b"));
        assertEquals(2, biMap.get("a"));

        assertEquals(3, biMap.size());
        assertEquals(1, biMap.get("d"));
        assertEquals(2, biMap.get("a"));
        assertEquals(3, biMap.get("c"));
        assertEquals("d", biMap.getByValue(1));
        assertEquals("a", biMap.getByValue(2));
        assertEquals("c", biMap.getByValue(3));
    }

}
