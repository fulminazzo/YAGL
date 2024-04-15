package it.angrybear.yagl;

import it.angrybear.yagl.commands.ShellCommand;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.JarUtils;
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
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class YAGLTest {
    private YAGL plugin;
    private Handler handler;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setUp() throws IOException {
        BukkitUtils.setupServer();

        this.output = new ByteArrayOutputStream();
        this.handler = new StreamHandler(this.output, new SimpleFormatter());
        this.handler.setLevel(Level.ALL);
        Logger logger = Logger.getAnonymousLogger();
        logger.addHandler(handler);

        this.plugin = mock(YAGL.class);
        File dataDir = new File("build/resources/test/plugin");
        if (dataDir.exists()) FileUtils.deleteFolder(dataDir);
        when(this.plugin.getDataFolder()).thenReturn(dataDir);
        new Refl<>(this.plugin).setFieldObject("commands", new LinkedList<>());
        when(this.plugin.getName()).thenReturn("YAGL-Plugin");
        doCallRealMethod().when(this.plugin).loadCommands();
        doCallRealMethod().when(this.plugin).unloadCommands();
        doCallRealMethod().when(this.plugin).saveDefaultCommands(any());
        when(this.plugin.getLogger()).thenReturn(logger);
    }

    @Test
    void testOnEnableShouldCallLoadCommands() {
        setupPluginManager();
        YAGL plugin = mock(YAGL.class);
        when(plugin.getLogger()).thenReturn(Logger.getAnonymousLogger());
        new Refl<>(plugin).setFieldObject("commands", new ArrayList<>());
        doCallRealMethod().when(plugin).onEnable();
        plugin.onEnable();
        verify(plugin, atLeastOnce()).loadCommands();
    }

    @Test
    void testOnDisableShouldCallUnloadCommands() {
        YAGL plugin = mock(YAGL.class);
        doCallRealMethod().when(plugin).onDisable();
        plugin.onDisable();
        verify(plugin, atLeastOnce()).unloadCommands();
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

    @Test
    void testInvalidCommandsDirectory() throws IOException {
        File file = new File(this.plugin.getDataFolder(), "commands");
        if (file.isFile()) FileUtils.deleteFile(file);
        if (file.isDirectory()) FileUtils.deleteFolder(file);
        FileUtils.createNewFile(file);
        assertDoesNotThrow(() -> this.plugin.loadCommands());
        file.delete();
    }

    @Test
    void testNullKnownCommands() {
        final String field = "knownCommands";

        setupPluginManager();
        new Refl<>(Bukkit.getPluginManager()).getFieldRefl("commandMap").setFieldObject(field, null);

        assertDoesNotThrow(() -> this.plugin.unloadCommands());

        String printOutput = getOutput();
        assertTrue(printOutput.contains(field), String.format("'%s' did not contain '%s'", printOutput, field));
    }

    @Test
    void testNullPluginManager() {
        assertDoesNotThrow(() -> this.plugin.loadCommands());
    }

    @Test
    void simulateJarEntries() {
        setupPluginManager();
        List<String> entries = Arrays.asList("ignored", "also-ignored", "commands", "commands/", "commands/mock.groovy");
        try (MockedStatic<JarUtils> ignored = mockStatic(JarUtils.class)) {
            when(JarUtils.getEntries((Class<?>) any(), anyString())).thenAnswer(a -> entries.iterator());

            this.plugin.loadCommands();

            List<ShellCommand> commands = new Refl<>(this.plugin).getFieldObject("commands");
            assertNotNull(commands);
            assertEquals(1, commands.size(), "Commands size did not match expected");
        }
    }

    private void setupPluginManager() {
        Server server = Bukkit.getServer();
        SimpleCommandMap commandMap = new SimpleCommandMap(server);
        SimplePluginManager pluginManager = new SimplePluginManager(server, commandMap);
        when(server.getPluginManager()).thenReturn(pluginManager);
    }

    private String getOutput() {
        this.handler.flush();
        return this.output.toString();
    }
}