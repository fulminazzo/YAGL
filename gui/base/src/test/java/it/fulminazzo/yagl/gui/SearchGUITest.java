package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.reflect.Reflect;
import it.fulminazzo.yagl.reflect.ReflectException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SearchGUITest {

    @Test
    void testPrepareOpenGUIDoesNotFillExisting() {
        SearchGUI<?> searchGUI = SearchGUI.newSearchGUI(9).setData(
                ItemGUIContent::newInstance,
                String::contains,
                Arrays.asList("stone", "grass_block")
        ).setQuery("search");

        for (int i = 0; i < 3; i++)
            searchGUI.setContents(i, ItemGUIContent.newInstance("barrier")
                    .setDisplayName("Barrier"));

        GUI templateGUI = Reflect.on(searchGUI).get("templateGUI").get();
        GUI preparedGUI = searchGUI.prepareOpenGUI(templateGUI, 0);

        for (int i = 0; i < 3; i++) {
            @NotNull List<GUIContent> contents = preparedGUI.getContents(i);
            assertFalse(contents.isEmpty(),
                    "Contents at slot " + i + " should not be empty");

            GUIContent content = contents.get(0);
            assertEquals(content,
                    ItemGUIContent.newInstance("barrier")
                            .setDisplayName(i == 0 ? "search" : "Barrier"),
                    "Content at slot " + i + " did not match expected"
            );
        }
    }

    @Test
    void testPrepareOpenGUIFillsMissing() {
        SearchGUI<?> searchGUI = SearchGUI.newSearchGUI(9).setData(
                ItemGUIContent::newInstance,
                String::contains,
                Arrays.asList("stone", "grass_block")
        ).setQuery("search");

        GUI templateGUI = Reflect.on(searchGUI).get("templateGUI").get();
        GUI preparedGUI = searchGUI.prepareOpenGUI(templateGUI, 0);

        for (int i = 0; i < 3; i++) {
            @NotNull List<GUIContent> contents = preparedGUI.getContents(i);
            assertFalse(contents.isEmpty(),
                    "Contents at slot " + i + " should not be empty");

            GUIContent content = contents.get(0);
            assertEquals(content,
                    ItemGUIContent.newInstance("barrier")
                            .setDisplayName(i == 0 ? "search" : " "),
                    "Content at slot " + i + " did not match expected"
            );
        }
    }

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

        SearchGUI<String> searchGUI = SearchGUI.newSearchGUI().setData(
                ItemGUIContent::newInstance,
                String::contains,
                materials
        ).setQuery("_");

        GUI gui = Reflect.on(searchGUI).get("templateGUI").get();
        gui = searchGUI.prepareOpenGUI(gui, 0);

        List<String> expected = Stream.concat(
                        Stream.of("barrier", "barrier", "barrier"),
                        Arrays.stream(materials)
                )
                .filter(m -> m.contains("_") || m.equals("barrier"))
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
    void testCopyAll() {
        SearchGUI<String> src = SearchGUI.newSearchGUI().<String>setData(ItemGUIContent::newInstance, String::contains, "stone", "diamond");
        src.setQuery("stone");
        src.setContents(0, ItemGUIContent.newInstance("stone"));

        SearchGUI<?> dst = SearchGUI.newSearchGUI();

        src.copyTo(dst, true);

        assertEquals(src, dst);
        assertEquals(src.getData(), dst.getData());
        assertEquals(src.getQuery(), dst.getQuery());
        assertSame(src.dataConverter, dst.dataConverter);
        assertSame(Reflect.on(src).get("searchFunction").get(), Reflect.on(dst).get("searchFunction").get());
    }

    @Test
    void testCopy() {
        SearchGUI<String> gui = SearchGUI.newSearchGUI(27)
                .setData(
                        d -> ItemGUIContent.newInstance("stone"),
                        (s, t) -> true,
                        new String[]{"stone", "diamond", "emerald", "gold", "iron"}
                ).setTitle("Search GUI Title")
                .setVariable("hello", "world")
                .setQuery("test query")
                .setContents(4, ItemGUIContent.newInstance("obsidian"));

        SearchGUI<String> copy = gui.copy();

        assertInstanceOf(SearchGUI.class, copy);
        assertEquals(gui.templateGUI, copy.templateGUI);
        assertSame(gui.dataConverter, copy.dataConverter);
        assertEquals(gui.getDataList(), copy.getDataList());
        assertEquals(Reflect.on(gui).get("data"), Reflect.on(copy).get("data"));
        assertEquals(gui.getData(), copy.getData());
        assertEquals(gui.variables(), copy.variables());
        assertEquals(Reflect.on(gui).get("searchFunction"), Reflect.on(copy).get("searchFunction"));
        assertEquals(gui.previousPage, copy.previousPage);
        assertEquals(gui.nextPage, copy.nextPage);
        assertEquals(gui.getTitle(), copy.getTitle());
        assertEquals(gui.getQuery(), copy.getQuery());
        assertEquals(gui.getContents(4), copy.getContents(4));
        assertNotSame(gui, copy);
        assertEquals(gui, copy);
        copy.clearData();
        assertFalse(gui.getData().isEmpty());
    }

    @Test
    void testSetPagesThrows() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () ->
                SearchGUI.newSearchGUI()
                        .setPages(2));
        assertEquals(Reflect.on(DataGUI.class).get("PAGES_ERROR_MESSAGE").get(), e.getMessage());
    }

    @Test
    void testReturnTypes() {
        TestUtils.testReturnType(SearchGUI.newSearchGUI(27),
                DataGUI.class,
                m -> {
                    for (String s : Arrays.asList("copy", "setPages", "getPage", "copyTo", "initializeNewInstance", "setData"))
                        if (s.equals(m.getName())) return true;
                    return false;
                });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            ":",
            "\u00a7r:",
            "query:query",
            "&colored:&colored"
    })
    void testQueryMethods(String string) {
        String[] tmp = string.split(":");
        String query = tmp.length < 1 ? "" : tmp[0];
        String expected = tmp.length < 2 ? "" : tmp[1];

        SearchGUI<?> gui = SearchGUI.newSearchGUI()
                .setQuery(query);

        assertEquals(expected, gui.getQuery());
    }

    @ParameterizedTest
    @ValueSource(strings = {"getPage", "setPages"})
    void testPagesRelatedMethodShouldThrowException(String method) {
        Exception e = assertThrowsExactly(ReflectException.class, () ->
                Reflect.on(SearchGUI.newSearchGUI()).invoke(method, 1)
        );
        Throwable cause = e.getCause();
        assertNotNull(cause);
        assertEquals(Reflect.on(DataGUI.class).get("PAGES_ERROR_MESSAGE").get(), cause.getMessage());
    }

    private static Object[] constructorParameters() {
        return new Object[]{
                new Object[]{(Supplier<SearchGUI<?>>) SearchGUI::newSearchGUI},
                new Object[]{(Supplier<SearchGUI<?>>) () -> SearchGUI.newSearchGUI(Constants.SECONDARY_GUI_SIZE)}
        };
    }

    @ParameterizedTest
    @MethodSource("constructorParameters")
    void testConstructors(Supplier<SearchGUI<?>> supplier) {
        SearchGUI<?> actual = supplier.get();
        assertEquals(SearchGUI.newSearchGUI(Constants.SECONDARY_GUI_SIZE), actual);
        assertTrue(actual.getData().isEmpty());
        assertNull(actual.dataConverter);
        assertNull(Reflect.on(actual).get("searchFunction").get());
    }

    @Test
    void testSetDataAndClearData() {
        Function<String, GUIContent> converter = ItemGUIContent::newInstance;
        BiPredicate<String, String> search = String::contains;
        SearchGUI<?> empty = SearchGUI.newSearchGUI();
        SearchGUI<String> gui = empty.setData(converter, search, "stone", "diamond");
        assertSame(empty, gui);
        assertEquals(Arrays.asList("stone", "diamond"), gui.getData());
        assertSame(converter, gui.dataConverter);
        assertSame(search, Reflect.on(gui).get("searchFunction").get());
        gui.setQuery("stone");
        assertEquals(Arrays.asList("stone"), gui.getDataList());

        SearchGUI<Integer> replaced = gui.setData(i -> ItemGUIContent.newInstance("stone"),
                (i, query) -> i.toString().contains(query), Arrays.asList(1, 2));
        assertSame(gui, replaced);
        assertEquals(Arrays.asList(1, 2), replaced.getData());
        assertSame(replaced, replaced.clearData());
        assertTrue(replaced.getData().isEmpty());
        assertNull(replaced.dataConverter);
        assertNull(Reflect.on(replaced).get("searchFunction").get());
    }

    @Test
    @SuppressWarnings("deprecation")
    void testInheritedSetDataOverloadsThrow() {
        SearchGUI<?> gui = SearchGUI.newSearchGUI();
        Function<String, GUIContent> converter = ItemGUIContent::newInstance;
        assertThrows(IllegalStateException.class, () -> gui.setData(converter, "stone"));
        assertThrows(IllegalStateException.class, () -> gui.setData(converter, Arrays.asList("stone")));
    }

    @Nested
    class SearchFullscreenGUITest {

        @Test
        void testGetSearchGUIThrowsIfInitialized() {
            SearchGUI.SearchFullscreenGUI fullscreenGUI = new SearchGUI.SearchFullscreenGUI();
            assertThrows(IllegalStateException.class, fullscreenGUI::getSearchGUI);
        }

        @Test
        void testCopyMethod() {
            SearchGUI.SearchFullscreenGUI src = new SearchGUI.SearchFullscreenGUI();
            SearchGUI.SearchFullscreenGUI dst = new SearchGUI.SearchFullscreenGUI();
            assertEquals(dst, src);
        }

    }

}
