package it.angrybear.yagl.guis;

import it.angrybear.yagl.contents.ItemGUIContent;
import it.angrybear.yagl.items.Item;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void testPagesMethod(int contents, int data, int expected) {
        DataGUI<Integer> dataGUI = DataGUI.newGUI(27, s -> ItemGUIContent.newInstance());
        for (int i = 0; i < data; i++) dataGUI.addData(i);
        for (int i = 0; i < contents; i++) dataGUI.addContent(Item.newItem("stone"));

        int pages = dataGUI.pages();
        assertEquals(expected, pages);
    }
}