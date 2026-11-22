package it.fulminazzo.yagl.gui;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * A helper class for GUI serializers that test four variants of the same GUI type
 * (e.g., size-based, type-based, fullscreen size-based and fullscreen type-based construction).
 *
 * @param <G> the type of the GUI
 */
abstract class QuadGUISerializerTestHelper<G extends GUI> extends DualGUISerializerTestHelper<G> {

    /**
     * Instantiates a new QuadGUISerializerTestHelper.
     *
     * @param type the type of the GUI
     */
    protected QuadGUISerializerTestHelper(final @NotNull Class<G> type) {
        super(type);
    }

    @Test
    final void testDeserializeFullscreen() throws IOException {
        G expected = getTargetFullscreen();
        G gui = load(getFileNameFullscreen(expected));
        assertNotNull(gui, "Loaded GUI should not be null");
        checkDeserializeAssertions(expected, gui);
        testDeserializeFullscreen(gui);
    }

    @Test
    final void testSerializeFullscreen() throws IOException {
        G gui = getTargetFullscreen();
        Map<?, ?> raw = readRawMap(dump(gui, getFileNameFullscreen(gui)));
        assertNotNull(raw, "Map data should not be null");
        checkSerializeAssertions(raw);
        testSerializeFullscreen(raw);
    }

    @Test
    final void testDeserializeFullscreenTyped() throws IOException {
        G expected = getTargetFullscreenTyped();
        G gui = load(getFileNameFullscreenTyped(expected));
        assertNotNull(gui, "Loaded GUI should not be null");
        checkDeserializeAssertions(expected, gui);
        testDeserializeFullscreenTyped(gui);
    }

    @Test
    final void testSerializeFullscreenTyped() throws IOException {
        G gui = getTargetFullscreenTyped();
        Map<?, ?> raw = readRawMap(dump(gui, getFileNameFullscreenTyped(gui)));
        assertNotNull(raw, "Map data should not be null");
        checkSerializeAssertions(raw);
        testSerializeFullscreenTyped(raw);
    }

    /**
     * Override this method to add more assertions for the deserialized fullscreen GUI.
     *
     * @param gui the deserialized fullscreen GUI
     */
    protected void testDeserializeFullscreen(final @NotNull G gui) {
        // Override to add custom checks
    }

    /**
     * Override this method to add more assertions for the serialized fullscreen GUI.
     *
     * @param raw the serialized fullscreen GUI data
     */
    protected void testSerializeFullscreen(final @NotNull Map<?, ?> raw) {
        // Override to add custom checks
    }

    /**
     * Override this method to add more assertions for the deserialized fullscreen typed GUI.
     *
     * @param gui the deserialized fullscreen typed GUI
     */
    protected void testDeserializeFullscreenTyped(final @NotNull G gui) {
        // Override to add custom checks
    }

    /**
     * Override this method to add more assertions for the serialized fullscreen typed GUI.
     *
     * @param raw the serialized fullscreen typed GUI data
     */
    protected void testSerializeFullscreenTyped(final @NotNull Map<?, ?> raw) {
        // Override to add custom checks
    }

    /**
     * Gets the fullscreen GUI target for the fullscreen serialization and deserialization tests.
     *
     * @return the fullscreen GUI target
     */
    protected abstract @NotNull G getTargetFullscreen();

    /**
     * Gets the fullscreen typed GUI target for the fullscreen typed serialization and deserialization tests.
     *
     * @return the fullscreen typed GUI target
     */
    protected abstract @NotNull G getTargetFullscreenTyped();

    /**
     * Gets the corresponding file name for the given fullscreen GUI.
     *
     * @param gui the fullscreen GUI
     * @return the file name
     */
    protected static @NotNull String getFileNameFullscreen(final @NotNull GUI gui) {
        return gui.getClass().getSimpleName().toLowerCase(Locale.ROOT) + "_fullscreen";
    }

    /**
     * Gets the corresponding file name for the given fullscreen typed GUI.
     *
     * @param gui the fullscreen typed GUI
     * @return the file name
     */
    protected static @NotNull String getFileNameFullscreenTyped(final @NotNull GUI gui) {
        return gui.getClass().getSimpleName().toLowerCase(Locale.ROOT) + "_fullscreen_type";
    }

}
