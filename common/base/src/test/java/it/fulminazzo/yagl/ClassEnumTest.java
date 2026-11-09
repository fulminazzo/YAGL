package it.fulminazzo.yagl;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void testClassesNames() {
        assertEquals("ONE", First.ONE.name());
        assertEquals("TWO", First.TWO.name());
        assertEquals("THREE", First.THREE.name());
        assertEquals("ONE", Second.ONE.name());
        assertEquals("TWO", Second.TWO.name());
        assertEquals("THREE", Second.THREE.name());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void testThatValueOfOrdinalsReturnCorrectValues(final int ordinal) {
        assertEquals(getFirstValues().get(ordinal), First.valueOf(ordinal));
    }
    
    @Test
    void testThatValueOfOrdinalsThrowsForInvalidOrdinal() {
        assertThrows(IllegalArgumentException.class, () -> First.valueOf(3));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ONE", "TWO", "THREE"})
    void testThatValueOfNamesReturnCorrectValues(final String name) {
        First value = name.equals("ONE") ? First.ONE : name.equals("TWO") ? First.TWO : First.THREE;
        assertEquals(value, First.valueOf(name));
    }
    
    @Test
    void testThatValueOfNamesThrowsForInvalidNames() {
        assertThrows(IllegalArgumentException.class, () -> First.valueOf("FOUR"));
    }

    @Test
    void testThatValuesReturnCorrectValues() {
        assertArrayEquals(getFirstValues().toArray(new First[3]), First.values());
    }

    private static List<First> getFirstValues() {
        return Arrays.asList(First.ONE, First.TWO, First.THREE);
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

        public static @NotNull First valueOf(final int index) {
            return valueOf(index, First.class);
        }

        public static First valueOf(final @NotNull String color) {
            return valueOf(color, First.class);
        }

        public static First @NotNull [] values() {
            return values(First.class);
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
