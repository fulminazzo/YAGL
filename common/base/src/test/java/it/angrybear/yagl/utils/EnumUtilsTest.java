package it.angrybear.yagl.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumUtilsTest {

    @Test
    void testValid() {
        assertEquals(MockEnum.VALUE1, EnumUtils.valueOf(MockEnum.class, MockEnum.VALUE1.name().toLowerCase()));
    }

    @Test
    void testInvalid() {
        assertThrowsExactly(IllegalArgumentException.class, () -> EnumUtils.valueOf(MockEnum.class, "VALUE3"));
    }

    private enum MockEnum {
        VALUE1
    }
}