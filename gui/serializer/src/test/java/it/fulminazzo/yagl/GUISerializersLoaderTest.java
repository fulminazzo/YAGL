package it.fulminazzo.yagl;

import it.fulminazzo.yagl.action.BiGUIAction;
import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.action.GUIItemAction;
import it.fulminazzo.yagl.action.command.*;
import it.fulminazzo.yagl.action.message.BiGUIMessage;
import it.fulminazzo.yagl.action.message.GUIItemMessage;
import it.fulminazzo.yagl.action.message.GUIMessage;
import it.fulminazzo.yagl.content.GUIContent;
import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.content.requirement.PermissionRequirement;
import it.fulminazzo.yagl.content.requirement.RequirementChecker;
import it.fulminazzo.yagl.gui.DefaultGUI;
import it.fulminazzo.yagl.gui.GUI;
import it.fulminazzo.yagl.serializer.SerializationException;
import it.fulminazzo.yagl.serializer.YaglSerializer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GUISerializersLoaderTest {

    private static Class<?>[] testTypes() {
        return new Class<?>[]{
                GUIAction.class, GUIMessage.class, GUICommand.class, GUIConsoleCommand.class,
                BiGUIAction.class, BiGUIMessage.class, BiGUICommand.class, BiGUIConsoleCommand.class,
                GUIItemAction.class, GUIItemMessage.class, GUIItemCommand.class, GUIItemConsoleCommand.class,
                RequirementChecker.class, PermissionRequirement.class,
                GUIContent.class, ItemGUIContent.class,
                GUI.class, DefaultGUI.class
        };
    }

    @ParameterizedTest
    @MethodSource("testTypes")
    void testThatSerializerIsRegisteredFor(final @NotNull Class<?> type) throws SerializationException {
        assertNotNull(YaglSerializer.getInstance().getSerializer(type));
    }

}