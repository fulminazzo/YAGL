package it.angrybear.structures;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PredicateSetTest {

    @Test
    void testEquality() {
        Set<Integer> set1 = new PredicateSet<>(Objects::equals);
        Set<Integer> set2 = new PredicateSet<>(Objects::equals);
        set1.addAll(Arrays.asList(1, 2, 3, 1, 4, 5, 1, 2, 3));
        set2.addAll(Arrays.asList(1, 2, 3, 1, 4, 5, 1, 2, 3));
        assertEquals(set1, set2);
    }
}