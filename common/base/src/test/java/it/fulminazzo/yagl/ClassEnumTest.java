package it.fulminazzo.yagl;

import it.fulminazzo.yagl.reflect.Reflect;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassEnumTest {

    @Test
    void testClassesOrdinal() {
        for (int i = 0; i < First.LIST.size(); i++) {
            First expected = First.LIST.get(i);
            First actual = ClassEnumTest.values(First.class)[i];
            assertEquals(i, actual.ordinal());
            assertEquals(expected, actual);
        }
        for (int i = 0; i < Second.LIST.size(); i++) {
            Second expected = Second.LIST.get(i);
            Second actual = ClassEnumTest.values(Second.class)[i];
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

    @SuppressWarnings("unchecked")
    private static <E extends ClassEnum> E[] values(final @NotNull Class<E> type) {
        Reflect reflect = Reflect.on(type);
        return reflect.getStaticFields().stream()
                .filter(f -> f.getType().isAssignableFrom(type))
                .map(f -> (E) reflect.get(f).get())
                .toArray(l -> (E[]) Array.newInstance(type, l));
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
