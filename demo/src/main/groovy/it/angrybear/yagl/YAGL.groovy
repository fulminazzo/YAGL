package it.angrybear.yagl

import groovy.transform.CompileStatic
import it.angrybear.yagl.commands.ShellCommand
import it.angrybear.yagl.listeners.PersistentListener
import it.fulminazzo.fulmicollection.objects.Refl
import it.fulminazzo.fulmicollection.utils.JarUtils
import it.fulminazzo.yamlparser.utils.FileUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.NotNull

@CompileStatic
class YAGL extends JavaPlugin {
    private final List<ShellCommand> commands = new ArrayList<>()

    @Override
    void onEnable() {
        loadCommands()
        Bukkit.getPluginManager().registerEvents(new PersistentListener(), this)
        getLogger().info("Loaded ${commands.size()} commands")
    }

    @Override
    void onDisable() {
        unloadCommands()
    }

    /**
     * Loads all the commands from the <i>{@link #getDataFolder()}/commands</i> directory.
     * If it does not exist, it is created using {@link #saveDefaultCommands(File)}.
     */
    void loadCommands() {
        this.commands.clear()
        File commandsDir = new File(getDataFolder(), 'commands')
        if (!commandsDir.exists()) saveDefaultCommands(commandsDir)
        File[] files = commandsDir.listFiles()
        if (files != null)
            this.commands.addAll(files.findAll({ it.name.endsWith('.groovy') }).collect { new ShellCommand(it) })

        commandMap().ifPresent { map -> this.commands.each { map.register(getName(), it) } }
    }

    /**
     * Unloads all the commands loaded in {@link #commands}.
     */
    void unloadCommands() {
        commandMap().ifPresent(map -> {
            Map<String, Command> commands = new Refl<>(map).getFieldObject('knownCommands')
            if (commands == null) getLogger().warning('Could not find \'knownCommands\' field in CommandMap')
            else commands.keySet().collect().each { key ->
                Command value = commands.get(key)
                if (this.commands.contains(value)) commands.remove(key, value)
            }
        })
    }

    private static Optional<CommandMap> commandMap() {
        def pluginManager = Bukkit.getPluginManager()
        // Terrible line, but necessary for JaCoCo coverage report to 100%
        pluginManager == null ? Optional.empty() : Optional.ofNullable((CommandMap) new Refl<>(pluginManager)
                .getFieldObject('commandMap'))
    }

    /**
     * Saves all the default command scripts to the given directory
     *
     * @param commandsDir   the output directory
     */
    void saveDefaultCommands(final @NotNull File commandsDir) {
        final def resourceDir = '/commands'
        FileUtils.createFolder(commandsDir)
        getClass().getResourceAsStream(resourceDir).withReader { reader ->
            String fileName
            while ((fileName = reader.readLine()) != null) writeResourceToFile(commandsDir, fileName, resourceDir)
        }
        Iterator<String> jarEntries = JarUtils.getEntries(YAGL, '')
        while (jarEntries.hasNext()) {
            def entry = jarEntries.next()
            if (entry.startsWith(resourceDir.substring(1)) && entry.length() > resourceDir.length())
                writeResourceToFile(commandsDir, entry.substring(resourceDir.length()), resourceDir)
        }
    }

    private void writeResourceToFile(final @NotNull File dir, final @NotNull String fileName, final @NotNull resourceDir) {
        def file = new File(dir, fileName)
        if (file.exists()) FileUtils.deleteFile(file)
        FileUtils.createNewFile(file)
        def input = getClass().getResourceAsStream("${resourceDir}/${fileName}")
        def output = new FileOutputStream(file)
        output << input
    }

}
