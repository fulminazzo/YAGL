package it.fulminazzo.yagl.guis;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class FullSizeGUITest {

    private static Object[] slots() {
        return new Object[][]{
                new Object[]{0, 0},
                new Object[]{27, 0},
                new Object[]{26, 26},
                new Object[]{28, 1},
                new Object[]{71, 44}
        };
    }

    @ParameterizedTest
    @MethodSource("slots")
    void testGetCorrespondingSlotReturnsCorrectSlot(int slot, int expected) {
        FullSizeGUI gui = new FullSizeGUI(27);
        int actual = gui.getCorrespondingSlot(slot);

        assertEquals(expected, actual);
    }

    private static Object[][] slotsGUIs() {
        return new Object[][]{
                new Object[]{0, TypeGUI.class},
                new Object[]{27, DefaultGUI.class},
                new Object[]{26, TypeGUI.class},
                new Object[]{28, DefaultGUI.class},
                new Object[]{71, DefaultGUI.class}
        };
    }

    @ParameterizedTest
    @MethodSource("slotsGUIs")
    void testGetCorrespondingGUIReturnsCorrectGUI(int slot, Class<? extends GUI> expectedClass) {
        FullSizeGUI gui = new FullSizeGUI(GUIType.CHEST);
        GUI actual = gui.getCorrespondingGUI(slot);

        assertInstanceOf(expectedClass, actual);
    }

}