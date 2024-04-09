package it.angrybear.yagl.utils;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumUtilsTest {

    @Nested
    class ValueOfTests {

        @Test
        void shouldThrowRealException() {
            assertThrowsExactly(RuntimeException.class, () -> EnumUtils.valueOf(mockClassEnum.class, "any"));
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

    // Bad practice, a class should always start with an uppercase letter.
    // This is just for testing purposes.
    private static class mockClassEnum {

        public static mockClassEnum valueOf(String name) throws IllegalAccessException {
            throw new IllegalAccessException("Just a mock");
        }
    }
}