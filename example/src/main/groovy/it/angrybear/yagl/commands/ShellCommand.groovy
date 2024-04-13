package it.angrybear.yagl.commands

import groovy.transform.CompileStatic
import it.angrybear.yagl.YAGL
import it.fulminazzo.fulmicollection.utils.ClassUtils
import it.fulminazzo.yamlparser.utils.FileUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.NotNull

/**
 * A general class used to create a command from a Groovy script.
 */
@CompileStatic
class ShellCommand extends Command {
    private final String shellCode

    /**
     * Instantiates a new shell command
     *
     * @param file  the file containing the script
     */
    ShellCommand(final @NotNull File file) {
        super(file.getName().substring(0, file.getName().lastIndexOf('.')))
        def imports = ClassUtils.findClassesInPackage(YAGL.package.name)
                .collect({ "import ${it.package.name}.*" })
                .reverse().unique().join("\n")
        def code = FileUtils.readFileToString(file)
        this.shellCode = "${imports}\n${code}\nrun(sender, label, args)"
    }

    @Override
    boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Binding binding = new Binding(["sender": sender, "label": label, "args": args])
        new GroovyShell(binding).evaluate(this.shellCode)
        return true
    }

    @Override
    List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return new ArrayList<>()
    }
}
