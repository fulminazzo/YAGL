package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.contents.ItemGUIContent;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SearchGUITest {

    @Test
    void testFilter() {
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

        SearchGUI<String> searchGUI = SearchGUI.newGUI(
                ItemGUIContent::newInstance,
                String::contains,
                materials
        ).setQuery("_");

        GUI gui = new Refl<>(searchGUI).getFieldObject("templateGUI");
        gui = searchGUI.prepareOpenGUI(gui, 0);

        List<String> expected = Arrays.stream(materials)
                .filter(c -> c.contains("_"))
                .collect(Collectors.toList());

        assertEquals(expected.size(), gui.getContents().size(), "GUI should show only expected contents");

        for (int i = 0; i < expected.size(); i++) {
            @NotNull List<GUIContent> contents = gui.getContents(i);
            assertFalse(contents.isEmpty(), "Contents at slot " + i + " should not be empty");

            GUIContent content = contents.get(0);
            assertInstanceOf(ItemGUIContent.class, content, "Contents at slot " + i + " should be ItemGUIContent");

            ItemGUIContent itemGUIContent = (ItemGUIContent) content;
            assertEquals(expected.get(i), itemGUIContent.getMaterial());
        }
    }

    @Test
    void testCopy() {
        SearchGUI<String> src = SearchGUI.newGUI(s -> null, (f, s) -> true);
        src.setContents(0, ItemGUIContent.newInstance("stone"));

        SearchGUI<String> dst = src.copy();

        new Refl<>(dst).setFieldObject("searchFunction", new Refl<>(src).getFieldObject("searchFunction"));
        assertEquals(src, dst);
    }

    @Test
    void testCopyAll() {
        SearchGUI<String> src = SearchGUI.newGUI(s -> null, (f, s) -> true);
        src.setContents(0, ItemGUIContent.newInstance("stone"));

        SearchGUI<String> dst = SearchGUI.newGUI(s -> null, (f, s) -> true);

        src.copyAll(dst, true);

        new Refl<>(dst).setFieldObject("searchFunction", new Refl<>(src).getFieldObject("searchFunction"));
        assertEquals(src, dst);
    }

    @Test
    void testSetPagesThrows() {
        assertThrows(IllegalStateException.class, () ->
                SearchGUI.newGUI(s -> null, (f, s) -> true)
                        .setPages(2));
    }

    @Test
    void testReturnTypes() {
        TestUtils.testReturnType(SearchGUI.newGUI(27,
                        c -> null,
                        (t, s) -> false),
                DataGUI.class,
                m -> {
                    for (String s : Arrays.asList("copy", "setPages", "getPage", "copyAll"))
                        if (s.equals(m.getName())) return true;
                    return false;
                });
    }

    private static Object[] constructorParameters() {
        return new Object[]{
                new Object[]{(Supplier<DataGUI<?>>) () -> SearchGUI.newGUI(null, (t, s) -> false), false},
                new Object[]{(Supplier<DataGUI<?>>) () -> SearchGUI.newGUI(FullSizeGUI.SECOND_INVENTORY_SIZE, null, (t, s) -> false), false},
                new Object[]{(Supplier<DataGUI<?>>) () -> SearchGUI.newGUI(null, (t, s) -> false, "Hello", "World"), true},
                new Object[]{(Supplier<DataGUI<?>>) () -> SearchGUI.newGUI(FullSizeGUI.SECOND_INVENTORY_SIZE, null, (t, s) -> false, new String[]{"Hello", "World"}), true},
                new Object[]{(Supplier<DataGUI<?>>) () -> SearchGUI.newGUI(null, (t, s) -> false, Arrays.asList("Hello", "World")), true},
                new Object[]{(Supplier<DataGUI<?>>) () -> SearchGUI.newGUI(FullSizeGUI.SECOND_INVENTORY_SIZE, null, (t, s) -> false, Arrays.asList("Hello", "World")), true}
        };
    }

    @ParameterizedTest
    @MethodSource("constructorParameters")
    void testConstructors(Supplier<DataGUI<Object>> supplier, boolean dataProvided) {
        @NotNull DataGUI<Object> expected = SearchGUI.newGUI(FullSizeGUI.SECOND_INVENTORY_SIZE, null, (t, s) -> false);
        expected.setData("Hello", "World");
        DataGUI<Object> actual = supplier.get();
        if (!dataProvided) {
            actual.setData("Hello", "World");
        }
        new Refl<>(actual).setFieldObject("searchFunction", new Refl<>(expected).getFieldObject("searchFunction"));
        assertEquals(expected, actual);
    }

    @Nested
    class SearchFullSizeGUITest {

        @Test
        void testGetQueryCallsSearchGUIGetQuery() {
            SearchGUI<?> gui = mock(SearchGUI.class);

            SearchGUI.SearchFullSizeGUI fullSizeGUI = new SearchGUI.SearchFullSizeGUI();
            fullSizeGUI.setSearchGui(gui);

            fullSizeGUI.getQuery();

            verify(gui).getQuery();
        }

        @Test
        void testSetQueryCallsSearchGUISetQuery() {
            SearchGUI<?> gui = mock(SearchGUI.class);

            SearchGUI.SearchFullSizeGUI fullSizeGUI = new SearchGUI.SearchFullSizeGUI();
            fullSizeGUI.setSearchGui(gui);

            fullSizeGUI.setQuery("any");

            verify(gui).setQuery("any");
        }

        @Test
        void testGetSearchGUIThrowsIfInitialized() {
            SearchGUI.SearchFullSizeGUI fullSizeGUI = new SearchGUI.SearchFullSizeGUI();
            assertThrows(IllegalStateException.class, fullSizeGUI::getSearchGui);
        }

        @Test
        void testCopyMethod() {
            SearchGUI.SearchFullSizeGUI src = new SearchGUI.SearchFullSizeGUI();
            SearchGUI.SearchFullSizeGUI dst = new SearchGUI.SearchFullSizeGUI();
            assertEquals(dst, src);
        }

    }

}