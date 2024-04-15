package it.angrybear.yagl.utils;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ObjectUtilsTest {

    @Test
    void testCopyOfNoInterface() {
        Object current = new CopyNoInterface("hello");
        Object o = ObjectUtils.copy(current);
        assertInstanceOf(CopyNoInterfaceImpl.class, o);
        assertNotEquals(current, o);
    }

    @Test
    void testCopyOfIterable() {
        Object current = new CopyIterable("hello");
        Object o = ObjectUtils.copy(current);
        assertInstanceOf(CopyIterableImpl.class, o);
        assertNotEquals(current, o);
    }

    private static class CopyIterableImpl { }

    private static class CopyIterable implements Iterable<String> {

        public CopyIterable(String s) {

        }

        @NotNull
        @Override
        public Iterator<String> iterator() {
            return null;
        }
    }

    private static class CopyNoInterfaceImpl { }

    private static class CopyNoInterface {

        public CopyNoInterface(String s) {

        }
    }
}