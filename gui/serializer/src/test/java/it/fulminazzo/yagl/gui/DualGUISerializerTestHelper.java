package it.fulminazzo.yagl.gui;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * A helper class for GUI serializers that test two variants of the same GUI type
 * (e.g., size-based and type-based construction).
 *
 * @param <G> the type of the GUI
 */
abstract class DualGUISerializerTestHelper<G extends GUI> extends GUISerializerTestHelper<G> {

    /**
     * Instantiates a new DualGUISerializerTestHelper.
     *
     * @param type the type of the GUI
     */
    protected DualGUISerializerTestHelper(final @NotNull Class<G> type) {
        super(type);
    }

    @Test
    final void testDeserializeTyped() throws IOException {
        G expected = getTargetTyped();
        G gui = load(getFileName(expected));
        assertNotNull(gui, "Loaded GUI should not be null");
        checkDeserializeAssertions(expected, gui);
        testDeserializeTyped(gui);
    }

    @Test
    final void testSerializeTyped() throws IOException {
        G gui = getTargetTyped();
        Map<?, ?> raw = readRawMap(dump(gui, getFileName(gui)));
        assertNotNull(raw, "Map data should not be null");
        checkSerializeAssertions(raw);
        testSerializeTyped(raw);
    }

    /**
     * Override this method to add more assertions for the deserialized typed GUI.
     *
     * @param gui the deserialized typed GUI
     */
    protected void testDeserializeTyped(final @NotNull G gui) {
        // Override to add custom checks
    }

    /**
     * Override this method to add more assertions for the serialized typed GUI.
     *
     * @param raw the serialized typed GUI data
     */
    protected void testSerializeTyped(final @NotNull Map<?, ?> raw) {
        // Override to add custom checks
    }

    /**
     * Gets the typed GUI target for the typed serialization and deserialization tests.
     *
     * @return the typed GUI target
     */
    protected abstract @NotNull G getTargetTyped();

}
