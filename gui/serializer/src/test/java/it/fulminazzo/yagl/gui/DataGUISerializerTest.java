package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.content.ItemGUIContent;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Getter
class DataGUISerializerTest extends QuadGUISerializerTestHelper<DataGUI<?>> {
    private final @NotNull DataGUI<?> target;
    private final @NotNull DataGUI<?> targetTyped;
    private final @NotNull DataGUI<?> targetFullscreen;
    private final @NotNull DataGUI<?> targetFullscreenTyped;

    @SuppressWarnings("unchecked")
    protected DataGUISerializerTest() {
        super((Class<DataGUI<?>>) (Class<?>) DataGUI.class);

        target = populateDataGUI(DataGUI.newDataGUI(27));
        targetTyped = populateDataGUI(DataGUI.newDataGUI(GUIType.CHEST));
        targetFullscreen = populateDataFullscreenGUI(DataGUI.newFullscreenDataGUI(27, 18));
        targetFullscreenTyped = populateDataFullscreenGUI(DataGUI.newFullscreenDataGUI(GUIType.CHEST, 18));
    }

    @Override
    protected void testDeserialize(final @NotNull DataGUI<?> gui) {
        checkDataGUIDeserializeAssertions(target, gui);
    }

    @Override
    protected void testSerialize(final @NotNull Map<?, ?> raw) {
        assertEquals(target.size(), raw.get(PageableGUISerializer.GUI_SIZE_PATH), "Incorrect GUI size");
        checkDataGUISerializeAssertions(target, raw);
    }

    @Override
    protected void testDeserializeTyped(final @NotNull DataGUI<?> gui) {
        checkDataGUIDeserializeAssertions(targetTyped, gui);
    }

    @Override
    protected void testSerializeTyped(final @NotNull Map<?, ?> raw) {
        assertEquals(
                ((TypeGUI) targetTyped.templateGUI).getInventoryType().name(),
                raw.get(PageableGUISerializer.GUI_TYPE_PATH),
                "Incorrect template GUI inventory type"
        );
        checkDataGUISerializeAssertions(targetTyped, raw);
    }

    @Override
    protected void testDeserializeFullscreen(final @NotNull DataGUI<?> gui) {
        checkDataGUIDeserializeAssertions(targetFullscreen, gui);
    }

    @Override
    protected void testSerializeFullscreen(final @NotNull Map<?, ?> raw) {
        checkDataGUISerializeAssertions(targetFullscreen, raw);
    }

    @Override
    protected void testDeserializeFullscreenTyped(final @NotNull DataGUI<?> gui) {
        checkDataGUIDeserializeAssertions(targetFullscreenTyped, gui);
    }

    @Override
    protected void testSerializeFullscreenTyped(final @NotNull Map<?, ?> raw) {
        checkDataGUISerializeAssertions(targetFullscreenTyped, raw);
    }

    @Test
    void testSerializeWithData() throws IOException {
        DataGUI<String> gui = populateDataGUI(DataGUI.newDataGUI(27))
                .setData(ItemGUIContent::newInstance, "diamond", "coal");
        Map<?, ?> raw = readRawMap(dump(gui, getFileName(gui)));
        assertNotNull(raw, "Map data should not be null");
        checkDataGUISerializeAssertions(gui, raw);
    }

    private void checkDataGUISerializeAssertions(final @NotNull DataGUI<?> target, final @NotNull Map<?, ?> raw) {
        assertFalse(raw.containsKey("data"), "Data should not be serialized");
        assertFalse(raw.containsKey(PageableGUISerializer.PAGES_PATH), "Pages should not be serialized");
        assertFalse(raw.containsKey(PageableGUISerializer.GUI_PAGES_PATH), "GUI pages should not be serialized");
    }

    private void checkDataGUIDeserializeAssertions(final @NotNull DataGUI<?> target, final @NotNull DataGUI<?> gui) {
        assertTrue(gui.getData().isEmpty(), "Data should be empty");
        assertNull(gui.dataConverter, "Data converter should not be set");
        assertEquals(target.pages(), gui.pages(), "Incorrect number of pages");
        assertEquals(target.previousPage, gui.previousPage, "Incorrect previous page");
        assertEquals(target.nextPage, gui.nextPage, "Incorrect next page");
    }

    private static @NotNull DataGUI<?> populateDataGUI(final @NotNull DataGUI<?> target) {
        populateGUI(target);
        target.setPreviousPage(11, ItemGUIContent.newInstance("arrow"))
                .setNextPage(15, ItemGUIContent.newInstance("paper"));
        return target;
    }

    private static @NotNull DataGUI<?> populateDataFullscreenGUI(final @NotNull DataGUI<?> target) {
        populateDataGUI(target)
                .setContents(27, ItemGUIContent.newInstance("coal"))
                .setContents(42, ItemGUIContent.newInstance("lapis_lazuli"));
        return target;
    }
}