package it.angrybear.yagl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void testFromAndToARGB() {
        String argb = "000F010A";
        Color color = Color.fromARGB(argb);
        assertEquals("#" + argb.substring(2), color.toRGB());
        assertEquals("#" + argb, color.toARGB());
    }
}