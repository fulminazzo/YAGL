package it.angrybear.yagl.guis;

import it.angrybear.yagl.actions.GUIItemAction;
import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.contents.ItemGUIContent;
import it.angrybear.yagl.items.Item;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

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

    @Test
    void testOpenPage() {
        int[][] slots = new int[][]{
                new int[]{0, 1, 2, 3, 5, 6, 7},
                new int[]{1, 2, 3, 5, 6, 7},
                new int[]{1, 2, 3, 5, 6, 7, 8}
        };

        Double[] data = new Double[] {
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
            clazz.when(() -> ReflectionUtils.getClass("it.angrybear.yagl.GUIAdapter"))
                    .thenReturn(PageableGUITest.MockGUIAdapter.class);

            for (int i = 0; i < gui.pages(); i++) {
                gui.open(viewer, i);
                GUI expected = new Refl<>(gui).getFieldObject("templateGUI");
                assertNotNull(expected);
                expected = PageableGUITest.generateExpected(expected.copy(), i);

                int[] tmpSlots = slots[i];
                for (int s = 0; s < tmpSlots.length; s++) {
                    int ind = s;
                    if (i > 0) ind += slots[i - 1].length;
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
}