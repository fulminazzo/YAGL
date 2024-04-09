package it.angrybear.yagl.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumUtilsTest {

    @Test
    void shouldThrowRealException() {
        assertThrowsExactly(RuntimeException.class, () -> EnumUtils.valueOf(MockClassEnum.class, "any"));
    }

    @Test
    void shouldThrowRealIllegalException() {
        assertThrowsExactly(IllegalArgumentException.class, () -> EnumUtils.valueOf(MockClassEnum.class, "any", "valueOfIllegal"));
    }

    @Test
    void shouldThrowRealIllegalExceptionWithMessage() {
        assertThrowsExactly(IllegalArgumentException.class, () -> EnumUtils.valueOf(MockClassEnum.class, "any", "valueOfIllegalMessage"));
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

        public static void invalid(String name) {
            //
        }
    }

    private static class MockClassEnum {

        public static MockClassEnum valueOf(String name) throws IllegalAccessException {
            throw new IllegalAccessException("Just a mock");
        }

        public static MockClassEnum valueOfIllegal(String name) {
            throw new IllegalArgumentException();
        }

        public static MockClassEnum valueOfIllegalMessage(String name) {
            throw new IllegalArgumentException("Just a mock");
        }
    }
}