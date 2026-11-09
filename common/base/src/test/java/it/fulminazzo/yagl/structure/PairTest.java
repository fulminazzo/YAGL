package it.fulminazzo.yagl.structure;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    @Test
    void testThatMapOnlyWorksIfAllValuesAreNotNull() {
        Pair<Integer, Integer> empty = new Pair<>();
        Pair<Integer, Integer> expected = new Pair<>(1, 2);
        BiFunction<String, String, Pair<Integer, Integer>> function = (f, s) -> expected;

        assertEquals(empty, new Pair<String, String>().map(function), "Should not execute if null and null");
        assertEquals(empty, new Pair<String, String>("first", null).map(function), "Should not execute if first and null");
        assertEquals(empty, new Pair<String, String>(null, "second").map(function), "Should not execute if null and second");
        assertEquals(expected, new Pair<>("first", "second").map(function), "Should execute if first and second");
    }

    @Test
    void testThatIfPresentOnlyExecutesIfAllValuesAreNotNull() {
        Pair<String, String> pair = new Pair<>();
        AtomicBoolean check = new AtomicBoolean(false);

        pair.ifPresent((f, s) -> check.set(true));
        assertFalse(check.get(), "Should not be set if null and null");

        pair.set("first", null);
        pair.ifPresent((f, s) -> check.set(true));
        assertFalse(check.get(), "Should not be set if first and null");

        pair.set(null, "second");
        pair.ifPresent((f, s) -> check.set(true));
        assertFalse(check.get(), "Should not be set if null and second");

        pair.set("first", "second");
        pair.ifPresent((f, s) -> check.set(true));
        assertTrue(check.get(), "Should be set if first and second");
    }

}