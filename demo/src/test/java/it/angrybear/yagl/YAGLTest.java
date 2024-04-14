package it.angrybear.yagl;

import it.angrybear.yagl.commands.ShellCommand;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.jbukkit.BukkitUtils;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class YAGLTest {
    private YAGL plugin;

    @BeforeEach
    void setUp() throws IOException {
        BukkitUtils.setupServer();
        this.plugin = mock(YAGL.class);
        File dataDir = new File("build/resources/test/plugin");
        if (dataDir.exists()) FileUtils.deleteFolder(dataDir);
        when(this.plugin.getDataFolder()).thenReturn(dataDir);
        new Refl<>(this.plugin).setFieldObject("commands", new LinkedList<>());
        when(this.plugin.getName()).thenReturn("YAGL-Plugin");
        doCallRealMethod().when(this.plugin).loadCommands();
        doCallRealMethod().when(this.plugin).unloadCommands();
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
    void testLoadCommandsFromAlreadyPresentDirectory() throws IOException {
        FileUtils.createFolder(new File(this.plugin.getDataFolder(), "commands"));
        this.plugin.loadCommands();
        List<ShellCommand> commands = new Refl<>(this.plugin).getFieldObject("commands");
        assertNotNull(commands);
        assertEquals(0, commands.size(), "Expected zero commands");
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

    @Test
    void testLoadAndRegisterCommands() {
        setupPluginManager();
        CommandMap commandMap = new Refl<>(Bukkit.getPluginManager()).getFieldObject("commandMap");
        assertNotNull(commandMap);
        assertNull(commandMap.getCommand("mock"), "Not expected 'mock' command but some was found");
        this.plugin.loadCommands();
        assertNotNull(commandMap.getCommand("mock"), "Expected 'mock' command but none was found");
    }

    @Test
    void testUnloadAndUnregisterCommands() {
        setupPluginManager();
        CommandMap commandMap = new Refl<>(Bukkit.getPluginManager()).getFieldObject("commandMap");
        assertNotNull(commandMap);

        ShellCommand shellCommand = new ShellCommand(new File(this.plugin.getDataFolder(), "commands/mock.groovy"));
        Map<String, Command> knownCommands = new Refl<>(commandMap).getFieldObject("knownCommands");
        assertNotNull(knownCommands);
        knownCommands.put("mock", shellCommand);

        List<ShellCommand> commands = new Refl<>(this.plugin).getFieldObject("commands");
        assertNotNull(commands);
        commands.add(shellCommand);

        assertNotNull(commandMap.getCommand("mock"), "Expected 'mock' command but none was found");
        this.plugin.unloadCommands();
        assertNull(commandMap.getCommand("mock"), "Not expected 'mock' command but some was found");
    }

    private void setupPluginManager() {
        Server server = Bukkit.getServer();
        SimpleCommandMap commandMap = new SimpleCommandMap(server);
        SimplePluginManager pluginManager = new SimplePluginManager(server, commandMap);
        when(server.getPluginManager()).thenReturn(pluginManager);
    }
}