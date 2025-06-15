package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.actions.BiGUIAction;
import it.fulminazzo.yagl.actions.GUIAction;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.contents.ItemGUIContent;
import it.fulminazzo.yagl.viewers.Viewer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FullSizeGUITest {

    @ParameterizedTest
    @EnumSource(GUIType.class)
    void testNorthCoordinates(GUIType type) {
        GUI upperGUI = GUI.newGUI(type);
        FullSizeGUI gui = GUI.newFullSizeGUI(type);

        assertEquals(upperGUI.northWest(), gui.northWest());
        assertEquals(upperGUI.north(), gui.north());
        assertEquals(upperGUI.northEast(), gui.northEast());

        assertEquals(upperGUI.topSlots(), gui.topSlots());
    }

    @ParameterizedTest
    @EnumSource(GUIType.class)
    void testLeftSlots(GUIType type) {
        GUI upperGUI = GUI.newGUI(type);
        GUI lowerGUI = GUI.newGUI(FullSizeGUI.SECOND_INVENTORY_SIZE);
        FullSizeGUI gui = GUI.newFullSizeGUI(type);

        Set<Integer> expectedSlots = upperGUI.leftSlots();
        expectedSlots.addAll(lowerGUI.leftSlots().stream()
                .map(s -> s + type.getSize())
                .collect(Collectors.toList()));

        assertEquals(expectedSlots, gui.leftSlots());
    }

    @ParameterizedTest
    @EnumSource(GUIType.class)
    void testRightSlots(GUIType type) {
        GUI upperGUI = GUI.newGUI(type);
        GUI lowerGUI = GUI.newGUI(FullSizeGUI.SECOND_INVENTORY_SIZE);
        FullSizeGUI gui = GUI.newFullSizeGUI(type);

        Set<Integer> expectedSlots = upperGUI.rightSlots();
        expectedSlots.addAll(lowerGUI.rightSlots().stream()
                .map(s -> s + type.getSize())
                .collect(Collectors.toList()));

        assertEquals(expectedSlots, gui.rightSlots());
    }

    private static Object[] middleLineGUIs() {
        List<Object> objects = new ArrayList<>(Arrays.asList(
                new Object[]{9, 18},
                new Object[]{18, 18},
                new Object[]{27, 27},
                new Object[]{36, 27},
                new Object[]{45, 36},
                new Object[]{54, 36}
        ));
        objects.addAll(Arrays.stream(GUIType.values())
                .filter(t -> t.getSize() == 9)
                .map(t -> new Object[]{t, 18})
                .collect(Collectors.toList())
        );
        objects.addAll(Arrays.stream(GUIType.values())
                .filter(t -> t.getSize() == 27 || t.getSize() == 36)
                .map(t -> new Object[]{t, 27})
                .collect(Collectors.toList())
        );
        objects.addAll(Arrays.stream(GUIType.values())
                .filter(t -> t.getSize() % 9 != 0)
                .map(t -> new Object[]{t, t.getSize() + 9})
                .collect(Collectors.toList())
        );
        return objects.toArray(new Object[0]);
    }

    @ParameterizedTest
    @MethodSource("middleLineGUIs")
    void testMiddleLine(Object initializer, int expected) {
        FullSizeGUI gui;
        if (initializer instanceof Integer)
            gui = GUI.newFullSizeGUI((Integer) initializer);
        else if (initializer instanceof GUIType)
            gui = GUI.newFullSizeGUI((GUIType) initializer);
        else throw new IllegalArgumentException(initializer.toString());

        assertEquals(expected, gui.middleLine());
    }

    @ParameterizedTest
    @EnumSource(GUIType.class)
    void testSouthCoordinates(GUIType type) {
        FullSizeGUI gui = GUI.newFullSizeGUI(type);

        int expectedSouthLine = FullSizeGUI.SECOND_INVENTORY_SIZE - 9 + type.getSize();

        assertEquals(expectedSouthLine, gui.southLine());
        assertEquals(expectedSouthLine, gui.southWest());
        assertEquals(expectedSouthLine + 4, gui.south());
        assertEquals(expectedSouthLine + 8, gui.southEast());

        List<Integer> bottomSlots = new ArrayList<>();
        for (int i = 0; i < 9; i++) bottomSlots.add(expectedSouthLine + i);

        assertIterableEquals(bottomSlots, gui.bottomSlots());
    }

    private static Object[][] guisInitializers() {
        return new Object[][]{
                new Object[]{(Supplier<GUI>) () -> GUI.newFullSizeGUI(9), new FullSizeGUI(9)},
                new Object[]{(Supplier<GUI>) () -> GUI.newFullSizeGUI(GUIType.ANVIL), new FullSizeGUI(GUIType.ANVIL)}
        };
    }

    @ParameterizedTest
    @MethodSource("guisInitializers")
    void testGUIInitFunctions(Supplier<GUI> supplier, FullSizeGUI expected) {
        GUI actual = supplier.get();
        assertEquals(expected, actual);
    }

    @Test
    void testAddContentMethod() {
        String[] materials = new String[]{
                "stone", "grass_block", "dirt", "cobblestone", "oak_planks",
                "bedrock", "sand", "gravel", "gold_ore", "iron_ore",
                "coal_ore", "oak_log", "oak_leaves", "glass", "lapis_ore",
                "lapis_block", "dispenser", "sandstone", "gold_block", "iron_block",
                "bricks", "tnt", "bookshelf", "mossy_cobblestone", "obsidian",
                "torch", "fire", "water", "lava", "diamond_ore",
                "diamond_block", "crafting_table", "furnace", "redstone_ore", "ice",
                "cactus", "jukebox", "netherrack", "soul_sand", "glowstone",
                "jack_o_lantern", "stone_bricks", "melon", "nether_bricks", "end_stone"
        };

        FullSizeGUI gui = new FullSizeGUI(9);
        gui.addContent(Arrays.stream(materials)
                .map(ItemGUIContent::newInstance)
                .toArray(ItemGUIContent[]::new));

        Refl<?> refl = new Refl<>(gui);

        GUI upperGUI = refl.getFieldObject("upperGUI");
        for (int i = 0; i < 9; i++) {
            @NotNull List<GUIContent> contents = upperGUI.getContents(i);
            assertFalse(contents.isEmpty());

            GUIContent content = contents.get(0);
            assertInstanceOf(ItemGUIContent.class, content);

            ItemGUIContent item = (ItemGUIContent) content;
            assertEquals(materials[i], item.getMaterial());
        }

        GUI lowerGUI = refl.getFieldObject("lowerGUI");
        for (int i = 9; i < materials.length; i++) {
            @NotNull List<GUIContent> contents = lowerGUI.getContents(i - 9);
            assertFalse(contents.isEmpty());

            GUIContent content = contents.get(0);
            assertInstanceOf(ItemGUIContent.class, content);

            ItemGUIContent item = (ItemGUIContent) content;
            assertEquals(materials[i], item.getMaterial());
        }
    }

    @Test
    void testThatAddContentThrowsIfOtherException() {
        Refl<FullSizeGUI> refl = new Refl<>(new FullSizeGUI(9));

        GUI upperGUI = mock(GUI.class);
        when(upperGUI.addContent(any(GUIContent.class))).thenAnswer(a -> {
            throw new IllegalArgumentException("Should not be captured");
        });

        refl.setFieldObject("upperGUI", upperGUI);

        assertThrows(IllegalArgumentException.class, () ->
                refl.getObject().addContent(ItemGUIContent.newInstance("stone"))
        );
    }

    private static Object[] slots() {
        return new Object[][]{
                new Object[]{0, 0},
                new Object[]{27, 0},
                new Object[]{26, 26},
                new Object[]{28, 1},
                new Object[]{27 + FullSizeGUI.SECOND_INVENTORY_SIZE - 1, FullSizeGUI.SECOND_INVENTORY_SIZE - 1}
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
                new Object[]{27 + FullSizeGUI.SECOND_INVENTORY_SIZE - 1, DefaultGUI.class}
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
                new Object[]{27, 0, GUI.newResizableGUI(FullSizeGUI.SECOND_INVENTORY_SIZE)},
                new Object[]{26, 26, GUI.newGUI(GUIType.CHEST)},
                new Object[]{28, 1, GUI.newResizableGUI(FullSizeGUI.SECOND_INVENTORY_SIZE)},
                new Object[]{27 + FullSizeGUI.SECOND_INVENTORY_SIZE - 1, FullSizeGUI.SECOND_INVENTORY_SIZE - 1, GUI.newResizableGUI(FullSizeGUI.SECOND_INVENTORY_SIZE)}
        };
    }

    @ParameterizedTest
    @MethodSource("slotsGUIs")
    void testIsMovableReturnsCorrectValue(int slot, int actual, GUI internalGUI) {
        FullSizeGUI gui = setupGUI(internalGUI);

        internalGUI.setMovable(actual, true);

        assertTrue(gui.isMovable(slot));
    }

    @ParameterizedTest
    @MethodSource("slotsGUIs")
    void testSetMovableSetsCorrectSlot(int slot, int actual, GUI internalGUI) {
        FullSizeGUI gui = setupGUI(internalGUI);

        assertFalse(internalGUI.isMovable(actual));

        gui.setMovable(slot, true);

        assertTrue(internalGUI.isMovable(actual));
    }

    @ParameterizedTest
    @MethodSource("slotsGUIs")
    void testGetContentsReturnsCorrectValue(int slot, int actual, GUI internalGUI) {
        FullSizeGUI gui = setupGUI(internalGUI);
        GUIContent content = ItemGUIContent.newInstance("stone");

        internalGUI.setContents(actual, content);

        @NotNull List<GUIContent> contents = gui.getContents(slot);
        assertFalse(contents.isEmpty());
        assertEquals(content, contents.get(0));
    }

    @ParameterizedTest
    @MethodSource("slotsGUIs")
    void testSetContentsSetsCorrectValue(int slot, int actual, GUI internalGUI) {
        FullSizeGUI gui = setupGUI(internalGUI);
        GUIContent content = ItemGUIContent.newInstance("stone");

        assertTrue(internalGUI.getContents(actual).isEmpty());

        gui.setContents(slot, content);

        @NotNull List<GUIContent> contents = internalGUI.getContents(actual);
        assertFalse(contents.isEmpty());
        assertEquals(content, contents.get(0));
    }

    @ParameterizedTest
    @MethodSource("slotsGUIs")
    void testUnsetContentRemovesContents(int slot, int actual, GUI internalGUI) {
        FullSizeGUI gui = setupGUI(internalGUI);
        GUIContent content = ItemGUIContent.newInstance("stone");

        internalGUI.setContents(actual, content);

        gui.unsetContent(slot);

        @NotNull List<GUIContent> contents = internalGUI.getContents(actual);
        assertTrue(contents.isEmpty());
    }

    private static FullSizeGUI setupGUI(GUI internalGUI) {
        Refl<FullSizeGUI> refl = new Refl<>(new FullSizeGUI(GUIType.CHEST));
        if (internalGUI instanceof TypeGUI) refl.setFieldObject("upperGUI", internalGUI);
        else refl.setFieldObject("lowerGUI", internalGUI);
        return refl.getObject();
    }

    @Test
    void testGetContentsReturnsCorrectValues() {
        GUIContent firstContent = ItemGUIContent.newInstance("stone");
        GUIContent secondContent = ItemGUIContent.newInstance("stone");

        FullSizeGUI gui = new FullSizeGUI(GUIType.CHEST);
        Refl<?> refl = new Refl<>(gui);

        GUI upperGUI = refl.getFieldObject("upperGUI");
        upperGUI.setContents(0, firstContent);

        GUI lowerGUI = refl.getFieldObject("lowerGUI");
        lowerGUI.setContents(1, secondContent);

        @NotNull List<GUIContent> contents = gui.getContents();

        assertIterableEquals(Arrays.asList(firstContent, secondContent), contents);
    }

    @Test
    void testRows() {
        FullSizeGUI gui = new FullSizeGUI(GUIType.CHEST);

        assertEquals(7, gui.rows());
    }

    @Test
    void testColumns() {
        FullSizeGUI gui = new FullSizeGUI(GUIType.CHEST);

        assertEquals(9, gui.columns());
    }

    @Test
    void testClearClearsBothGUIs() {
        GUI upperGUI = mock(GUI.class);
        GUI lowerGUI = mock(ResizableGUI.class);

        FullSizeGUI gui = new Refl<>(new FullSizeGUI(GUIType.CHEST))
                .setFieldObject("upperGUI", upperGUI)
                .setFieldObject("lowerGUI", lowerGUI)
                .getObject();

        gui.clear();

        verify(upperGUI).clear();
        verify(lowerGUI).clear();
    }

    private static Object[][] methods() {
        return new Object[][]{
                new Object[]{"setTitle", new Class<?>[]{String.class}},
                new Object[]{"getTitle", new Class<?>[]{}},
                new Object[]{"onClickOutside", new Class<?>[]{GUIAction.class}},
                new Object[]{"clickOutsideAction", new Class<?>[]{}},
                new Object[]{"onOpenGUI", new Class<?>[]{GUIAction.class}},
                new Object[]{"openGUIAction", new Class<?>[]{}},
                new Object[]{"onCloseGUI", new Class<?>[]{GUIAction.class}},
                new Object[]{"closeGUIAction", new Class<?>[]{}},
                new Object[]{"onChangeGUI", new Class<?>[]{BiGUIAction.class}},
                new Object[]{"changeGUIAction", new Class<?>[]{}},
                new Object[]{"variables", new Class<?>[]{}}
        };
    }

    @ParameterizedTest
    @MethodSource("methods")
    void testFullSizeGUIDelegatesToUpperGUI(String methodName, Class<?>[] parameterTypes) {
        Refl<?> fullSizeGUI = new Refl<>(new FullSizeGUI(9));

        GUI upperGUI = mock(GUI.class);
        fullSizeGUI.setFieldObject("upperGUI", upperGUI);

        Method method = fullSizeGUI.getMethod(methodName, parameterTypes);

        Object[] parameters = Arrays.stream(method.getParameterTypes())
                .map(TestUtils::mockParameter)
                .toArray(Object[]::new);

        fullSizeGUI.invokeMethod(methodName, parameters);

        new Refl<>(verify(upperGUI)).invokeMethod(methodName, parameters);
    }

    @Test
    void testOpenWithNoBukkitModule() {
        assertThrowsExactly(IllegalStateException.class, () -> new FullSizeGUI(9).open(mock(Viewer.class)));
    }

    @Test
    void testReturnTypes() {
        TestUtils.testReturnType(new FullSizeGUI(9), GUI.class, m -> m.getName().equals("copy"));
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 0, -1, FullSizeGUI.SECOND_INVENTORY_SIZE + 1})
    void testLowerGUIResizeFails(int size) {
        assertThrows(IllegalArgumentException.class, () -> new FullSizeGUI(9).getLowerGUI().resize(size));
    }

    @Test
    void testUpdateThrowsIfNoModuleProvided() {
        assertThrowsExactly(IllegalStateException.class, () -> new FullSizeGUI(9).update(null));
    }

}