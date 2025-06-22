package it.fulminazzo.yagl.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserUtilsTest {

    @Test
    void testInvalidTypeForParserUtils() {
        assertThrowsExactly(IllegalArgumentException.class, () ->
                ParserUtils.typeToClass(ParserUtils.class, "any"));
    }

}