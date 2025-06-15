package it.fulminazzo.yagl.parsers;

import it.fulminazzo.yagl.contents.ItemGUIContent;
import it.fulminazzo.yagl.guis.FullSizeGUI;
import it.fulminazzo.yagl.guis.GUI;
import it.fulminazzo.yagl.guis.GUIType;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

class FullSizeGUIParserTest {

    @Test
    void testSaveAndLoad() throws IOException {
        GUIYAGLParser.addAllParsers();

        FullSizeGUI expected = GUI.newFullSizeGUI(GUIType.ANVIL)
                .setContents(0, ItemGUIContent.newInstance("stone"))
                .setContents(1, ItemGUIContent.newInstance("cobblestone"))
                .setContents(3, ItemGUIContent.newInstance("grass"))
                .setContents(4, ItemGUIContent.newInstance("dirt"));

        File file = new File("build/resources/test/fullsize-gui.yml");
        if (!file.exists()) FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        final String path = expected.getClass().getSimpleName().toLowerCase();
        configuration.set(path, expected);
        configuration.save();

        configuration = new FileConfiguration(file);
        GUI actual = configuration.get(path, GUI.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testLoadNullSection() throws Exception {
        assertNull(new FullSizeGUIParser().load(mock(IConfiguration.class), "path"));
    }

    @Test
    void testSaveNull() {
        assertDoesNotThrow(() -> new FullSizeGUIParser().dump(mock(IConfiguration.class), "path", null));
    }

}