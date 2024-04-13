package it.angrybear.yagl;

import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class YAGLTest {
    private YAGL plugin;

    @BeforeEach
    void setUp() throws IOException {
        this.plugin = mock(YAGL.class);
        File dataDir = new File("build/resources/test/plugin");
        if (dataDir.exists()) FileUtils.deleteFolder(dataDir);
        when(this.plugin.getDataFolder()).thenReturn(dataDir);
        doCallRealMethod().when(this.plugin).saveDefaultCommands(any());
    }

    @Test
    void testSaveDefaultCommands() throws IOException {
        final String fileName = "commands/mock.groovy";
        File expected = new File(this.plugin.getDataFolder(), fileName);
        String expectedContent = FileUtils.readFileToString(new File("build/resources/test/" + fileName));

        this.plugin.saveDefaultCommands(expected.getParentFile());

        assertTrue(expected.exists(), String.format("Expected file '%s' to exist", expected.getAbsolutePath()));
        String actualContent = FileUtils.readFileToString(expected);

        assertEquals(expectedContent, actualContent, "Content did not match expected");
    }
}