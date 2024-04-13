package it.angrybear.yagl;

import it.angrybear.yagl.commands.ShellCommand;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
        new Refl<>(this.plugin).setFieldObject("commands", new LinkedList<>());
        doCallRealMethod().when(this.plugin).loadCommands();
        doCallRealMethod().when(this.plugin).saveDefaultCommands(any());
    }

    @Test
    void testLoadCommands() {
        this.plugin.loadCommands();
        List<ShellCommand> commands = new Refl<>(this.plugin).getFieldObject("commands");
        assertNotNull(commands);
        assertEquals(1, commands.size(), "Expected one command");
        ShellCommand command = commands.get(0);
        assertEquals("mock", command.getName());
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