package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    private static Object[] getNotEqualColors() {
        return new Object[]{
                null, "ffff00aa",
                Color.fromARGB("25ff00aa"),
                Color.fromARGB("ff3100aa"),
                Color.fromARGB("ffff01aa"),
                Color.fromARGB("ffff0077"),
                Color.fromARGB("aabbccdd"),
        };
    }

    @ParameterizedTest
    @MethodSource("getNotEqualColors")
    void testColorInequality(Object color) {
        assertNotEquals(Color.fromARGB("ff00aa"), color);
        if (color != null)
            assertNotEquals(Color.fromARGB("ff00aa").hashCode(), color.hashCode());
    }

    @Test
    void testValueOfIndex() {
        Color[] values = Color.values();
        for (int i = 0; i < values.length; i++) {
            Color expected = values[i];
            Color actual = Color.valueOf(i);
            assertEquals(expected, actual, String.format("Expected '%s' but got '%s' at index '%s'",
                    expected, actual, i));
        }
    }

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

    @Test
    void testInvalidColorNegative() {
        final int max = 4;
        for (int i = 0; i < max; i++) {
            Integer[] params = new Integer[max];
            Arrays.fill(params, 0);
            params[i] = -1;
            assertThrowsExactly(IllegalArgumentException.class, () -> new Refl<>(Color.class, (Object[]) params));
        }
    }

    @Test
    void testInvalidColorTooHigh() {
        final int max = 4;
        for (int i = 0; i < max; i++) {
            Integer[] params = new Integer[max];
            Arrays.fill(params, 0);
            params[i] = 256;
            assertThrowsExactly(IllegalArgumentException.class, () -> new Refl<>(Color.class, (Object[]) params));
        }
    }

    @Test
    void testInvalidARGB() {
        assertThrowsExactly(IllegalArgumentException.class, () -> Color.fromARGB("invalid"));
    }

}