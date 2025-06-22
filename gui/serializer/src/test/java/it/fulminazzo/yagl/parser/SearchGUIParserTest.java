package it.fulminazzo.yagl.parser;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.gui.SearchGUI;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.item.Item;
import it.fulminazzo.yagl.item.field.ItemFlag;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class SearchGUIParserTest {

    @Test
    void testSaveAndLoadSearchGUI() throws IOException {
        GUIYAGLParser.addAllParsers();
        Function<String, GUIContent> function = s -> null;
        BiPredicate<String, String> filter = (t, s) -> false;
        SearchGUI<String> expected = SearchGUI.newGUI(9, function, filter, "Hello", "world")
                .setPreviousPage(1, Item.newItem("paper")
                        .setDisplayName("&ePrevious Page")
                        .addEnchantment("unbreaking", 3)
                        .addItemFlags(ItemFlag.HIDE_UNBREAKABLE))
                .setNextPage(1, Item.newItem("paper")
                        .setDisplayName("&eNext Page")
                        .addEnchantment("unbreaking", 3)
                        .addItemFlags(ItemFlag.HIDE_UNBREAKABLE))
                .setVariable("var1", "hello")
                .setVariable("var2", "world")
                .setQuery("Hello, world");
        GUIParserTest.setupContents(expected);

        File file = new File("build/resources/test/search-gui.yml");
        if (!file.exists()) FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        final String path = expected.getClass().getSimpleName().toLowerCase();
        configuration.set(path, expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        GUI actual = configuration.get(path, GUI.class);
        assertNotNull(actual);
        assertInstanceOf(SearchGUI.class, actual);

        Refl<?> reflGUI = new Refl<>(actual);
        SearchGUI<?> searchGUI = (SearchGUI<?>) reflGUI
                .setFieldObject("dataConverter", function)
                .setFieldObject("searchFunction", filter)
                .getObject();
        @NotNull List<?> data = searchGUI.getData();
        assertTrue(data.isEmpty(), String.format("Expected empty but was %s", data));
        assertEquals("", searchGUI.getQuery());

        reflGUI.setFieldObject("data", expected.getData());
        searchGUI.setQuery(expected.getQuery());
        assertEquals(expected, actual);
    }

}