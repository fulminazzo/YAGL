package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.serializer.SerializableFunctionSerializer;
import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import it.fulminazzo.yagl.serializer.TypedSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class GUISerializerTest extends SerializerTestHelper<GUI> {

    protected GUISerializerTest() {
        super(GUI.class, new GUISerializer());
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testDeserialize(
            final @NotNull GUI expected,
            final @NotNull Map<String, Object> raw,
            final @Nullable CustomCheck perGUICheck
    ) throws IOException {
        GUI gui = load(getFileName(expected));
        assertNotNull(gui, "Expected loaded GUI to not be null");
        assertEquals(expected.getTitle(), gui.getTitle(), "Incorrect title");
        assertEquals(expected.getMovableSlots(), gui.getMovableSlots(), "Incorrect movable slots");
        assertEquals(expected.variables(), gui.variables(), "Incorrect variables");
        assertEquals(expected.clickOutsideAction().orElse(null), gui.clickOutsideAction().orElse(null), "Incorrect click outside action");
        assertEquals(expected.openGUIAction().orElse(null), gui.openGUIAction().orElse(null), "Incorrect open GUI action");
        assertEquals(expected.changeGUIAction().orElse(null), gui.changeGUIAction().orElse(null), "Incorrect change GUI action");
        assertEquals(expected.closeGUIAction().orElse(null), gui.closeGUIAction().orElse(null), "Incorrect close GUI action");

        assertEquals(expected.getContents().size(), gui.getContents().size(), "Incorrect contents size");

        assertEquals(expected, gui);
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testSerialize(
            final @NotNull GUI gui,
            final @NotNull Map<String, Object> expected,
            final @Nullable CustomCheck perGUICheck
    ) throws IOException {
        Map<?, ?> raw = readRawMap(dump(gui, getFileName(gui)));
        assertNotNull(raw, "Expected raw data to not be null");
        assertEquals(expected.get(GUISerializer.TITLE_PATH), raw.get(GUISerializer.TITLE_PATH), "Incorrect title");
        assertEquals(expected.get(GUISerializer.MOVABLE_SLOTS_PATH), raw.get(GUISerializer.MOVABLE_SLOTS_PATH), "Incorrect movable slots");
        assertEquals(expected.get(GUISerializer.VARIABLES_PATH), raw.get(GUISerializer.VARIABLES_PATH), "Incorrect variables");
        assertEquals(expected.get(GUISerializer.CLICK_OUTSIDE_ACTION_PATH), raw.get(GUISerializer.CLICK_OUTSIDE_ACTION_PATH), "Incorrect click outside action");
        assertEquals(expected.get(GUISerializer.OPEN_GUI_ACTION_PATH), raw.get(GUISerializer.OPEN_GUI_ACTION_PATH), "Incorrect open GUI action");
        assertEquals(expected.get(GUISerializer.CLOSE_GUI_ACTION_PATH), raw.get(GUISerializer.CLOSE_GUI_ACTION_PATH), "Incorrect close GUI action");
        assertEquals(expected.get(GUISerializer.CHANGE_GUI_ACTION_PATH), raw.get(GUISerializer.CHANGE_GUI_ACTION_PATH), "Incorrect change GUI action");

        Object contents = raw.get(GUISerializer.CONTENTS_PATH);
        assertInstanceOf(Map.class, contents, "Expected contents to be a map");
        Map<?, ?> expectedContentsMap = (Map<?, ?>) expected.get(GUISerializer.CONTENTS_PATH);
        int expectedContentsCount = expectedContentsMap.size();
        Map<?, ?> contentsMap = (Map<?, ?>) contents;
        assertEquals(
                expectedContentsCount,
                contentsMap.size(),
                "Invalid contents count, expected " + expectedContentsCount + " but got: " + contents
        );
        assertIterableEquals(expectedContentsMap.keySet(), contentsMap.keySet(), "Invalid contents keys");
        for (Object key : contentsMap.keySet()) {
            Object slotContents = contentsMap.get(key);
            assertInstanceOf(List.class, slotContents, "Expected contents per slot to be a list");
            List<?> slotContentsList = (List<?>) slotContents;
            Object expectedSlotContentsCount = expectedContentsMap.get(key);
            assertEquals(
                    expectedSlotContentsCount,
                    slotContentsList.size(),
                    "Invalid contents count at slot " + key + ", expected " + expectedSlotContentsCount + " but got: " + slotContentsList
            );

            for (Object content : slotContentsList) {
                assertInstanceOf(Map.class, content, "Expected content to be a map");
                Map<?, ?> contentMap = (Map<?, ?>) content;
                assertEquals("ITEM", contentMap.get(TypedSerializer.TYPE_PATH), "Expected content type to be ITEM");
            }
        }

        if (perGUICheck != null) perGUICheck.accept(raw, expected);
    }

    private static Object[][] testData() {
        Map<String, Integer> contents = new HashMap<>(Map.of(
                "1", 1,
                "5", 2,
                "10", 3
        ));
        Map<String, Object> baseExpected = new HashMap<>(Map.of(
                GUISerializer.TITLE_PATH, "Awesome GUI",
                GUISerializer.CONTENTS_PATH, contents,
                GUISerializer.MOVABLE_SLOTS_PATH, new ArrayList<>(List.of(5, 9)),
                GUISerializer.VARIABLES_PATH, new HashMap<>(Map.of("hello", "world")),
                GUISerializer.CLICK_OUTSIDE_ACTION_PATH, Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, "GUI_COMMAND",
                        SerializableFunctionSerializer.CONTENT_PATH, "say I clicked outside!"
                ),
                GUISerializer.OPEN_GUI_ACTION_PATH, Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, "GUI_COMMAND",
                        SerializableFunctionSerializer.CONTENT_PATH, "say I opened the GUI!"
                ),
                GUISerializer.CLOSE_GUI_ACTION_PATH, Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, "GUI_COMMAND",
                        SerializableFunctionSerializer.CONTENT_PATH, "say I closed the GUI!"
                ),
                GUISerializer.CHANGE_GUI_ACTION_PATH, Map.of(
                        SerializableFunctionSerializer.TYPE_PATH, "BI_GUI_COMMAND",
                        SerializableFunctionSerializer.CONTENT_PATH, "say I changed the GUI!"
                )
        ));
        Function<Map<String, Object>, Map<String, Object>> combineMap = m -> {
            Map<String, Object> combined = new HashMap<>(baseExpected);
            combined.putAll(m);
            return combined;
        };
        return new Object[][]{
                {
                        populateGUI(GUI.newGUI(27)),
                        combineMap.apply(Map.of(DefaultGUISerializer.SIZE_PATH, 27)),
                        ((CustomCheck) (raw, expected) -> {
                            assertEquals(expected.get(DefaultGUISerializer.SIZE_PATH), raw.get(DefaultGUISerializer.SIZE_PATH));
                        })
                }
        };
    }

    private static @NotNull String getFileName(final @NotNull GUI gui) {
        return gui.getClass().getSimpleName().toLowerCase(Locale.ROOT);
    }

    private static @NotNull GUI populateGUI(final @NotNull GUI gui) {
        return gui
                .setTitle("Awesome GUI")
                .setContents(1, ItemGUIContent.newInstance("stone"))
                .setContents(5, ItemGUIContent.newInstance("diamond"), ItemGUIContent.newInstance("emerald"))
                .setContents(10,
                        ItemGUIContent.newInstance("gold"),
                        ItemGUIContent.newInstance("iron"),
                        ItemGUIContent.newInstance("redstone")
                )
                .setMovable(5, true).setMovable(9, true)
                .setVariable("hello", "world")
                .onClickOutside("say I clicked outside!")
                .onOpenGUI("say I opened the GUI!")
                .onCloseGUI("say I closed the GUI!")
                .onChangeGUI("say I changed the GUI!")
                ;
    }

    @FunctionalInterface
    private interface CustomCheck extends BiConsumer<Map<?, ?>, Map<String, Object>> {
    }

}