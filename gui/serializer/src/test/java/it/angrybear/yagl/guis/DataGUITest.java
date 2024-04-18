package it.angrybear.yagl.guis;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.contents.ItemGUIContent;
import it.angrybear.yagl.items.Item;
import it.fulminazzo.fulmicollection.objects.Refl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataGUITest {

    private static Object[][] pagesTest() {
        return new Object[][]{
                new Object[]{0, 27, 1},
                new Object[]{0, 54, 2},
                new Object[]{9, 54, 3},
                new Object[]{26, 54, 54},
                new Object[]{14, 198, 16},
        };
    }

    @ParameterizedTest
    @MethodSource("pagesTest")
    void testFillContentsMethod(int contents, int data, int pages) {
        GUIContent convertedContent = ItemGUIContent.newInstance("grass");
        DataGUI<Integer> dataGUI = DataGUI.newGUI(27, s -> convertedContent);
        Refl<?> guiRefl = new Refl<>(dataGUI);
        for (int i = 0; i < data; i++) dataGUI.addData(i);
        @NotNull Item stone = Item.newItem("stone");
        for (int i = 0; i < contents; i++) dataGUI.addContent(stone);

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

    @ParameterizedTest
    @MethodSource("pagesTest")
    void testPagesMethod(int contents, int data, int expected) {
        DataGUI<Integer> dataGUI = DataGUI.newGUI(27, s -> ItemGUIContent.newInstance());
        for (int i = 0; i < data; i++) dataGUI.addData(i);
        for (int i = 0; i < contents; i++) dataGUI.addContent(Item.newItem("stone"));

        int pages = dataGUI.pages();
        assertEquals(expected, pages);
    }
}