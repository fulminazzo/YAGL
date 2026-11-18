package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.action.command.GUICommand;
import it.fulminazzo.yagl.action.command.GUIConsoleCommand;
import it.fulminazzo.yagl.action.message.GUIMessage;
import it.fulminazzo.yagl.serializer.SerializableFunctionSerializer;
import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

public class GUIActionSerializerTest extends SerializerTestHelper<GUIAction> {

    protected GUIActionSerializerTest() {
        super(GUIAction.class, GUIActionSerializer.getInstance());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testDeserialize(final @NotNull GUIAction expected, final @NotNull Object raw) throws IOException {
        Class<? extends @NotNull GUIAction> expectedType = expected.getClass();
        GUIAction actual = load(getFileName(expected));
        assertNotNull(actual);
        assertEquals(expected.serialize(), actual.serialize());
        assumeFalse(expectedType.isSynthetic());
        assertEquals(expectedType, actual.getClass());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testSerialize(final @NotNull GUIAction guiAction, final @NotNull Object expected) throws IOException {
        String fileName = getFileName(guiAction);
        if (expected instanceof String) {
            String raw = readRawString(dump(guiAction, fileName));
            assertNotNull(raw, "Content should not have been null for base type");
            assertFalse(raw.isEmpty(), "Content should not have been empty for base type");
        } else {
            Map<?, ?> raw = readRawMap(dump(guiAction, fileName));
            assertEquals(expected, raw);
        }
    }

    private static Object[][] testData() {
        return new Object[][]{
                {((GUIAction) (v, g) -> v.sendMessage("Hello, world!")), ""},
                {new GUIMessage("Hello, world!"), Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, GUIActionSerializer.GUI_MESSAGE_TYPE_NAME,
                        SerializableFunctionSerializer.CONTENT_PATH, "Hello, world!"
                )},
                {new GUICommand("say Hello, world!"), Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, GUIActionSerializer.GUI_COMMAND_TYPE_NAME,
                        SerializableFunctionSerializer.CONTENT_PATH, "say Hello, world!"
                )},
                {new GUIConsoleCommand("say Hello, world!"), Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, GUIActionSerializer.GUI_CONSOLE_COMMAND_TYPE_NAME,
                        SerializableFunctionSerializer.CONTENT_PATH, "say Hello, world!"
                )}
        };
    }

    private static @NotNull String getFileName(final @NotNull GUIAction action) {
        Class<? extends @NotNull GUIAction> actionType = action.getClass();
        String fileName = actionType.getSimpleName().toLowerCase(Locale.ROOT);
        if (actionType.isSynthetic()) fileName = "base";
        return fileName;
    }

}
