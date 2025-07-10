package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.content.CustomItemGUIContent;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.fulmicollection.objects.Refl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unused")
class GUITest {

    private static Object[][] expectedCorners() {
        return new Object[][]{
                new Object[]{9, new PointMap(0, 4, 8, 0, 4, 8, 0, 4, 8), 1, 9},
                new Object[]{18, new PointMap(0, 4, 8, 0, 4, 8, 9, 13, 17), 2, 9},
                new Object[]{27, new PointMap(0, 4, 8, 9, 13, 17, 18, 22, 26), 3, 9},
                new Object[]{36, new PointMap(0, 4, 8, 9, 13, 17, 27, 31, 35), 4, 9},
                new Object[]{45, new PointMap(0, 4, 8, 18, 22, 26, 36, 40, 44), 5, 9},
                new Object[]{54, new PointMap(0, 4, 8, 18, 22, 26, 45, 49, 53), 6, 9},
                new Object[]{GUIType.CHEST, new PointMap(0, 4, 8, 9, 13, 17, 18, 22, 26), 3, 9},
                new Object[]{GUIType.DISPENSER, new PointMap(0, 1, 2, 3, 4, 5, 6, 7, 8), 3, 3},
                new Object[]{GUIType.DROPPER, new PointMap(0, 1, 2, 3, 4, 5, 6, 7, 8), 3, 3},
                new Object[]{GUIType.FURNACE, new PointMap(0, 1, 2, 0, 1, 2, 0, 1, 2), 1, 3},
                new Object[]{GUIType.WORKBENCH, new PointMap(0, 1, 2, 3, 4, 5, 6, 7, 8), 3, 3},
                new Object[]{GUIType.ENCHANTING, new PointMap(0, 1, 1, 0, 1, 1, 0, 1, 1), 1, 2},
                new Object[]{GUIType.BREWING, new PointMap(0, 1, 1, 2, 3, 4, 2, 3, 4), 2, 3},
                new Object[]{GUIType.PLAYER, new PointMap(0, 4, 8, 9, 13, 17, 27, 31, 35), 4, 9},
                new Object[]{GUIType.ENDER_CHEST, new PointMap(0, 4, 8, 9, 13, 17, 18, 22, 26), 3, 9},
                new Object[]{GUIType.ANVIL, new PointMap(0, 1, 2, 0, 1, 2, 0, 1, 2), 1, 3},
                new Object[]{GUIType.SMITHING, new PointMap(0, 1, 2, 0, 1, 2, 0, 1, 2), 1, 3},
                new Object[]{GUIType.BEACON, new PointMap(0, 0, 0, 0, 0, 0, 0, 0, 0), 1, 1},
                new Object[]{GUIType.HOPPER, new PointMap(0, 2, 4, 0, 2, 4, 0, 2, 4), 1, 5},
                new Object[]{GUIType.SHULKER_BOX, new PointMap(0, 4, 8, 9, 13, 17, 18, 22, 26), 3, 9},
                new Object[]{GUIType.BARREL, new PointMap(0, 4, 8, 9, 13, 17, 18, 22, 26), 3, 9},
                new Object[]{GUIType.BLAST_FURNACE, new PointMap(0, 1, 2, 0, 1, 2, 0, 1, 2), 1, 3},
                new Object[]{GUIType.LECTERN, new PointMap(0, 0, 0, 0, 0, 0, 0, 0, 0), 1, 1},
                new Object[]{GUIType.SMOKER, new PointMap(0, 1, 2, 0, 1, 2, 0, 1, 2), 1, 3},
                new Object[]{GUIType.LOOM, new PointMap(0, 1, 1, 0, 1, 3, 2, 2, 2), 2, 3},
                new Object[]{GUIType.CARTOGRAPHY, new PointMap(0, 1, 2, 0, 1, 2, 0, 1, 2), 1, 3},
                new Object[]{GUIType.GRINDSTONE, new PointMap(0, 1, 2, 0, 1, 2, 0, 1, 2), 1, 3},
                new Object[]{GUIType.STONECUTTER, new PointMap(0, 1, 1, 0, 1, 1, 0, 1, 1), 1, 2},
        };
    }
    
    @ParameterizedTest
    @MethodSource("expectedCorners")
    void testGUICorners(Object object, PointMap map, int rows, int columns) {
        GUI gui = new Refl<>(GUI.class).invokeMethod("newGUI", object);

        assertEquals(map.northWest, gui.northWest(), "Invalid North West slot");
        assertEquals(map.north, gui.north(), "Invalid North slot");
        assertEquals(map.northEast, gui.northEast(), "Invalid North East slot");
        
        assertEquals(map.middleWest, gui.middleWest(), "Invalid Middle West slot");
        assertEquals(map.middle, gui.middle(), "Invalid Middle slot");
        assertEquals(map.middleEast, gui.middleEast(), "Invalid Middle East slot");
        
        assertEquals(map.southWest, gui.southWest(), "Invalid South West slot");
        assertEquals(map.south, gui.south(), "Invalid South slot");
        assertEquals(map.southEast, gui.southEast(), "Invalid South East slot");

        assertEquals(rows, gui.rows());
        assertEquals(columns, gui.columns());
    }

    @ParameterizedTest
    @MethodSource("expectedCorners")
    void testSetTopSide(Object object, PointMap map, int rows, int columns) {
        final Map<Object, Integer> ignored = new HashMap<>();

        GUI gui = new Refl<>(GUI.class).invokeMethod("newGUI", object);

        ItemGUIContent itemGUIContent = ItemGUIContent.newInstance("stone");
        gui.setTopSide(itemGUIContent);
        testSide(object, map.northWest, map.north, map.northEast, i -> i <= map.northEast,
                gui, ignored, itemGUIContent);

        gui.clear();
        Item item = Item.newItem("gold");
        gui.setTopSide(item);
        testSide(object, map.northWest, map.north, map.northEast, i -> i <= map.northEast,
                gui, ignored, ItemGUIContent.newInstance(item));

        gui.clear();
        GUIContent guiContent = mock(GUIContent.class);
        when(guiContent.copy()).thenReturn(guiContent);
        gui.setTopSide(guiContent);
        testSide(object, map.northWest, map.north, map.northEast, i -> i <= map.northEast,
                gui, ignored, guiContent);

        gui.clear();
        gui.setTopSide(Collections.singletonList(guiContent));
        testSide(object, map.northWest, map.north, map.northEast, i -> i <= map.northEast,
                gui, ignored, guiContent);
    }

    @ParameterizedTest
    @MethodSource("expectedCorners")
    void testSetLeftSide(Object object, PointMap map, int rows, int columns) {
        final Map<Object, Integer> ignored = new HashMap<>();
        ignored.put(GUIType.WORKBENCH, 9);
        ignored.put(GUIType.BREWING, 3);
        ignored.put(GUIType.PLAYER, 36);
        ignored.put(GUIType.LOOM, 3);

        GUI gui = new Refl<>(GUI.class).invokeMethod("newGUI", object);

        ItemGUIContent itemGUIContent = ItemGUIContent.newInstance("stone");
        gui.setLeftSide(itemGUIContent);
        testSide(object, map.northWest, map.middleWest, map.southWest, gui, ignored, itemGUIContent);

        gui.clear();
        Item item = Item.newItem("gold");
        gui.setLeftSide(item);
        testSide(object, map.northWest, map.middleWest, map.southWest, gui, ignored, ItemGUIContent.newInstance(item));

        gui.clear();
        GUIContent guiContent = mock(GUIContent.class);
        when(guiContent.copy()).thenReturn(guiContent);
        gui.setLeftSide(guiContent);
        testSide(object, map.northWest, map.middleWest, map.southWest, gui, ignored, guiContent);

        gui.clear();
        gui.setLeftSide(Collections.singletonList(guiContent));
        testSide(object, map.northWest, map.middleWest, map.southWest, gui, ignored, guiContent);
    }

    @ParameterizedTest
    @MethodSource("expectedCorners")
    void testSetBottomSide(Object object, PointMap map, int rows, int columns) {
        final Map<Object, Integer> ignored = new HashMap<>();
        ignored.put(GUIType.WORKBENCH, 9);
        ignored.put(GUIType.PLAYER, 36);
        ignored.put(GUIType.LOOM, 3);

        GUI gui = new Refl<>(GUI.class).invokeMethod("newGUI", object);

        ItemGUIContent itemGUIContent = ItemGUIContent.newInstance("stone");
        gui.setBottomSide(itemGUIContent);
        testSide(object, map.southWest, map.south, map.southEast, i -> i >= map.southWest && (object != GUIType.PLAYER || i < 37),
                gui, ignored, itemGUIContent);

        gui.clear();
        Item item = Item.newItem("gold");
        gui.setBottomSide(item);
        testSide(object, map.southWest, map.south, map.southEast, i -> i >= map.southWest && (object != GUIType.PLAYER || i < 37),
                gui, ignored, ItemGUIContent.newInstance(item));

        gui.clear();
        GUIContent guiContent = mock(GUIContent.class);
        when(guiContent.copy()).thenReturn(guiContent);
        gui.setBottomSide(guiContent);
        testSide(object, map.southWest, map.south, map.southEast, i -> i >= map.southWest && (object != GUIType.PLAYER || i < 37),
                gui, ignored, guiContent);

        gui.clear();
        gui.setBottomSide(Collections.singletonList(guiContent));
        testSide(object, map.southWest, map.south, map.southEast, i -> i >= map.southWest && (object != GUIType.PLAYER || i < 37),
                gui, ignored, guiContent);
    }

    @ParameterizedTest
    @MethodSource("expectedCorners")
    void testSetRightSide(Object object, PointMap map, int rows, int columns) {
        final Map<Object, Integer> ignored = new HashMap<>();
        ignored.put(GUIType.LOOM, 0);

        GUI gui = new Refl<>(GUI.class).invokeMethod("newGUI", object);

        ItemGUIContent itemGUIContent = ItemGUIContent.newInstance("stone");
        gui.setRightSide(itemGUIContent);
        testSide(object, map.northEast, map.middleEast, map.southEast, gui, ignored, itemGUIContent);

        gui.clear();
        Item item = Item.newItem("gold");
        gui.setRightSide(item);
        testSide(object, map.northEast, map.middleEast, map.southEast, gui, ignored, ItemGUIContent.newInstance(item));

        gui.clear();
        GUIContent guiContent = mock(GUIContent.class);
        when(guiContent.copy()).thenReturn(guiContent);
        gui.setRightSide(guiContent);
        testSide(object, map.northEast, map.middleEast, map.southEast, gui, ignored, guiContent);

        gui.clear();
        gui.setRightSide(Collections.singletonList(guiContent));
        testSide(object, map.northEast, map.middleEast, map.southEast, gui, ignored, guiContent);
    }

    private static void testSide(Object object, int north, int middle, int south, GUI gui,
                                 Map<Object, Integer> ignoredSlots, Object expected) {
        testSide(object, north, middle, south,  i -> (i - north) % gui.columns() == 0 ||
                (i - middle) % gui.columns() == 0, gui, ignoredSlots, expected);
    }

    private static void testSide(Object object, int north, int middle, int south,
                                 Predicate<Integer> validateSlot, GUI gui,
                                 Map<Object, Integer> ignoredSlots, Object expected) {
        for (int i = 0; i < gui.size(); i++) {
            @NotNull List<GUIContent> contents = gui.getContents(i);
            if ((i == north || i == middle || i == south || validateSlot.test(i)) && ignoredSlots.getOrDefault(object, -1) != i) {
                assertFalse(contents.isEmpty(), String.format("Expected not empty at %s", i));
                assertEquals(expected, contents.get(0));
            } else assertTrue(contents.isEmpty(), String.format("Expected empty at %s but was: %s", i, contents));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "northWest", "north", "northEast",
            "middleWest", "middle", "middleEast",
            "southWest", "south", "southEast",
    })
    void testSetCornersMethods(String methodName) {
        GUI gui = GUI.newGUI(27);
        Refl<GUI> guiRefl = new Refl<>(gui);
        int slot = guiRefl.invokeMethod(methodName);

        Item item = Item.newItem("stone");
        guiRefl.invokeMethod("set" + methodName, new Class[]{Item[].class},
                (Object) new Item[]{item});

        assertEquals(ItemGUIContent.newInstance(item), gui.getContents(slot).get(0));

        ItemGUIContent itemGUIContent = ItemGUIContent.newInstance("diamond");
        guiRefl.invokeMethod("set" + methodName, new Class[]{CustomItemGUIContent[].class},
                (Object) new ItemGUIContent[]{itemGUIContent});

        assertEquals(itemGUIContent, gui.getContents(slot).get(0));

        GUIContent guiContent = mock(GUIContent.class);
        guiRefl.invokeMethod("set" + methodName, new Class[]{GUIContent[].class},
                (Object) new GUIContent[]{guiContent});

        assertEquals(guiContent, gui.getContents(slot).get(0));
    }

    @Test
    void testEmptySlots() {
        GUI gui = GUI.newGUI(54);
        gui.setContents(1, Item.newItem("Stone"));

        Set<Integer> slots = gui.emptySlots();
        for (int i = 0; i < gui.size(); i++)
            if (i == 1) assertFalse(slots.contains(i), String.format("Slots should not contain '%s'", i));
            else assertTrue(slots.contains(i), String.format("Slots should contain '%s'", i));
    }

    private static class PointMap {
        public final int northWest;
        public final int north;
        public final int northEast;
        public final int middleWest;
        public final int middle;
        public final int middleEast;
        public final int southWest;
        public final int south;
        public final int southEast;

        public PointMap(int northWest, int north, int northEast, int middleWest, int middle, int middleEast, int southWest, int south, int southEast) {
            this.northWest = northWest;
            this.north = north;
            this.northEast = northEast;
            this.middleWest = middleWest;
            this.middle = middle;
            this.middleEast = middleEast;
            this.southWest = southWest;
            this.south = south;
            this.southEast = southEast;
        }

    }

}
