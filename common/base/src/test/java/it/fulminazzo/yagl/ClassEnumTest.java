package it.fulminazzo.yagl;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClassEnumTest {

    @Test
    void testClassesOrdinal() {
        for (int i = 0; i < First.LIST.size(); i++) {
            First expected = First.LIST.get(i);
            First actual = First.values(First.class)[i];
            assertEquals(i, actual.ordinal());
            assertEquals(expected, actual);
        }
        for (int i = 0; i < Second.LIST.size(); i++) {
            Second expected = Second.LIST.get(i);
            Second actual = Second.values(Second.class)[i];
            assertEquals(i, actual.ordinal());
            assertEquals(expected, actual);
        }
    }

    @Test
    void testInvalidOrdinal() {
        First INVALID = mock(First.class);
        when(INVALID.name()).thenCallRealMethod();
        when(INVALID.ordinal()).thenCallRealMethod();
        assertThrowsExactly(IllegalArgumentException.class, INVALID::ordinal);
    }

    @Test
    void testInvalidValueOfIndex() {
        assertThrowsExactly(IllegalArgumentException.class, () -> First.valueOf(4, First.class));
    }

    private static class First extends ClassEnum {
        public static final List<First> LIST = new LinkedList<>();

        public static final First ONE = new First();
        public static final First TWO = new First();
        public static final First THREE = new First();

        public First() {
            super();
            LIST.add(this);
        }

    }


    private static class Second extends ClassEnum {
        public static final List<Second> LIST = new LinkedList<>();

        public static final Second ONE = new Second();
        public static final Second TWO = new Second();
        public static final Second THREE = new Second();

        public Second() {
            super();
            LIST.add(this);
        }

    }

}
