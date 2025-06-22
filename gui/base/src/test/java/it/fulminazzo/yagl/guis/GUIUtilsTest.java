package it.fulminazzo.yagl.guis;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class GUIUtilsTest {

    @ParameterizedTest
    @ValueSource(ints = {
            -1, -10, 9, 10, 21
    })
    void testCheckSlotThrows(int slot) {
        assertThrowsExactly(IndexOutOfBoundsException.class, () -> GUIUtils.checkSlot(slot, 9));
    }

}