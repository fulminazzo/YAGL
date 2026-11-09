package it.fulminazzo.yagl.structure;

import it.fulminazzo.yagl.function.TriFunction;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class TripleTest {

    @Test
    void testThatMapOnlyWorksIfAllValuesAreNotNull() {
        Triple<Integer, Integer, Integer> empty = new Triple<>();
        Triple<Integer, Integer, Integer> expected = new Triple<>(1, 2, 3);
        TriFunction<String, String, String, Triple<Integer, Integer, Integer>> function = (f, s, t) -> expected;

        assertEquals(empty, new Triple<String, String, String>().map(function), "Should not execute if null, null and null");
        assertEquals(empty, new Triple<String, String, String>("first", null, null).map(function), "Should not execute if first, null and null");
        assertEquals(empty, new Triple<String, String, String>(null, "second", null).map(function), "Should not execute if null, second and null");
        assertEquals(empty, new Triple<String, String, String>("first", "second", null).map(function), "Should not execute if first, second and null");
        assertEquals(empty, new Triple<String, String, String>(null, null, "third").map(function), "Should not execute if null, null and null");
        assertEquals(empty, new Triple<String, String, String>("first", null, "third").map(function), "Should not execute if first, null and null");
        assertEquals(empty, new Triple<String, String, String>(null, "second", "third").map(function), "Should not execute if null, second and null");
        assertEquals(expected, new Triple<>("first", "second", "third").map(function), "Should execute if first, second and null");
    }

    @Test
    void testThatIfPresentOnlyExecutesIfAllValuesAreNotNull() {
        Triple<String, String, String> triple = new Triple<>();
        AtomicBoolean check = new AtomicBoolean(false);

        triple.ifPresent((f, s, t) -> check.set(true));
        assertFalse(check.get(), "Should not be set if null, null and null");

        triple.set("first", null, null);
        triple.ifPresent((f, s, t) -> check.set(true));
        assertFalse(check.get(), "Should not be set if first, null and null");

        triple.set(null, "second", null);
        triple.ifPresent((f, s, t) -> check.set(true));
        assertFalse(check.get(), "Should not be set if null, second and null");

        triple.set("first", "second", null);
        triple.ifPresent((f, s, t) -> check.set(true));
        assertFalse(check.get(), "Should not be set if first, second and null");

        triple.set(null, null, "third");
        triple.ifPresent((f, s, t) -> check.set(true));
        assertFalse(check.get(), "Should not be set if null, null and third");

        triple.set("first", null, "third");
        triple.ifPresent((f, s, t) -> check.set(true));
        assertFalse(check.get(), "Should not be set if first, null and third");

        triple.set(null, "second", "third");
        triple.ifPresent((f, s, t) -> check.set(true));
        assertFalse(check.get(), "Should not be set if null, second and third");

        triple.set("first", "second", "third");
        triple.ifPresent((f, s, t) -> check.set(true));
        assertTrue(check.get(), "Should be set if first, second and third");
    }

}