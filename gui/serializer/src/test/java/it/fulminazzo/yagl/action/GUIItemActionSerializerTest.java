package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.action.command.GUIItemCommand;
import it.fulminazzo.yagl.action.command.GUIItemConsoleCommand;
import it.fulminazzo.yagl.action.message.GUIItemMessage;
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

public class GUIItemActionSerializerTest extends SerializerTestHelper<GUIItemAction> {

    protected GUIItemActionSerializerTest() {
        super(GUIItemAction.class, GUIItemActionSerializer.getInstance());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testDeserialize(final @NotNull GUIItemAction expected, final @NotNull Object raw) throws IOException {
        Class<? extends @NotNull GUIItemAction> expectedType = expected.getClass();
        GUIItemAction actual = load(getFileName(expected));
        assertNotNull(actual);
        try {
            assertEquals(expected.serialize(), actual.serialize());
        } catch (UnsupportedOperationException e) {
            // pass for GUIItemBack and GUIItemClose
        }
        assumeFalse(expectedType.isSynthetic());
        assertEquals(expectedType, actual.getClass());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testSerialize(final @NotNull GUIItemAction guiAction, final @NotNull Object expected) throws IOException {
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
                {((GUIItemAction) (e) -> e.getViewer().sendMessage("Hello, world!")), ""},
                {new GUIItemMessage("Hello, world!"), Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, GUIItemActionSerializer.GUI_MESSAGE_TYPE_NAME,
                        SerializableFunctionSerializer.CONTENT_PATH, "Hello, world!"
                )},
                {new GUIItemCommand("say Hello, world!"), Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, GUIItemActionSerializer.GUI_COMMAND_TYPE_NAME,
                        SerializableFunctionSerializer.CONTENT_PATH, "say Hello, world!"
                )},
                {new GUIItemConsoleCommand("say Hello, world!"), Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, GUIItemActionSerializer.GUI_CONSOLE_COMMAND_TYPE_NAME,
                        SerializableFunctionSerializer.CONTENT_PATH, "say Hello, world!"
                )},
                {new GUIItemClose(), GUIItemActionSerializer.GUI_ITEM_CLOSE_TYPE_NAME},
                {new GUIItemBack(), GUIItemActionSerializer.GUI_ITEM_BACK_TYPE_NAME}
        };
    }

    private static @NotNull String getFileName(final @NotNull GUIItemAction action) {
        Class<? extends @NotNull GUIItemAction> actionType = action.getClass();
        String fileName = actionType.getSimpleName().toLowerCase(Locale.ROOT);
        if (actionType.isSynthetic()) fileName = "base";
        return fileName;
    }

}
