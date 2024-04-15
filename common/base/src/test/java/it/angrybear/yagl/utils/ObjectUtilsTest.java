package it.angrybear.yagl.utils;

import org.junit.jupiter.api.Test;

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

    private static class CopyNoInterfaceImpl { }

    private static class CopyNoInterface {

        public CopyNoInterface(String s) {

        }
    }
}