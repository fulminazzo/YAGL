package it.fulminazzo.yagl.structure;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

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