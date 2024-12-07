package it.angrybear.yagl.parsers;

import it.angrybear.yagl.contents.GUIContent;
import it.angrybear.yagl.guis.DataGUI;
import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.items.Item;
import it.angrybear.yagl.items.fields.ItemFlag;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class DataGUIParserTest {

    @Test
    void testSaveAndLoadDataGUI() throws IOException {
        GUIYAGLParser.addAllParsers();
        Function<String, GUIContent> function = s -> null;
        DataGUI<String> expected = DataGUI.newGUI(9, function, "Hello", "world")
                .setPreviousPage(1, Item.newItem("paper")
                        .setDisplayName("&ePrevious Page")
                        .addEnchantment("unbreaking", 3)
                        .addItemFlags(ItemFlag.HIDE_UNBREAKABLE))
                .setNextPage(1, Item.newItem("paper")
                        .setDisplayName("&eNext Page")
                        .addEnchantment("unbreaking", 3)
                        .addItemFlags(ItemFlag.HIDE_UNBREAKABLE))
                .setVariable("var1", "hello")
                .setVariable("var2", "world");
        GUIParserTest.setupContents(expected);

        File file = new File("build/resources/test/data-gui.yml");
        if (!file.exists()) FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        final String path = expected.getClass().getSimpleName().toLowerCase();
        configuration.set(path, expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        GUI actual = configuration.get(path, GUI.class);
        assertNotNull(actual);
        assertInstanceOf(DataGUI.class, actual);

        Refl<?> reflGUI = new Refl<>(actual);
        DataGUI<?> dataGUI = (DataGUI<?>) reflGUI
                .setFieldObject("dataConverter", function)
                .getObject();
        @NotNull List<?> data = dataGUI.getData();
        assertTrue(data.isEmpty(), String.format("Expected empty but was %s", data));

        reflGUI.setFieldObject("data", expected.getData());
        assertEquals(expected, actual);
    }

}