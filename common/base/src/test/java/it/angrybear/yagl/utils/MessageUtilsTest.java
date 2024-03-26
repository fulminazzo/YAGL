package it.angrybear.yagl.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageUtilsTest {

    @Test
    void testNormal() {
        String color = "&7";
        assertEquals(color, MessageUtils.decolor(MessageUtils.color(color)));
    }

    @Test
    void testHex() {
        String color = "#FF00AA";
        assertEquals(color, MessageUtils.decolor(MessageUtils.color(color)));
    }
}