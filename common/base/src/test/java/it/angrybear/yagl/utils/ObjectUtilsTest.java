package it.angrybear.yagl.utils;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testCopyOfData() {
        GeneralCopy c1 = new GeneralCopy();
        GeneralCopy c2 = ObjectUtils.copy(c1);
        assertNotEquals(c1.list.getClass(), c2.list.getClass());
        assertIterableEquals(c1.list, c2.list);
        assertNotEquals(c1.array, c2.array);
        assertArrayEquals(c1.array, c2.array);
        assertNotEquals(c1.copiable, c2.copiable);
    }

    @Test
    void testCopyThrowsIllegalArgument() {
        Throwable exception = assertThrowsExactly(IllegalArgumentException.class, () ->
                ObjectUtils.copy(new GeneralCopyException()));
        assertEquals("Everything good", exception.getMessage());
    }

    private static class GeneralCopy {
        List<String> list = Arrays.asList("hello", "world");
        String[] array = new String[]{"hello", "world"};
        GeneralCopiable copiable = new GeneralCopiable();
    }

    private static class GeneralCopyException {
        GeneralCopiableException copiable = new GeneralCopiableException();
    }

    private static class GeneralCopiable {

        public GeneralCopiable copy() {
            return new GeneralCopiable();
        }
    }

    private static class GeneralCopiableException {

        public GeneralCopiable copy() {
            throw new IllegalArgumentException("Everything good");
        }
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