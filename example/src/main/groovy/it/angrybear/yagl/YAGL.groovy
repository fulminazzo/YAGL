package it.angrybear.yagl

import groovy.transform.CompileDynamic
import it.angrybear.yagl.commands.ShellCommand
import it.fulminazzo.fulmicollection.objects.Refl
import it.fulminazzo.yamlparser.utils.FileUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.NotNull

@CompileDynamic
class YAGL extends JavaPlugin {
    private final List<ShellCommand> commands = new ArrayList<>()

    @Override
    void onEnable() {
        loadCommands()
        getLogger().info("Loaded ${commands.size()} commands")
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
            this.commands.addAll(files.collect { new ShellCommand(it) })

        commandMap().ifPresent { map -> this.commands.each { map.register(getName(), it) } }
    }

    /**
     * Unloads all the commands loaded in {@link #commands}.
     */
    void unloadCommands() {
        commandMap().ifPresent { map ->
            Map<String, Command> commands = new Refl<>(map).getFieldObject('knownCommands')
            if (commands == null) getLogger().warning('Could not find \'knownCommands\' field in CommandMap')
            else commands.keySet().clone().each { key ->
                Command value = commands.get(key)
                if (this.commands.contains(value)) commands.remove(key, value)
            }
        }
    }

    private static Optional<CommandMap> commandMap() {
        def pluginManager = Bukkit.getPluginManager()
        if (pluginManager == null) Optional.empty()
        else {
            def refl = new Refl<>(pluginManager)
            Optional.ofNullable(refl.getFieldObject('commandMap'))
        }
    }

    /**
     * Saves all the default command scripts to the given directory
     *
     * @param commandsDir   the output directory
     */
    void saveDefaultCommands(final @NotNull File commandsDir) {
        final def containingFolder = '/commands'
        FileUtils.createFolder(commandsDir)
        getClass().getResourceAsStream(containingFolder).withReader {
            def fileName = it.readLine()
            def file = new File(commandsDir, fileName)
            FileUtils.createNewFile(file)
            def input = getClass().getResourceAsStream("${containingFolder}/${fileName}")
            def output = new FileOutputStream(file)
            output << input
        }
    }

}
