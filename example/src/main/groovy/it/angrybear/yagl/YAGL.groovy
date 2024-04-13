package it.angrybear.yagl

import groovy.transform.CompileDynamic
import it.angrybear.yagl.commands.ShellCommand
import it.fulminazzo.yamlparser.utils.FileUtils
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.NotNull

@CompileDynamic
class YAGL extends JavaPlugin {
    final List<ShellCommand> commands = new ArrayList<>()

    @Override
    void onEnable() {

    }

    /**
     * Loads all the commands from the <i>{@link #getDataFolder()}/commands</i> directory.
     * If it does not exist, it is created using {@link #saveDefaultCommands(File)}.
     */
    void loadCommands() {
        this.commands.clear()
        File commandsDir = new File(getDataFolder(), "commands")
        if (!commandsDir.exists()) saveDefaultCommands(commandsDir)
        File[] files = commandsDir.listFiles()
        if (files != null)
            this.commands.addAll(files.collect { new ShellCommand(it) })
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
