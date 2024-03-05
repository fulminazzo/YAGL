package it.angrybear.yagl.structures;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PComparatorSetTest {

    @Test
    void testAdd() {
        Set<Mock> set = new PComparatorSet<>((m1, m2) -> m1.value <= m2.value, (m1, m2) -> m1.name.equals(m2.name));
        Mock m1 = new Mock("mock", 10);
        Mock m2 = new Mock("not-mock", 10);
        Mock m3 = new Mock("mock", 20);
        set.add(m1);
        set.add(m2);
        set.add(m3);
        assertEquals(2, set.size(), "Set size should have been 2");
        Iterator<Mock> iterator = set.iterator();
        assertEquals(m2, iterator.next());
        assertEquals(m3, iterator.next());
        assertFalse(set.stream().anyMatch(m -> m.equals(m1)), "No mock should have been equal to m1");
    }

    private static class Mock {
        final String name;
        final int value;

        public Mock(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
}