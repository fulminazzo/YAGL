package it.angrybear.yagl.actions;

import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionParsersTest {

    private static CommandAction[] getActions() {
        return new CommandAction[]{
                new BiGUICommand("command"),
                new GUIItemCommand("command2"),
                new GUICommand("command3"),
        };
    }

    @ParameterizedTest
    @MethodSource("getActions")
    void testCommandActions(CommandAction action) throws IOException {
        ActionParsers.addParsers();
        File file = new File("build/resources/test/command-action.yml");
        if (file.exists()) FileUtils.deleteFile(file);
        FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        configuration.set("tmp", action);
        configuration.save();
        configuration = new FileConfiguration(file);
        CommandAction action1 = configuration.get("tmp", action.getClass());
        assertEquals(action, action1);
    }
}