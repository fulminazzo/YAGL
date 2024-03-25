package it.angrybear.yagl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void testGetterMethods() {
        Color color = Color.RED;
        assertEquals(255, color.getAlpha());
        assertEquals(255, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(0, color.getBlue());
    }

    @Test
    void testFromAndToARGB() {
        String argb = "000F010A";
        Color color = Color.fromARGB(argb);
        assertEquals("#" + argb.substring(2), color.toRGB());
        assertEquals("#" + argb, color.toARGB());
    }
}