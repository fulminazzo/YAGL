package it.fulminazzo.yagl.structure;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TripleTest {

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

        triple.set("first", null, null);
        triple.ifPresent((f, s, t) -> check.set(true));
        assertFalse(check.get(), "Should not be set if first, null and third");

        triple.set(null, "second", null);
        triple.ifPresent((f, s, t) -> check.set(true));
        assertFalse(check.get(), "Should not be set if null, second and third");

        triple.set("first", "second", null);
        triple.ifPresent((f, s, t) -> check.set(true));
        assertTrue(check.get(), "Should be set if first, second and third");
    }

}