package it.fulminazzo.yagl.command

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
final class ShellCommand extends Command {
    private static final String NUMBER_FORMAT_REGEX = '(catch *\\(NumberFormatException +ignored\\) *\\{' +
            '\\n)[ \\t]*(?:// auto-generated code)?(\\n *})'
    private static final String INVALID_NUMBER_CODE = 'sender.sendMessage(ignored.message.replace(' +
            '\'For input string: \', \'Invalid number \'))'
    private final String shellCode

    /**
     * Instantiates a new shell command
     *
     * @param file  the file containing the script
     */
    ShellCommand(final @NotNull File file) {
        super(file.name.substring(0, file.name.lastIndexOf('.')))
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
    boolean execute(final @NotNull CommandSender sender, final @NotNull String label, final @NotNull String[] args) {
        Binding binding = new Binding(['sender':sender, 'label':label, 'args':args])
        new GroovyShell(binding).evaluate(this.shellCode)
        return true
    }

    @Override
    List<String> tabComplete(final @NotNull CommandSender sender, final @NotNull String alias,
                             final @NotNull String[] args) throws IllegalArgumentException {
        return []
    }

}
