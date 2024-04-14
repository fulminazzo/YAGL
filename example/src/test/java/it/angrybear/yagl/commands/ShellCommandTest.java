package it.angrybear.yagl.commands;

import it.fulminazzo.fulmicollection.objects.Refl;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ShellCommandTest {

    @Test
    void testExecute() {
        // Prepare file
        File file = new File("build/resources/test/shell-command.groovy");
        // Prepare sender
        AtomicReference<String> message = new AtomicReference<>();
        CommandSender sender = mock(CommandSender.class);
        doAnswer(a -> {
            message.set(a.getArgument(0));
            return null;
        }).when(sender).sendMessage(anyString());

        ShellCommand shellCommand = new ShellCommand(file);
        shellCommand.execute(sender, "command", new String[0]);

        assertEquals("Hello world", message.get());
    }

    @Test
    void testNumberFormatExceptionReplacement() {
        String expected = "def run = { sender, label, args ->\n" +
                "    try {\n" +
                "        sender.sendMessage(\"Your number is ${Integer.valueOf(args[0])}\")\n" +
                "    } catch (NumberFormatException ignored) {\n" +
                "    sender.sendMessage(e.getMessage().replace('For input string: ', 'Invalid number '))\n" +
                "    }\n" +
                "}\n" +
                "run(sender, label, args)";
        File file = new File("build/resources/test/number-format-exception-command.groovy");

        ShellCommand shellCommand = new ShellCommand(file);
        String actual = new Refl<>(shellCommand).getFieldObject("shellCode");

        assertEquals(expected, actual);
    }

}