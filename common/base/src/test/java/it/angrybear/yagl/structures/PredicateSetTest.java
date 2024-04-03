package it.angrybear.yagl.structures;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PredicateSetTest {

    @Test
    void testEquality() {
        Set<Integer> set1 = new PredicateSet<>(Objects::equals);
        Set<Integer> set2 = new PredicateSet<>(Objects::equals);
        set1.addAll(Arrays.asList(1, 2, 3, 1, 4, 5, 1, 2, 3));
        set2.addAll(Arrays.asList(1, 2, 3, 1, 4, 5, 1, 2, 3));
        assertEquals(set1, set2);
    }

    @Test
    void testReplace() {
        MockClass m1 = new MockClass();
        MockClass m2 = new MockClass();
        PredicateSet<MockClass> set = new PredicateSet<>((o1, o2) -> o1.hashCode() == o2.hashCode());
        set.add(m1);

        set.add(m2);
        MockClass first = set.stream().findFirst().orElse(null);
        assertEquals(m1.hashCode(), first.hashCode(), "Object should have been equal to m1");
        assertNotEquals(m2.hashCode(), first.hashCode(), "Object should have not been equal to m2");

        set.add(m2, true);
        first = set.stream().findFirst().orElse(null);
        if (first == null) fail("Expected first but none found");
        assertNotEquals(m1.hashCode(), first.hashCode(), "Object should have not been equal to m1");
        assertEquals(m2.hashCode(), first.hashCode(), "Object should have been equal to m2");
    }

    private static class MockClass {

        @Override
        public boolean equals(Object o) {
            if (o instanceof MockClass) return true;
            return super.equals(o);
        }
    }
}