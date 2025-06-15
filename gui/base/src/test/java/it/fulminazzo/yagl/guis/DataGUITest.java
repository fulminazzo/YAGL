package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.actions.GUIItemAction;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.contents.ItemGUIContent;
import it.fulminazzo.yagl.exceptions.NotImplemented;
import it.fulminazzo.yagl.items.Item;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

class DataGUITest {

    private static Object[][] pagesTest() {
        return new Object[][]{
                new Object[]{0, 27, 1, false, false},
                new Object[]{0, 54, 2, false, false},
                new Object[]{9, 54, 3, false, false},
                new Object[]{26, 54, 54, false, false},
                new Object[]{14, 198, 16, false, false},
                new Object[]{0, 27, 2, false, true},
                new Object[]{0, 27, 1, true, false},
                new Object[]{0, 54, 3, true, true},
        };
    }

    @ParameterizedTest
    @MethodSource("pagesTest")
    void testPagesMethod(int contents, int data, int expected, boolean prev, boolean next) {
        DataGUI<Integer> dataGUI = setupGUI(contents, data, prev, next, ItemGUIContent.newInstance());
        int pages = dataGUI.pages();
        assertEquals(expected, pages);
    }

    @ParameterizedTest
    @MethodSource("pagesTest")
    void testFillContentsMethod(int contents, int data, int pages, boolean prev, boolean next) {
        GUIContent convertedContent = ItemGUIContent.newInstance("grass");
        DataGUI<Integer> dataGUI = setupGUI(contents, data, prev, next, convertedContent);
        Refl<?> guiRefl = new Refl<>(dataGUI);

        for (int p = 0; p < pages; p++) {
            GUI gui = guiRefl.getFieldObject("templateGUI");
            assertNotNull(gui);
            gui = guiRefl.invokeMethod("fillContents", gui.copy(), p);
            assertNotNull(gui);

            for (int c = 0; c < dataGUI.size(); c++) {
                if (gui.emptySlots().contains(c)) continue;
                @NotNull List<GUIContent> cs = gui.getContents(c);
                assertFalse(cs.isEmpty(), String.format("Expected not empty at slot %s for page %s", c, p));
                GUIContent content = cs.get(0);
                String message = String.format("Invalid material at slot %s for page %s", c, p);
                if (c < contents) assertNotEquals(convertedContent, content, message);
                else assertEquals(convertedContent, content, message);
            }
        }
    }

    @Test
    void testThatDataGUIWithDifferentDataConverterAreEqual() {
        DataGUI<String> first = DataGUI.newGUI(27, ItemGUIContent::newInstance);
        DataGUI<String> second = DataGUI.newGUI(27, ItemGUIContent::newInstance);
        assertEquals(first, second);
    }

    private static @NotNull DataGUI<Integer> setupGUI(int contents, int data, boolean prev, boolean next, GUIContent convertedContent) {
        DataGUI<Integer> dataGUI = DataGUI.newGUI(27, s -> convertedContent);
        if (prev) dataGUI.setPreviousPage(0, Item.newItem());
        if (next) dataGUI.setNextPage(2, Item.newItem());
        for (int i = 0; i < data; i++) dataGUI.addData(i);
        @NotNull Item stone = Item.newItem("stone");
        for (int i = 0; i < contents; i++) dataGUI.addContent(stone);
        return dataGUI;
    }

    private static Object[] removeDataParameters() {
        return new Consumer[]{
                (Consumer<DataGUI<String>>) g -> g.removeData("hello", "world"),
                (Consumer<DataGUI<String>>) g -> g.removeData(Arrays.asList("hello", "world")),
                (Consumer<DataGUI<String>>) g -> g.removeData(s -> s.equals("hello") || s.equals("world"))
        };
    }

    @ParameterizedTest
    @MethodSource("removeDataParameters")
    void testRemoveData(Consumer<DataGUI<String>> consumer) {
        DataGUI<String> gui = DataGUI.newGUI(9, s -> null, "hello", "world");
        consumer.accept(gui);
        @NotNull List<String> data = gui.getData();
        assertTrue(data.isEmpty(), String.format("Expected empty but was '%s'", data));
    }

    @Test
    void testNoData() {
        try (MockedStatic<ReflectionUtils> clazz = mockStatic(ReflectionUtils.class, CALLS_REAL_METHODS)) {
            clazz.when(() -> ReflectionUtils.getClass("it.fulminazzo.yagl.GUIAdapter"))
                    .thenReturn(PageableGUITest.MockGUIAdapter.class);

            DataGUI<?> gui = DataGUI.newGUI(9, s -> null);
            int page = 3;
            assertDoesNotThrow(() -> gui.open(new PageableGUITest.MockViewer(UUID.randomUUID(), "Mock"), page));
        }
    }

    @Test
    void testInvalidPage() {
        DataGUI<?> gui = DataGUI.newGUI(9, s -> null).setData(new String[]{"Hello"});
        int page = 3;
        Throwable throwable = assertThrowsExactly(IllegalArgumentException.class, () ->
                gui.open(null, page));
        String message = throwable.getMessage();
        assertTrue(message.contains(String.valueOf(page)),
                String.format("Message should contain '%s' but was '%s'", page, message));
    }

    @Test
    void testNoEmptySlots() {
        int size = 9;
        Item[] contents = new Item[size];
        Arrays.fill(contents, Item.newItem("stone"));
        DataGUI<?> gui = DataGUI.newGUI(size, s -> null, "Hello").addContent(contents);
        assertThrowsExactly(IllegalStateException.class, gui::pages);
    }

    @ParameterizedTest
    @ValueSource(strings = {"getPage", "setPages"})
    void testPagesRelatedMethodShouldThrowException(String method) {
        Throwable throwable = assertThrowsExactly(IllegalStateException.class, () ->
                new Refl<>(DataGUI.newGUI(GUIType.ANVIL, s -> null)).invokeMethod(method, 1));
        assertEquals(new Refl<>(DataGUI.class).getFieldObject("ERROR_MESSAGE"), throwable.getMessage());
    }

    private static Constructor<?>[] privateConstructors() {
        return Arrays.stream(DataGUI.class.getDeclaredConstructors())
                .filter(c -> Modifier.isPrivate(c.getModifiers()))
                .toArray(Constructor[]::new);
    }

    @ParameterizedTest
    @MethodSource("privateConstructors")
    void testPrivateConstructorsConverter(Constructor<?> constructor) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Object[] parameters = Arrays.stream(constructor.getParameterTypes())
                .map(TestUtils::mockParameter)
                .map(o -> o instanceof Number ? 9 : o)
                .toArray(Object[]::new);
        Object object = ReflectionUtils.setAccessibleOrThrow(constructor).newInstance(parameters);
        assertThrowsExactly(NotImplemented.class, () -> new Refl<>(object)
                .getFieldRefl("dataConverter")
                .invokeMethod("apply", (Object) null));
    }

    @Test
    void testOpenPage() {
        int[][] slots = new int[][]{
                new int[]{0, 1, 2, 3, 5, 6, 7},
                new int[]{1, 2, 3, 5, 6, 7},
                new int[]{1, 2, 3, 5, 6, 7, 8}
        };

        Double[] data = new Double[]{
                0.0, 0.1, 0.2, 0.3, 0.5, 0.6, 0.7,
                1.1, 1.2, 1.3, 1.5, 1.6, 1.7,
                2.1, 2.2, 2.3, 2.5, 2.6, 2.7, 2.8,
        };
        Function<Double, GUIContent> cc = d -> ItemGUIContent.newInstance()
                .setDisplayName("Data: " + d)
                .setAmount((int) (d * 10));
        PageableGUI gui = DataGUI.newGUI(9, cc, data)
                .setPreviousPage(0, Item.newItem("redstone_block")
                        .setDisplayName("&7Go to page &e<previous_page>"))
                .setNextPage(8, Item.newItem("emerald_block")
                        .setDisplayName("&7Go to page &e<next_page>"))
                .setContents(4, Item.newItem("obsidian").setDisplayName("&7Page: &e<page>"));

        final PageableGUITest.MockViewer viewer = new PageableGUITest.MockViewer(UUID.randomUUID(), "Steve");
        try (MockedStatic<ReflectionUtils> clazz = mockStatic(ReflectionUtils.class, CALLS_REAL_METHODS)) {
            clazz.when(() -> ReflectionUtils.getClass("it.fulminazzo.yagl.GUIAdapter"))
                    .thenReturn(PageableGUITest.MockGUIAdapter.class);

            for (int i = 0; i < gui.pages(); i++) {
                gui.open(viewer, i);
                GUI expected = new Refl<>(gui).getFieldObject("templateGUI");
                assertNotNull(expected);
                expected = PageableGUITest.generateExpected(expected.copy(), i);

                int[] tmpSlots = slots[i];
                for (int s = 0; s < tmpSlots.length; s++) {
                    int ind = s;
                    for (int k = i; k > 0; k--) ind += slots[k - 1].length;
                    double d = data[ind];
                    expected.setContents(tmpSlots[s], cc.apply(d));
                }

                GUI actual = viewer.openedGUI;
                actual.getContents(0).forEach(e -> e.onClickItem((GUIItemAction) null));
                actual.getContents(8).forEach(e -> e.onClickItem((GUIItemAction) null));

                assertEquals(expected, actual, String.format("Invalid page %s", i));
            }
        }
    }

    @Test
    void testReturnTypes() {
        TestUtils.testReturnType(DataGUI.newGUI(45, c -> null), PageableGUI.class, m -> {
            for (String s : Arrays.asList("copy", "setPages", "getPage"))
                if (s.equals(m.getName())) return true;
            return false;
        });
    }

    @Test
    void testCopy() {
        DataGUI<?> expected = DataGUI.newGUI(9, c -> null, "Hello", "world");
        DataGUI<?> actual = expected.copy();
        assertEquals(expected, actual);
    }

    private static Object[] constructorParameters() {
        return new Object[]{
                new Object[]{(Supplier<DataGUI<?>>) () -> DataGUI.newGUI(27, null), false, false},
                new Object[]{(Supplier<DataGUI<?>>) () -> DataGUI.newGUI(27, null, "Hello", "World"), false, true},
                new Object[]{(Supplier<DataGUI<?>>) () -> DataGUI.newGUI(27, null, Arrays.asList("Hello", "World")), false, true},
                new Object[]{(Supplier<DataGUI<?>>) () -> DataGUI.newGUI(GUIType.CHEST, null), true, false},
                new Object[]{(Supplier<DataGUI<?>>) () -> DataGUI.newGUI(GUIType.CHEST, null, "Hello", "World"), true, true},
                new Object[]{(Supplier<DataGUI<?>>) () -> DataGUI.newGUI(GUIType.CHEST, null, Arrays.asList("Hello", "World")), true, true},
        };
    }

    @ParameterizedTest
    @MethodSource("constructorParameters")
    void testConstructors(Supplier<DataGUI<Object>> supplier, boolean typeProvided, boolean dataProvided) {
        @NotNull DataGUI<Object> expected = typeProvided ?
                new DataGUI<>(GUI.newGUI(GUIType.CHEST), null) :
                new DataGUI<>(GUI.newGUI(27), null);
        expected.setData("Hello", "World");
        DataGUI<Object> actual = supplier.get();
        if (!dataProvided) {
            actual.setData("Hello", "World");
        }
        assertEquals(expected, actual);
    }

    private static Object[] fullSizeConstructorParameters() {
        return new Object[]{
                new Object[]{(Supplier<DataGUI<?>>) () -> DataGUI.newFullSizeGUI(27, null), false, false},
                new Object[]{(Supplier<DataGUI<?>>) () -> DataGUI.newFullSizeGUI(27, null, "Hello", "World"), false, true},
                new Object[]{(Supplier<DataGUI<?>>) () -> DataGUI.newFullSizeGUI(27, null, Arrays.asList("Hello", "World")), false, true},
                new Object[]{(Supplier<DataGUI<?>>) () -> DataGUI.newFullSizeGUI(GUIType.CHEST, null), true, false},
                new Object[]{(Supplier<DataGUI<?>>) () -> DataGUI.newFullSizeGUI(GUIType.CHEST, null, "Hello", "World"), true, true},
                new Object[]{(Supplier<DataGUI<?>>) () -> DataGUI.newFullSizeGUI(GUIType.CHEST, null, Arrays.asList("Hello", "World")), true, true},
        };
    }

    @ParameterizedTest
    @MethodSource("fullSizeConstructorParameters")
    void testFullSizeConstructors(Supplier<DataGUI<Object>> supplier, boolean typeProvided, boolean dataProvided) {
        @NotNull DataGUI<Object> expected = typeProvided ?
                new DataGUI<>(GUI.newFullSizeGUI(GUIType.CHEST), null) :
                new DataGUI<>(GUI.newFullSizeGUI(27), null);
        expected.setData("Hello", "World");
        DataGUI<Object> actual = supplier.get();
        if (!dataProvided) {
            actual.setData("Hello", "World");
        }
        assertEquals(expected, actual);
    }
}