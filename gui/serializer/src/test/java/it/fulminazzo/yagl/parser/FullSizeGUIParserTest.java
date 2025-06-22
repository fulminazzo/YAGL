package it.fulminazzo.yagl.parser;

import it.fulminazzo.yagl.gui.FullSizeGUI;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.gui.GUIType;
import it.fulminazzo.yamlparser.configuration.ConfigurationSection;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FullSizeGUIParserTest {

    @BeforeAll
    static void setAllUp() {
        GUIYAGLParser.addAllParsers();
    }

    @Test
    void testSaveAndLoad() throws IOException {
        FullSizeGUI expected = GUI.newFullSizeGUI(GUIType.BREWING, 9)
                .setTitle("Full Size GUI");
        GUIParserTest.setupContents(expected);

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
    void testLoadWithNullContents() throws Exception {
        IConfiguration configuration = mock(IConfiguration.class);
        ConfigurationSection section = new ConfigurationSection(configuration, "path");
        section.set("upper-gui-type", "DEFAULT");
        when(configuration.getConfigurationSection(any())).thenReturn(section);
        when(configuration.get("path", GUI.class)).thenReturn(GUI.newGUI(9));

        GUI gui = new FullSizeGUIParser().load(configuration, "path");

        assertInstanceOf(FullSizeGUI.class, gui);
    }

    @Test
    void testLoadWithNullContent() throws Exception {
        IConfiguration configuration = mock(IConfiguration.class);

        ConfigurationSection contentsSection = mock(ConfigurationSection.class);
        when(contentsSection.getKeys(false)).thenReturn(new LinkedHashSet<>(Arrays.asList("1", "2", "3")));
        when(contentsSection.getList(any(), any())).thenReturn(null);

        ConfigurationSection section = new ConfigurationSection(configuration, "path");
        section.set("upper-gui-type", "DEFAULT");
        section.set("contents", contentsSection);

        when(configuration.getConfigurationSection(any())).thenReturn(section);
        when(configuration.get("path", GUI.class)).thenReturn(GUI.newGUI(9));

        GUI gui = new FullSizeGUIParser().load(configuration, "path");

        assertInstanceOf(FullSizeGUI.class, gui);
    }

    @Test
    void testGUITypeNotSpecified() {
        IConfiguration configuration = mock(IConfiguration.class);
        ConfigurationSection section = new ConfigurationSection(configuration, "path");
        when(configuration.getConfigurationSection(any())).thenReturn(section);

        IllegalArgumentException ex = assertThrowsExactly(IllegalArgumentException.class, () ->
                new FullSizeGUIParser().load(configuration, "path")
        );
        assertEquals("'upper-gui-type' cannot be null", ex.getMessage());
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