package it.angrybear.yagl.parsers;

import it.angrybear.yagl.guis.GUI;
import it.angrybear.yagl.guis.PageableGUI;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PageableGUIParserTest {

    @Test
    void testSaveAndLoadPageableGUI() throws IOException {
        GUIYAGLParser.addAllParsers();
        GUI expected = PageableGUI.newGUI(9).setPages(3);

        File file = new File("build/resources/test/pageable-gui.yml");
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

}