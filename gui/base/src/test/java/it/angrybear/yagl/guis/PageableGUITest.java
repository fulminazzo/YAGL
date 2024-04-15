package it.angrybear.yagl.guis;

import it.angrybear.yagl.Metadatable;
import it.angrybear.yagl.TestUtils;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.viewers.Viewer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PageableGUITest {

    @Test
    void testPageableGUIMethods() throws InvocationTargetException, IllegalAccessException {
        GUI expected = setupGUI(GUI.newGUI(9));
        GUI actual = setupGUI(PageableGUI.newGUI(9));

        for (Method method : GUI.class.getDeclaredMethods()) {
            try {
                if (method.equals(GUI.class.getDeclaredMethod("open", Viewer.class))) continue;
                if (method.getName().contains("copy")) continue;
                Metadatable.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
                continue;
            } catch (NoSuchMethodException ignored) {}
            method = ReflectionUtils.setAccessible(method);
            Object[] params = Arrays.stream(method.getParameterTypes())
                    .map(TestUtils::mockParameter)
                    .map(o -> o instanceof Integer ? 0 : o)
                    .toArray(Object[]::new);
            Object obj1 = method.invoke(expected, params);
            Object obj2 = method.invoke(actual, params);
            if (obj1 != null && obj1.getClass().isArray())
                assertArrayEquals((Object[]) obj1, (Object[]) obj2);
            else if (!(obj1 instanceof GUI)) assertEquals(obj1, obj2);

            GUI template = new Refl<>(actual).getFieldObject("templateGUI");
            assertEquals(expected, template);
        }
    }

    @Test
    void testSetInvalidPages() {
        assertThrowsExactly(IllegalArgumentException.class, () -> PageableGUI.newGUI(GUIType.CHEST).setPages(-1));
    }

    @Test
    void testGetGUIPageException() {
        assertThrowsExactly(IndexOutOfBoundsException.class, () -> PageableGUI.newGUI(1).getPage(1));
    }

    @Test
    void testReturnTypes() {
        TestUtils.testReturnType(PageableGUI.newGUI(9), GUI.class, m -> m.getName().equals("copy"));
    }

    @Test
    void testLowerSlot() {
        assertThrowsExactly(IllegalArgumentException.class, () -> PageableGUI.newGUI(9).setNextPage(-1, Item.newItem()));
    }

    @Test
    void testHigherSlot() {
        assertThrowsExactly(IllegalArgumentException.class, () -> PageableGUI.newGUI(9).setPreviousPage(10, Item.newItem()));
    }

    private GUI setupGUI(GUI gui) {
        return gui.setTitle("hello world")
                .setMovable(3, true)
                .setMovable(4, false)
                .addContent(Item.newItem().setMaterial("stone"))
                .setContents(3, Item.newItem().setMaterial("diamond"))
                .setContents(4, Item.newItem().setMaterial("diamond"))
                .unsetContent(3)
                .onCloseGUI("command")
                .onClickOutside("command")
                .onOpenGUI("command")
                .onChangeGUI("command");
    }
}