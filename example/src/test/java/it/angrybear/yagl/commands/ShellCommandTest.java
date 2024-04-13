package it.angrybear.yagl.commands;

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

}