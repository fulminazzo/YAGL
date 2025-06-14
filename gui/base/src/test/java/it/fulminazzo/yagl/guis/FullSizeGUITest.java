package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.TestUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

    private static Object[][] slotsGUIsClasses() {
        return new Object[][]{
                new Object[]{0, TypeGUI.class},
                new Object[]{27, DefaultGUI.class},
                new Object[]{26, TypeGUI.class},
                new Object[]{28, DefaultGUI.class},
                new Object[]{71, DefaultGUI.class}
        };
    }

    @ParameterizedTest
    @MethodSource("slotsGUIsClasses")
    void testGetCorrespondingGUIReturnsCorrectGUI(int slot, Class<? extends GUI> expectedClass) {
        FullSizeGUI gui = new FullSizeGUI(GUIType.CHEST);
        GUI actual = gui.getCorrespondingGUI(slot);

        assertInstanceOf(expectedClass, actual);
    }

    private static Object[][] slotsGUIs() {
        return new Object[][]{
                new Object[]{0, 0, GUI.newGUI(GUIType.CHEST)},
                new Object[]{27, 0, GUI.newGUI(FullSizeGUI.SECOND_INVENTORY_SIZE)},
                new Object[]{26, 26, GUI.newGUI(GUIType.CHEST)},
                new Object[]{28, 1, GUI.newGUI(FullSizeGUI.SECOND_INVENTORY_SIZE)},
                new Object[]{71, 44, GUI.newGUI(FullSizeGUI.SECOND_INVENTORY_SIZE)}
        };
    }

    @ParameterizedTest
    @MethodSource("slotsGUIs")
    void testIsMovableReturnsCorrectValue(int slot, int actual, GUI internalGUI) {
        FullSizeGUI gui = setupGUI(internalGUI);

        internalGUI.setMovable(actual, true);

        assertTrue(gui.isMovable(slot));
    }

    private static FullSizeGUI setupGUI(GUI internalGUI) {
        Refl<FullSizeGUI> refl = new Refl<>(new FullSizeGUI(GUIType.CHEST));
        if (internalGUI instanceof TypeGUI) refl.setFieldObject("upperGUI", internalGUI);
        else refl.setFieldObject("lowerGUI", internalGUI);
        return refl.getObject();
    }

    @ParameterizedTest
    @ValueSource(strings = {"setTitle", "getTitle",
            "onClickOutside", "clickOutsideAction",
            "onOpenGUI", "openGUIAction",
            "onCloseGUI", "closeGUIAction",
            "onChangeGUI", "changeGUIAction",
            "variables"})
    void testFullSizeGUIDelegatesToUpperGUI(String methodName) {
        Refl<?> fullSizeGUI = new Refl<>(new FullSizeGUI(9));

        GUI upperGUI = mock(GUI.class);
        fullSizeGUI.setFieldObject("upperGUI", upperGUI);

        Method method = Arrays.stream(FullSizeGUI.class.getMethods())
                .filter(m -> m.getName().equals(methodName))
                .findFirst().orElseThrow(IllegalStateException::new);

        Object[] parameters = Arrays.stream(method.getParameterTypes())
                .map(TestUtils::mockParameter)
                .toArray(Object[]::new);

        fullSizeGUI.invokeMethod(methodName, parameters);

        new Refl<>(verify(upperGUI)).invokeMethod(methodName, parameters);
    }

}