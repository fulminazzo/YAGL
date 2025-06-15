package it.fulminazzo.yagl.guis;

import it.fulminazzo.yagl.Metadatable;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.actions.GUIItemAction;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.contents.ItemGUIContent;
import it.fulminazzo.yagl.items.Item;
import it.fulminazzo.yagl.viewers.Viewer;
import it.fulminazzo.yagl.wrappers.Sound;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.structures.tuples.Tuple;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PageableGUITest {

    @ParameterizedTest
    @ValueSource(strings = {
            "topSlots", "leftSlots", "bottomSlots", "rightSlots",
            "northWest", "north", "northEast",
            "middleLine", "middleWest", "middle", "middleEast",
            "southLine", "southWest", "south", "southEast",
            "rows", "columns"
    })
    void testThatPageableGUIDelegatesToInternalTemplateGUI(String methodName) {
        GUI templateGUI = mock(GUI.class);

        PageableGUI pageableGUI = new PageableGUI(templateGUI);

        new Refl<>(pageableGUI).invokeMethod(methodName);

        new Refl<>(verify(templateGUI)).invokeMethod(methodName);
    }

    @Test
    void testCopyAllCopiesPageableInternalData() {
        PageableGUI gui = PageableGUI.newGUI(GUIType.ANVIL).setPages(1);
        gui.getPage(0).setContents(1, Item.newItem("red_concrete"));
        gui.setVariable("hello", "world");
        gui.setPreviousPage(2, Item.newItem("blue_concrete"));
        gui.setNextPage(3, Item.newItem("green_concrete"));

        PageableGUI newGUI = PageableGUI.newGUI(GUIType.ANVIL).copyFrom(gui, true);

        assertEquals(gui.templateGUI, newGUI.templateGUI, "TemplateGUI was not equal");
        assertEquals(gui.pages(), newGUI.pages(), "Pages were not equal");
        assertEquals(gui.variables(), newGUI.variables(), "Variables were not equal");
        assertEquals(gui.previousPage, newGUI.previousPage, "Previous page was not equal");
        assertEquals(gui.nextPage, newGUI.nextPage, "Next page was not equal");
    }

    @Test
    void testOpen() {
        PageableGUI gui = PageableGUI.newGUI(9).setPages(1);
        gui.getPage(0).setContents(1, Item.newItem("red_concrete"));

        final MockViewer viewer = new MockViewer(UUID.randomUUID(), "Steve");
        try (MockedStatic<ReflectionUtils> clazz = mockStatic(ReflectionUtils.class, CALLS_REAL_METHODS)) {
            clazz.when(() -> ReflectionUtils.getClass("it.fulminazzo.yagl.GUIAdapter")).thenReturn(MockGUIAdapter.class);

            gui.open(viewer);
            GUI expected = gui.getPage(0)
                    .setVariable("next_page", "2")
                    .setVariable("pages", "1")
                    .setVariable("page", "1")
                    .setVariable("previous_page", "0");
            GUI actual = viewer.openedGUI;

            assertEquals(expected, actual);
        }
    }

    @Test
    void testOpenPage() {
        PageableGUI gui = PageableGUI.newGUI(9)
                .setPreviousPage(0, Item.newItem("redstone_block")
                        .setDisplayName("&7Go to page &e<previous_page>"))
                .setNextPage(8, Item.newItem("emerald_block")
                        .setDisplayName("&7Go to page &e<next_page>"))
                .setPages(3)
                .setContents(4, Item.newItem("obsidian").setDisplayName("&7Page: &e<page>"));
        gui.getPage(0).setContents(1, Item.newItem("red_concrete"));
        gui.getPage(1).setContents(1, Item.newItem("lime_concrete"));
        gui.getPage(2).setContents(1, Item.newItem("yellow_concrete"));

        final MockViewer viewer = new MockViewer(UUID.randomUUID(), "Steve");
        try (MockedStatic<ReflectionUtils> clazz = mockStatic(ReflectionUtils.class, CALLS_REAL_METHODS)) {
            clazz.when(() -> ReflectionUtils.getClass("it.fulminazzo.yagl.GUIAdapter"))
                    .thenReturn(MockGUIAdapter.class);

            for (int i = 0; i < gui.pages(); i++) {
                gui.open(viewer, i);
                GUI expected = generateExpected(gui.getPage(i), i);
                GUI actual = viewer.openedGUI;
                if (i > 1) {
                    GUIContent content = actual.getContents(0).get(0);
                    content.clickItemAction().ifPresent(a -> a.execute(viewer, actual, content));
                    assertIterableEquals(gui.getPage(i - 1).getContents(1), viewer.openedGUI.getContents(1));
                }
                actual.getContents(0).forEach(e -> e.onClickItem((GUIItemAction) null));
                if (i < gui.pages() - 1) {
                    GUIContent content = actual.getContents(8).get(0);
                    content.clickItemAction().ifPresent(a -> a.execute(viewer, actual, content));
                    assertIterableEquals(gui.getPage(i + 1).getContents(1), viewer.openedGUI.getContents(1));
                }
                actual.getContents(8).forEach(e -> e.onClickItem((GUIItemAction) null));

                assertEquals(expected, actual);
            }
        }
    }

    static GUI generateExpected(GUI gui, int index) {
        GUI g = GUI.newGUI(9)
                .setContents(1, gui.getContents(1))
                .setContents(4, Item.newItem("obsidian").setDisplayName("&7Page: &e<page>"))
                .setVariable("next_page", String.valueOf(index + 2))
                .setVariable("pages", String.valueOf(3))
                .setVariable("page", String.valueOf(index + 1))
                .setVariable("previous_page", String.valueOf(index));
        if (index > 0) g.setContents(0, ItemGUIContent.newInstance("redstone_block")
                .setDisplayName("&7Go to page &e<previous_page>"));
        if (index < 2) g.setContents(8, ItemGUIContent.newInstance("emerald_block")
                .setDisplayName("&7Go to page &e<next_page>"));
        return g;
    }

    @Test
    void testOpenPageNoNextPageOrPreviousPage() {
        PageableGUI gui = PageableGUI.newGUI(9).setPages(3)
                .setContents(4, Item.newItem("obsidian").setDisplayName("&7Page: &e<page>"));
        gui.getPage(0).setContents(1, Item.newItem("red_concrete"));
        gui.getPage(1).setContents(1, Item.newItem("lime_concrete"));
        gui.getPage(2).setContents(1, Item.newItem("yellow_concrete"));

        final MockViewer viewer = new MockViewer(UUID.randomUUID(), "Steve");
        try (MockedStatic<ReflectionUtils> clazz = mockStatic(ReflectionUtils.class, CALLS_REAL_METHODS)) {
            clazz.when(() -> ReflectionUtils.getClass("it.fulminazzo.yagl.GUIAdapter")).thenReturn(MockGUIAdapter.class);

            for (int i = 0; i < gui.pages(); i++) {
                gui.open(viewer, i);
                GUI expected = generateExpected(gui.getPage(i), i).unsetContent(0).unsetContent(8);
                GUI actual = viewer.openedGUI;

                assertEquals(expected, actual);
            }
        }
    }

    @Test
    void testPageableGUIMethods() throws InvocationTargetException, IllegalAccessException {
        GUI expected = setupGUI(GUI.newGUI(18));
        GUI actual = setupGUI(PageableGUI.newGUI(18));

        for (Method method : GUI.class.getDeclaredMethods()) {
            try {
                if (method.equals(GUI.class.getDeclaredMethod("open", Viewer.class))) continue;
                if (method.getName().contains("copy")) continue;
                Metadatable.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
                continue;
            } catch (NoSuchMethodException ignored) {
            }
            method = ReflectionUtils.setAccessibleOrThrow(method);
            Object[] params = Arrays.stream(method.getParameterTypes())
                    .map(TestUtils::mockParameter)
                    .map(o -> o instanceof Integer ? 9 : o)
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
    void testRemovePages() {
        assertEquals(5, PageableGUI.newGUI(9).setPages(10).setPages(5).pages());
    }

    @Test
    void testGetGUIPageException() {
        assertThrowsExactly(IndexOutOfBoundsException.class, () -> PageableGUI.newGUI(9).getPage(1));
    }

    @Test
    void testReturnTypes() {
        TestUtils.testReturnType(PageableGUI.newGUI(45), GUI.class, m -> m.getName().equals("copy"));
    }

    @Test
    void testRemovePreviousPage() {
        PageableGUI gui = PageableGUI.newGUI(9).setPreviousPage(1, Item.newItem()).unsetPreviousPage();
        Tuple<?, ?> nextPage = new Refl<>(gui).getFieldObject("nextPage");
        assertNotNull(nextPage);
        assertTrue(nextPage.isEmpty(), String.format("Expected empty but got '%s'", nextPage));
    }

    @Test
    void testRemoveNextPage() {
        PageableGUI gui = PageableGUI.newGUI(9).setNextPage(1, Item.newItem()).unsetNextPage();
        Tuple<?, ?> nextPage = new Refl<>(gui).getFieldObject("nextPage");
        assertNotNull(nextPage);
        assertTrue(nextPage.isEmpty(), String.format("Expected empty but got '%s'", nextPage));
    }

    @Test
    void testLowerSlot() {
        assertThrowsExactly(IllegalArgumentException.class, () -> PageableGUI.newGUI(9).setNextPage(-1, Item.newItem()));
    }

    @Test
    void testHigherSlot() {
        assertThrowsExactly(IllegalArgumentException.class, () -> PageableGUI.newGUI(9).setPreviousPage(10, Item.newItem()));
    }

    @Test
    void testIteratorShouldReturnPages() {
        PageableGUI gui = PageableGUI.newGUI(9).setPages(4);
        List<GUI> pages = Arrays.asList(
                gui.getPage(0).setContents(0, Item.newItem("stone")),
                gui.getPage(1).setContents(0, Item.newItem("diamond")),
                gui.getPage(2).setContents(0, Item.newItem("emerald")),
                gui.getPage(3)
        );
        assertIterableEquals(pages, gui);
    }

    @Test
    void testCopyReturnType() {
        GUI expected = PageableGUI.newGUI(9);
        GUI actual = expected.copy();
        assertInstanceOf(PageableGUI.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testCopyAllReplace() {
        GUIContent previousPage1 = ItemGUIContent.newInstance("paper");
        GUIContent nextPage1 = ItemGUIContent.newInstance("paper");

        GUIContent previousPage2 = ItemGUIContent.newInstance("book");
        GUIContent nextPage2 = ItemGUIContent.newInstance("book");

        PageableGUI src = new PageableGUI(GUI.newGUI(9))
                .setPages(2)
                .setPreviousPage(0, previousPage1)
                .setNextPage(8, nextPage1);

        PageableGUI dst = new PageableGUI(GUI.newGUI(9))
                .setPages(3)
                .setPreviousPage(0, previousPage2)
                .setNextPage(8, nextPage2);

        src.copyAll(dst, true);

        assertEquals(2, dst.pages());
        assertEquals(previousPage1, dst.previousPage.getValue());
        assertEquals(nextPage1, dst.nextPage.getValue());
    }

    @Test
    void testCopyAllReplaceNoValues() {
        GUIContent previousPage1 = ItemGUIContent.newInstance("paper");
        GUIContent nextPage1 = ItemGUIContent.newInstance("paper");

        PageableGUI src = new PageableGUI(GUI.newGUI(9))
                .setPages(2)
                .setPreviousPage(0, previousPage1)
                .setNextPage(8, nextPage1);

        PageableGUI dst = new PageableGUI(GUI.newGUI(9));

        src.copyAll(dst, true);

        assertEquals(2, dst.pages());
        assertEquals(previousPage1, dst.previousPage.getValue());
        assertEquals(nextPage1, dst.nextPage.getValue());
    }

    @Test
    void testCopyAllNoReplace() {
        GUIContent previousPage1 = ItemGUIContent.newInstance("paper");
        GUIContent nextPage1 = ItemGUIContent.newInstance("paper");

        GUIContent previousPage2 = ItemGUIContent.newInstance("book");
        GUIContent nextPage2 = ItemGUIContent.newInstance("book");

        PageableGUI src = new PageableGUI(GUI.newGUI(9))
                .setPages(2)
                .setPreviousPage(0, previousPage1)
                .setNextPage(8, nextPage1);

        PageableGUI dst = new PageableGUI(GUI.newGUI(9))
                .setPages(3)
                .setPreviousPage(0, previousPage2)
                .setNextPage(8, nextPage2);

        src.copyAll(dst, false);

        assertEquals(3, dst.pages());
        assertEquals(previousPage2, dst.previousPage.getValue());
        assertEquals(nextPage2, dst.nextPage.getValue());
    }

    @Test
    void testCopyAllNoReplaceNoValues() {
        GUIContent previousPage1 = ItemGUIContent.newInstance("paper");
        GUIContent nextPage1 = ItemGUIContent.newInstance("paper");

        PageableGUI src = new PageableGUI(GUI.newGUI(9))
                .setPages(2)
                .setPreviousPage(0, previousPage1)
                .setNextPage(8, nextPage1);

        PageableGUI dst = new PageableGUI(GUI.newGUI(9));

        src.copyAll(dst, false);

        assertEquals(0, dst.pages());
        assertEquals(previousPage1, dst.previousPage.getValue());
        assertEquals(nextPage1, dst.nextPage.getValue());
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

    static class MockViewer extends Viewer {
        GUI openedGUI;

        protected MockViewer(UUID uniqueId, String name) {
            super(uniqueId, name);
        }

        @Override
        public void playSound(@NotNull Sound sound) {
            //
        }

        @Override
        public void sendMessage(@NotNull String message) {
            //
        }

        @Override
        public void executeCommand(@NotNull String command) {
            //
        }

        @Override
        public void consoleExecuteCommand(@NotNull String command) {
            //
        }

        @Override
        public boolean hasPermission(@NotNull String permission) {
            return false;
        }

        @Override
        public void closeGUI() {
            //
        }
    }

    static class MockGUIAdapter {

        public static void openGUI(GUI gui, MockViewer viewer) {
            viewer.openedGUI = gui;
        }
    }
}