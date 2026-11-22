package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.serializer.SerializableFunctionSerializer;
import it.fulminazzo.yagl.serializer.Serializer;
import it.fulminazzo.yagl.serializer.SerializerTestHelper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A helper class to test the functionality of several {@link GUI} serializers.
 *
 * @param <G> the type of the GUI
 */
abstract class GUISerializerTestHelper<G extends GUI> extends SerializerTestHelper<G> {

    /**
     * Instantiates a new GUISerializerTestHelper.
     *
     * @param type the type of the GUI
     */
    @SuppressWarnings("unchecked")
    protected GUISerializerTestHelper(final @NotNull Class<G> type) {
        super(type, (Serializer<G>) new GUISerializer());
    }

    @Test
    final void testDeserialize() throws IOException {
        G expected = getTarget();
        G gui = load(getFileName(expected));
        assertNotNull(gui, "Loaded GUI should not be null");
        checkDeserializeAssertions(expected, gui);
        testDeserialize(gui);
    }

    @Test
    final void testSerialize() throws IOException {
        G gui = getTarget();
        Map<?, ?> raw = readRawMap(dump(gui, getFileName(gui)));
        assertNotNull(raw, "Map data should not be null");
        checkSerializeAssertions(raw);
        testSerialize(raw);
    }

    @Test
    final void testSerializeAndDeserialize() throws IOException {
        checkSerializeAndDeserialize(getTarget());
    }

    protected void checkSerializeAndDeserialize(final @NotNull G expected) throws IOException {
        G gui = readRaw(dump(expected, "roundtrip"), type);
        assertEquals(expected, gui);
    }

    protected void checkDeserializeAssertions(final @NotNull G expected, final @NotNull G gui) {
        assertNotNull(gui, "Expected loaded GUI to not be null");
        assertEquals(expected.getTitle(), gui.getTitle(), "Incorrect title");
        assertEquals(expected.getMovableSlots(), gui.getMovableSlots(), "Incorrect movable slots");
        assertEquals(expected.variables(), gui.variables(), "Incorrect variables");
        assertEquals(expected.clickOutsideAction().orElse(null), gui.clickOutsideAction().orElse(null), "Incorrect click outside action");
        assertEquals(expected.openGUIAction().orElse(null), gui.openGUIAction().orElse(null), "Incorrect open GUI action");
        assertEquals(expected.changeGUIAction().orElse(null), gui.changeGUIAction().orElse(null), "Incorrect change GUI action");
        assertEquals(expected.closeGUIAction().orElse(null), gui.closeGUIAction().orElse(null), "Incorrect close GUI action");

        assertEquals(expected.getContents().size(), gui.getContents().size(), "Incorrect contents size");

        for (int i = 0; i < expected.size(); i++)
            assertEquals(
                    expected.getContents(i),
                    gui.getContents(i),
                    "Incorrect contents at slot " + i
            );

        assertEquals(expected, gui);
    }

    protected void checkSerializeAssertions(final @NotNull Map<?, ?> raw) {
        assertNotNull(raw, "Expected raw data to not be null");
        assertEquals("Awesome GUI", raw.get(GUISerializer.TITLE_PATH), "Incorrect title");
        assertEquals(new ArrayList<>(List.of(5, 9)), raw.get(GUISerializer.MOVABLE_SLOTS_PATH), "Incorrect movable slots");
        assertEquals(new HashMap<>(Map.of("hello", "world")), raw.get(GUISerializer.VARIABLES_PATH), "Incorrect variables");
        assertEquals(Map.of(
                SerializableFunctionSerializer.TYPE_PATH, "GUI_COMMAND",
                SerializableFunctionSerializer.CONTENT_PATH, "say I clicked outside!"
        ), raw.get(GUISerializer.CLICK_OUTSIDE_ACTION_PATH), "Incorrect click outside action");
        assertEquals(Map.of(
                SerializableFunctionSerializer.TYPE_PATH, "GUI_COMMAND",
                SerializableFunctionSerializer.CONTENT_PATH, "say I opened the GUI!"
        ), raw.get(GUISerializer.OPEN_GUI_ACTION_PATH), "Incorrect open GUI action");
        assertEquals(Map.of(
                SerializableFunctionSerializer.TYPE_PATH, "GUI_COMMAND",
                SerializableFunctionSerializer.CONTENT_PATH, "say I closed the GUI!"
        ), raw.get(GUISerializer.CLOSE_GUI_ACTION_PATH), "Incorrect close GUI action");
        assertEquals(Map.of(
                SerializableFunctionSerializer.TYPE_PATH, "BI_GUI_COMMAND",
                SerializableFunctionSerializer.CONTENT_PATH, "say I changed the GUI!"
        ), raw.get(GUISerializer.CHANGE_GUI_ACTION_PATH), "Incorrect change GUI action");

        Object contents = raw.get(GUISerializer.CONTENTS_PATH);
        assertInstanceOf(Map.class, contents, "Expected contents to be a map");
        Map<?, ?> contentsMap = (Map<?, ?>) contents;

        Set<?> actualSlots = new TreeSet<>(contentsMap.keySet());
        for (Object key : actualSlots) {
            Object slotContents = contentsMap.get(key);
            assertInstanceOf(List.class, slotContents, "Expected contents per slot to be a list");
            List<?> slotContentsList = (List<?>) slotContents;

            for (Object content : slotContentsList)
                assertInstanceOf(Map.class, content, "Expected content to be a map");
        }
    }

    /**
     * Override this method to add more assertions for the deserialized GUI.
     *
     * @param gui the deserialized GUI
     */
    protected void testDeserialize(final @NotNull G gui) {
        // Override to add custom checks
    }

    /**
     * Override this method to add more assertions for the serialized GUI.
     *
     * @param raw the serialized GUI data
     */
    protected void testSerialize(final @NotNull Map<?, ?> raw) {
        // Override to add custom checks
    }

    /**
     * Gets the GUI target of serialization and deserialization.
     *
     * @return the GUI target
     */
    protected abstract @NotNull G getTarget();

    /**
     * Populates the GUI with default test data.
     *
     * @param gui the GUI
     * @return the populated GUI
     */
    protected static @NotNull GUI populateGUI(final @NotNull GUI gui) {
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

    /**
     * Gets the corresponding file name for the given GUI.
     *
     * @param gui the GUI
     * @return the file name
     */
    protected static @NotNull String getFileName(final @NotNull GUI gui) {
        String fileName = gui.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        boolean isTyped = false;
        if (gui instanceof FullscreenGUI && ((FullscreenGUI) gui).getUpperGUI() instanceof TypeGUI)
            isTyped = true;
        if (gui instanceof PageableGUI && ((PageableGUI) gui).templateGUI instanceof TypeGUI)
            isTyped = true;
        if (isTyped)
            fileName += "_type";
        return fileName;
    }

}