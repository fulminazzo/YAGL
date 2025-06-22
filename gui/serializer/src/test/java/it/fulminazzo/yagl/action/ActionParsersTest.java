package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.action.command.*;
import it.fulminazzo.yagl.action.message.BiGUIMessage;
import it.fulminazzo.yagl.action.message.GUIItemMessage;
import it.fulminazzo.yagl.action.message.GUIMessage;
import it.fulminazzo.yagl.action.message.MessageAction;
import it.fulminazzo.yamlparser.configuration.FileConfiguration;
import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ActionParsersTest {

    @BeforeAll
    static void setAllUp() {
        ActionParsers.addParsers();
    }

    private static CommandAction[] getCommandActions() {
        return new CommandAction[]{
                new BiGUICommand("command1"),
                new BiGUIConsoleCommand("command2"),
                new GUIItemCommand("command3"),
                new GUIItemConsoleCommand("command4"),
                new GUICommand("command5"),
                new GUIConsoleCommand("command6")
        };
    }

    @ParameterizedTest
    @MethodSource("getCommandActions")
    void testCommandActions(CommandAction action) throws IOException {
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

    private static MessageAction[] getMessageActions() {
        return new MessageAction[]{
                new BiGUIMessage("message1"),
                new GUIItemMessage("message3"),
                new GUIMessage("message5"),
        };
    }

    @ParameterizedTest
    @MethodSource("getMessageActions")
    void testMessageActions(MessageAction action) throws IOException {
        File file = new File("build/resources/test/message-action.yml");
        if (file.exists()) FileUtils.deleteFile(file);
        FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        configuration.set("tmp", action);
        configuration.save();
        configuration = new FileConfiguration(file);
        MessageAction action1 = configuration.get("tmp", action.getClass());
        assertEquals(action, action1);
    }

    @Test
    void testBackAction() throws IOException {
        GUIItemAction expected = new GUIItemBack();
        File file = new File("build/resources/test/back-action.yml");
        if (file.exists()) FileUtils.deleteFile(file);
        FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        configuration.set("tmp", expected);
        configuration.save();
        configuration = new FileConfiguration(file);
        GUIItemAction actual = configuration.get("tmp", expected.getClass());
        assertNotNull(actual);
        assertEquals(expected.getClass(), actual.getClass());
        assertNull(configuration.getString("tmp.content"), "Content should be empty");
    }

    @Test
    void testCloseAction() throws IOException {
        GUIItemAction expected = new GUIItemClose();
        File file = new File("build/resources/test/close-action.yml");
        if (file.exists()) FileUtils.deleteFile(file);
        FileUtils.createNewFile(file);
        FileConfiguration configuration = new FileConfiguration(file);
        configuration.set("tmp", expected);
        configuration.save();
        configuration = new FileConfiguration(file);
        GUIItemAction actual = configuration.get("tmp", expected.getClass());
        assertNotNull(actual);
        assertEquals(expected.getClass(), actual.getClass());
        assertNull(configuration.getString("tmp.content"), "Content should be empty");
    }

}