package it.angrybear.yagl.guis;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}