package it.angrybear.yagl.commands

import groovy.transform.CompileStatic
import it.fulminazzo.yamlparser.utils.FileUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.NotNull

import java.util.regex.Pattern

/**
 * A general class used to create a command from a Groovy script.
 */
@CompileStatic
class ShellCommand extends Command {
    private static final String NUMBER_FORMAT_REGEX = '(catch *\\(NumberFormatException +ignored\\) *\\{\\n)[ \\t]*(\\n *})'
    private static final String INVALID_NUMBER_CODE = 'sender.sendMessage(e.getMessage().replace(\'For input string: \', \'Invalid number \'))'
    private final String shellCode

    /**
     * Instantiates a new shell command
     *
     * @param file  the file containing the script
     */
    ShellCommand(final @NotNull File file) {
        super(file.getName().substring(0, file.getName().lastIndexOf('.')))
        def code = FileUtils.readFileToString(file)
        if (code == null) {
            this.shellCode = ''
            return
        }
        def matcher = Pattern.compile(NUMBER_FORMAT_REGEX).matcher(code)
        while (matcher.find()) {
            def replacement = "${matcher.group(1)}    ${INVALID_NUMBER_CODE}${matcher.group(2)}"
            code = code.replace(matcher.group(), replacement)
        }
        this.shellCode = "${code}\nrun(sender, label, args)"
    }

    @Override
    boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Binding binding = new Binding(['sender': sender, 'label': label, 'args': args])
        new GroovyShell(binding).evaluate(this.shellCode)
        return true
    }

    @Override
    List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return new ArrayList<>()
    }
}
