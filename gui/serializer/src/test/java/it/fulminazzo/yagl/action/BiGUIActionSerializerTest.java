package it.fulminazzo.yagl.action;

import it.fulminazzo.yagl.action.command.BiGUICommand;
import it.fulminazzo.yagl.action.command.BiGUIConsoleCommand;
import it.fulminazzo.yagl.action.message.BiGUIMessage;
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

public class BiGUIActionSerializerTest extends SerializerTestHelper<BiGUIAction> {

    protected BiGUIActionSerializerTest() {
        super(BiGUIAction.class, BiGUIActionSerializer.getInstance());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testDeserialize(final @NotNull BiGUIAction expected, final @NotNull Object raw) throws IOException {
        Class<? extends @NotNull BiGUIAction> expectedType = expected.getClass();
        BiGUIAction actual = load(getFileName(expected));
        assertNotNull(actual);
        assertEquals(expected.serialize(), actual.serialize());
        assumeFalse(expectedType.isSynthetic());
        assertEquals(expectedType, actual.getClass());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testSerialize(final @NotNull BiGUIAction guiAction, final @NotNull Object expected) throws IOException {
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
                {((BiGUIAction) (v, g1, g2) -> v.sendMessage("Hello, world!")), ""},
                {new BiGUIMessage("Hello, world!"), Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, BiGUIActionSerializer.BI_GUI_MESSAGE_TYPE_NAME,
                        SerializableFunctionSerializer.CONTENT_PATH, "Hello, world!"
                )},
                {new BiGUICommand("say Hello, world!"), Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, BiGUIActionSerializer.BI_GUI_COMMAND_TYPE_NAME,
                        SerializableFunctionSerializer.CONTENT_PATH, "say Hello, world!"
                )},
                {new BiGUIConsoleCommand("say Hello, world!"), Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, BiGUIActionSerializer.BI_GUI_CONSOLE_COMMAND_TYPE_NAME,
                        SerializableFunctionSerializer.CONTENT_PATH, "say Hello, world!"
                )}
        };
    }

    private static @NotNull String getFileName(final @NotNull BiGUIAction action) {
        Class<? extends @NotNull BiGUIAction> actionType = action.getClass();
        String fileName = actionType.getSimpleName().toLowerCase(Locale.ROOT);
        if (actionType.isSynthetic()) fileName = "base";
        return fileName;
    }

}
