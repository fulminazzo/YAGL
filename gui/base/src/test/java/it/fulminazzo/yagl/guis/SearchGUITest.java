package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.yagl.TestUtils;
import it.fulminazzo.yagl.contents.GUIContent;
import it.fulminazzo.yagl.contents.ItemGUIContent;
import it.fulminazzo.yagl.exceptions.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SearchGUITest {

    @Test
    void testPrepareOpenGUIDoesNotFillExisting() {
        SearchGUI<?> searchGUI = SearchGUI.newGUI(
                9,
                ItemGUIContent::newInstance,
                String::contains,
                Arrays.asList("stone", "grass_block")
        ).setQuery("search");

        for (int i = 0; i < 3; i++)
            searchGUI.setContents(i, ItemGUIContent.newInstance("barrier")
                    .setDisplayName("Barrier"));

        GUI templateGUI = new Refl<>(searchGUI).getFieldObject("templateGUI");
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
        SearchGUI<?> searchGUI = SearchGUI.newGUI(
                9,
                ItemGUIContent::newInstance,
                String::contains,
                Arrays.asList("stone", "grass_block")
        ).setQuery("search");

        GUI templateGUI = new Refl<>(searchGUI).getFieldObject("templateGUI");
        GUI preparedGUI = searchGUI.prepareOpenGUI(templateGUI, 0);

        for (int i = 0; i < 3; i++) {
            @NotNull List<GUIContent> contents = preparedGUI.getContents(i);
            assertFalse(contents.isEmpty(),
                    "Contents at slot " + i + " should not be empty");

            GUIContent content = contents.get(0);
            assertEquals(content,
                    ItemGUIContent.newInstance("glass_pane")
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

        SearchGUI<String> searchGUI = SearchGUI.newGUI(
                ItemGUIContent::newInstance,
                String::contains,
                materials
        ).setQuery("_");

        GUI gui = new Refl<>(searchGUI).getFieldObject("templateGUI");
        gui = searchGUI.prepareOpenGUI(gui, 0);

        List<String> expected = Stream.concat(
                        Stream.of("glass_pane", "glass_pane", "glass_pane"),
                        Arrays.stream(materials)
                )
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
        IllegalStateException e = assertThrows(IllegalStateException.class, () ->
                SearchGUI.newGUI(s -> null, (f, s) -> true)
                        .setPages(2));
        assertEquals(new Refl<>(DataGUI.class).getFieldObject("ERROR_MESSAGE"), e.getMessage());
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

    @ParameterizedTest
    @ValueSource(strings = {
            ":",
            "§r:",
            "query:query",
            "&colored:&colored"
    })
    void testQueryMethods(String string) {
        String[] tmp = string.split(":");
        String query = tmp.length < 1 ? "" : tmp[0];
        String expected = tmp.length < 2 ? "" : tmp[1];

        SearchGUI<?> gui = SearchGUI.newGUI(s -> null, (f, s) -> true)
                .setQuery(query);

        assertEquals(expected, gui.getQuery());
    }

    private static Constructor<?>[] serializationConstructors() {
        return Arrays.stream(SearchGUI.class.getDeclaredConstructors())
                .filter(c -> c.getParameterCount() <= 1)
                .toArray(Constructor[]::new);
    }

    @ParameterizedTest
    @ValueSource(strings = {"getPage", "setPages"})
    void testPagesRelatedMethodShouldThrowException(String method) {
        Throwable throwable = assertThrowsExactly(IllegalStateException.class, () ->
                new Refl<>(SearchGUI.newGUI(s -> null, (t, s) -> false)).invokeMethod(method, 1));
        assertEquals(new Refl<>(SearchGUI.class).getFieldObject("ERROR_MESSAGE"), throwable.getMessage());
    }

    @ParameterizedTest
    @MethodSource("serializationConstructors")
    void testConstructorsConverter(Constructor<?> constructor) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Object[] parameters = Arrays.stream(constructor.getParameterTypes())
                .map(TestUtils::mockParameter)
                .map(o -> o instanceof Number ? 9 : o)
                .toArray(Object[]::new);
        Object object = ReflectionUtils.setAccessibleOrThrow(constructor).newInstance(parameters);
        assertThrowsExactly(NotImplementedException.class, () -> new Refl<>(object)
                .getFieldRefl("searchFunction")
                .invokeMethod("test", (Object) null, null));
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