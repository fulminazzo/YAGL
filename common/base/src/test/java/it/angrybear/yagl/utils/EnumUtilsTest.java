package it.angrybear.yagl.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumUtilsTest {

    @Test
    void shouldThrowRealException() {
        assertThrowsExactly(ArithmeticException.class, () -> EnumUtils.valueOf(MockClassEnum.class, "any"));
    }

    @Test
    void testValid() {
        assertEquals(MockEnum.VALUE1, EnumUtils.valueOf(MockEnum.class, MockEnum.VALUE1.name().toLowerCase()));
    }

    @Test
    void testValidComplete() {
        assertEquals(MockEnum.VALUE1, EnumUtils.valueOf(MockEnum.class, MockEnum.VALUE1.name().toLowerCase(),
                "value", "valueOf"));
    }

    @Test
    void testInvalidMethod() {
        assertThrows(Exception.class, () -> EnumUtils.valueOf(MockEnum.class, MockEnum.VALUE1.name().toLowerCase(),
                "value", "invalid", "valueOf"));
    }

    @Test
    void testInvalid() {
        assertThrowsExactly(IllegalArgumentException.class, () -> EnumUtils.valueOf(MockEnum.class, "VALUE3"));
    }

    private enum MockEnum {
        VALUE1;

        public static MockEnum value(String name) {
            return null;
        }

        /**
         * Simulate invalid valueOf method
         * @param name the name
         */
        public static void invalid(String name) {

        }
    }

    private static class MockClassEnum {

        public static MockClassEnum valueOf(String name) {
            throw new ArithmeticException("Just a mock");
        }
    }
}