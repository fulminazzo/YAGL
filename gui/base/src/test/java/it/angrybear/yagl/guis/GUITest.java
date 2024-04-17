package it.angrybear.yagl.guis;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.contents.ItemGUIContent;
import it.angrybear.yagl.items.Item;
import it.fulminazzo.fulmicollection.objects.Refl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unused")
class GUITest {

    private static Object[][] expectedCorners() {
        return new Object[][]{
                new Object[]{9, 0, 4, 8, 0, 4, 8, 0, 4, 8},
                new Object[]{18, 0, 4, 8, 0, 4, 8, 9, 13, 17},
                new Object[]{27, 0, 4, 8, 9, 13, 17, 18, 22, 26},
                new Object[]{36, 0, 4, 8, 9, 13, 17, 27, 31, 35},
                new Object[]{45, 0, 4, 8, 18, 22, 26, 36, 40, 44},
                new Object[]{54, 0, 4, 8, 18, 22, 26, 45, 49, 53},
                new Object[]{GUIType.CHEST, 0, 4, 8, 9, 13, 17, 18, 22, 26},
                new Object[]{GUIType.DISPENSER, 0, 1, 2, 3, 4, 5, 6, 7, 8},
                new Object[]{GUIType.DROPPER, 0, 1, 2, 3, 4, 5, 6, 7, 8},
                new Object[]{GUIType.FURNACE, 0, 1, 2, 0, 1, 2, 0, 1, 2},
                new Object[]{GUIType.WORKBENCH, 0, 1, 2, 3, 4, 5, 6, 7, 8},
                new Object[]{GUIType.ENCHANTING, 0, 1, 1, 0, 1, 1, 0, 1, 1},
                new Object[]{GUIType.BREWING, 0, 1, 1, 2, 3, 4, 2, 3, 4},
                new Object[]{GUIType.PLAYER, 0, 4, 8, 9, 13, 17, 27, 31, 35},
                new Object[]{GUIType.ENDER_CHEST, 0, 4, 8, 9, 13, 17, 18, 22, 26},
                new Object[]{GUIType.ANVIL, 0, 1, 2, 0, 1, 2, 0, 1, 2},
                new Object[]{GUIType.SMITHING, 0, 1, 2, 0, 1, 2, 0, 1, 2},
                new Object[]{GUIType.BEACON, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new Object[]{GUIType.HOPPER, 0, 2, 4, 0, 2, 4, 0, 2, 4},
                new Object[]{GUIType.SHULKER_BOX, 0, 4, 8, 9, 13, 17, 18, 22, 26},
                new Object[]{GUIType.BARREL, 0, 4, 8, 9, 13, 17, 18, 22, 26},
                new Object[]{GUIType.BLAST_FURNACE, 0, 1, 2, 0, 1, 2, 0, 1, 2},
                new Object[]{GUIType.LECTERN, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new Object[]{GUIType.SMOKER, 0, 1, 2, 0, 1, 2, 0, 1, 2},
                new Object[]{GUIType.LOOM, 0, 1, 1, 0, 1, 3, 2, 2, 2},
                new Object[]{GUIType.CARTOGRAPHY, 0, 1, 2, 0, 1, 2, 0, 1, 2},
                new Object[]{GUIType.GRINDSTONE, 0, 1, 2, 0, 1, 2, 0, 1, 2},
                new Object[]{GUIType.STONECUTTER, 0, 1, 1, 0, 1, 1, 0, 1, 1},
        };
    }
    
    @ParameterizedTest
    @MethodSource("expectedCorners")
    void testGUICorners(Object object, 
                        int northWest, int north, int northEast,
                        int middleWest, int middle, int middleEast,
                        int southWest, int south, int southEast) {
        GUI gui = new Refl<>(GUI.class).invokeMethod("newGUI", object);

        assertEquals(northWest, gui.northWest(), "Invalid North West slot");
        assertEquals(north, gui.north(), "Invalid North slot");
        assertEquals(northEast, gui.northEast(), "Invalid North East slot");
        
        assertEquals(middleWest, gui.middleWest(), "Invalid Middle West slot");
        assertEquals(middle, gui.middle(), "Invalid Middle slot");
        assertEquals(middleEast, gui.middleEast(), "Invalid Middle East slot");
        
        assertEquals(southWest, gui.southWest(), "Invalid South West slot");
        assertEquals(south, gui.south(), "Invalid South slot");
        assertEquals(southEast, gui.southEast(), "Invalid South East slot");
    }

    @ParameterizedTest
    @MethodSource("expectedCorners")
    void testSetLeftSide(Object object,
                        int northWest, int north, int northEast,
                        int middleWest, int middle, int middleEast,
                        int southWest, int south, int southEast) {
        final Map<Object, Integer> ignored = new HashMap<>();
        ignored.put(GUIType.WORKBENCH, 9);
        ignored.put(GUIType.BREWING, 3);
        ignored.put(GUIType.PLAYER, 36);
        ignored.put(GUIType.LOOM, 3);

        GUI gui = new Refl<>(GUI.class).invokeMethod("newGUI", object);

        ItemGUIContent itemGUIContent = ItemGUIContent.newInstance("stone");
        gui.setLeftSide(itemGUIContent);
        testSide(object, northWest, middleWest, southWest, gui, ignored, itemGUIContent);

        gui.clear();
        Item item = Item.newItem("gold");
        gui.setLeftSide(item);
        testSide(object, northWest, middleWest, southWest, gui, ignored, ItemGUIContent.newInstance(item));

        gui.clear();
        GUIContent guiContent = mock(GUIContent.class);
        when(guiContent.copy()).thenReturn(guiContent);
        gui.setLeftSide(guiContent);
        testSide(object, northWest, middleWest, southWest, gui, ignored, guiContent);

        gui.clear();
        gui.setLeftSide(Collections.singletonList(guiContent));
        testSide(object, northWest, middleWest, southWest, gui, ignored, guiContent);
    }

    private static void testSide(Object object, int north, int middle, int south, GUI gui,
                                 Map<Object, Integer> ignoredSlots, Object expected) {
        for (int i = 0; i < gui.size(); i++) {
            @NotNull List<GUIContent> contents = gui.getContents(i);
            if ((i == north || i == middle || i == south || (i - north) % gui.columns() == 0 ||
                    (i - middle) % gui.columns() == 0) && ignoredSlots.getOrDefault(object, -1) != i) {
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
        guiRefl.invokeMethod("set" + methodName, new Class[]{ItemGUIContent[].class},
                (Object) new ItemGUIContent[]{itemGUIContent});

        assertEquals(itemGUIContent, gui.getContents(slot).get(0));

        GUIContent guiContent = mock(GUIContent.class);
        guiRefl.invokeMethod("set" + methodName, new Class[]{GUIContent[].class},
                (Object) new GUIContent[]{guiContent});

        assertEquals(guiContent, gui.getContents(slot).get(0));
    }

}