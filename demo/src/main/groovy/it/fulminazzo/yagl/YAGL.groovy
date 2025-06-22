package it.fulminazzo.yagl

import groovy.transform.CompileStatic
import it.fulminazzo.yagl.command.ShellCommand
import it.fulminazzo.yagl.listener.PersistentListener
import it.fulminazzo.fulmicollection.objects.Refl
import it.fulminazzo.fulmicollection.utils.JarUtils
import it.fulminazzo.yamlparser.utils.FileUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.NotNull

@CompileStatic
final class YAGL extends JavaPlugin {
    private final List<ShellCommand> commands = []

    /**
     * Loads a resource from the class loader and stores it in the file system.
     *
     * @param directory the output directory
     * @param fileName the name of the resource
     * @param resourceDir the name of the directory where the resource is
     * @param plugin the fallback plugin in case of a JAR context
     */
    static void writeResourceToFile(final @NotNull File directory, final @NotNull String fileName,
                                    final @NotNull String resourceDir, final @NotNull JavaPlugin plugin) {
        def file = new File(directory, fileName)
        if (file.exists()) FileUtils.deleteFile(file)
        FileUtils.createNewFile(file)
        def resourceName = "${resourceDir}/${fileName}"
        def input = YAGL.getResourceAsStream(resourceName)
        if (input == null) input = plugin.getResource(resourceName)
        def output = new FileOutputStream(file)
        output << input
    }

    @Override
    void onEnable() {
        loadCommands()
        Bukkit.pluginManager.registerEvents(new PersistentListener(), this)
        logger.info("Loaded ${commands.size()} commands")
    }

    @Override
    void onDisable() {
        unloadCommands()
    }

    /**
     * Loads all the commands from the <i>{@link #dataFolder}/commands</i> directory.
     * If it does not exist, it is created using {@link #saveDefaultCommands(File)}.
     */
    void loadCommands() {
        this.commands.clear()
        File commandsDir = new File(dataFolder, 'commands')
        if (!commandsDir.exists()) saveDefaultCommands(commandsDir)
        File[] files = commandsDir.listFiles()
        if (files != null)
            this.commands.addAll(files.findAll { it.name.endsWith('.groovy') }.collect { new ShellCommand(it) })

        commandMap().ifPresent { map -> this.commands.each { map.register(name, it) } }
    }

    /**
     * Unloads all the commands loaded in {@link #commands}.
     */
    void unloadCommands() {
        commandMap().ifPresent(map -> {
            Map<String, Command> commands = new Refl<>(map).getFieldObject('knownCommands')
            if (commands == null) logger.warning('Could not find \'knownCommands\' field in CommandMap')
            else commands.keySet().collect().each { key ->
                Command value = commands.get(key)
                if (this.commands.contains(value)) commands.remove(key, value)
            }
        })
    }

    /**
     * Saves all the default command scripts to the given directory
     *
     * @param commandsDir   the output directory
     */
    void saveDefaultCommands(final @NotNull File commandsDir) {
        final resourceDir = '/commands'
        FileUtils.createFolder(commandsDir)
        getClass().getResourceAsStream(resourceDir).withReader { reader ->
            String fileName
            while ((fileName = reader.readLine()) != null) writeResourceToFile(commandsDir, fileName, resourceDir, this)
        }
        Iterator<String> jarEntries = JarUtils.getEntries(YAGL, '')
        while (jarEntries.hasNext()) {
            def entry = jarEntries.next()
            if (entry.startsWith(resourceDir.substring(1)) && entry.length() > resourceDir.length())
                writeResourceToFile(commandsDir, entry.substring(resourceDir.length()), resourceDir, this)
        }
    }

    private static Optional<CommandMap> commandMap() {
        def pluginManager = Bukkit.pluginManager
        // Terrible line, but necessary for JaCoCo coverage report to 100%
        pluginManager == null ? Optional.empty() : Optional.ofNullable((CommandMap) new Refl<>(pluginManager)
                .getFieldObject('commandMap'))
    }

}
