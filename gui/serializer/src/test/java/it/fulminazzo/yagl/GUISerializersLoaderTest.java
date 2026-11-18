package it.fulminazzo.yagl;

import it.fulminazzo.yagl.action.GUIAction;
import it.fulminazzo.yagl.action.GUIItemAction;
import it.fulminazzo.yagl.action.command.GUICommand;
import it.fulminazzo.yagl.action.command.GUIConsoleCommand;
import it.fulminazzo.yagl.action.command.GUIItemCommand;
import it.fulminazzo.yagl.action.command.GUIItemConsoleCommand;
import it.fulminazzo.yagl.action.message.GUIItemMessage;
import it.fulminazzo.yagl.action.message.GUIMessage;
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
                GUIItemAction.class, GUIItemMessage.class, GUIItemCommand.class, GUIItemConsoleCommand.class
        };
    }

    @ParameterizedTest
    @MethodSource("testTypes")
    void testThatSerializerIsRegisteredFor(final @NotNull Class<?> type) throws SerializationException {
        assertNotNull(YaglSerializer.getInstance().getSerializer(type));
    }

}