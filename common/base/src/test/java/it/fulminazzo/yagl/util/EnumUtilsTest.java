package it.fulminazzo.yagl.util;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumUtilsTest {

    @Nested
    class ValueOfTest {

        @Test
        void shouldThrowRealException() {
            assertThrowsExactly(RuntimeException.class, () -> EnumUtils.valueOf(mockClassEnum.class, "any"));
        }

        @Test
        void testValid() {
            assertEquals(MockEnum.VALUE1, EnumUtils.valueOf(MockEnum.class, MockEnum.VALUE1.name().toLowerCase()));
        }

        @Test
        void shouldThrowRealRuntimeException() {
            assertThrowsExactly(IllegalStateException.class, () -> EnumUtils.valueOf(mockClassEnum.class, "any", "valueOfRuntime"));
        }

        @Test
        void shouldThrowRealIllegalException() {
            assertThrowsExactly(IllegalArgumentException.class, () -> EnumUtils.valueOf(mockClassEnum.class, "any", "valueOfIllegal"));
        }

        @Test
        void shouldThrowRealIllegalExceptionWithMessage() {
            assertThrowsExactly(IllegalArgumentException.class, () -> EnumUtils.valueOf(mockClassEnum.class, "any", "valueOfIllegalMessage"));
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

    @Nested
    class GetTypeNameTest {

        @Test
        void testGetTypeName() {
            String actual = new Refl<>(EnumUtils.class).invokeMethod("getTypeName", mockClassEnum.class);
            assertEquals("mock class enum", actual);
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

        public static mockClassEnum valueOfRuntime(String name) {
            throw new IllegalStateException("Just a mock");
        }

        public static mockClassEnum valueOfIllegal(String name) {
            throw new IllegalArgumentException();
        }

        public static mockClassEnum valueOfIllegalMessage(String name) {
            throw new IllegalArgumentException("Just a mock");
        }
    }
}