package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.serializer.TypedSerializer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Getter
class PageableGUISerializerTest extends DualGUISerializerTestHelper<PageableGUI> {
    private final @NotNull PageableGUI target;
    private final @NotNull PageableGUI targetTyped;

    protected PageableGUISerializerTest() {
        super(PageableGUI.class);

        target = PageableGUI.newGUI(27);
        populateGUI(target);
        target.setPages(3)
                .setPreviousPage(11, ItemGUIContent.newInstance("arrow"))
                .setNextPage(15, ItemGUIContent.newInstance("paper"));
        target.getPage(0).setContents(1, ItemGUIContent.newInstance("coal"))
                .setContents(5, ItemGUIContent.newInstance("iron_ingot"))
                .setContents(10, ItemGUIContent.newInstance("lapis_lazuli"));
        target.getPage(2).setContents(5, ItemGUIContent.newInstance("diamond"));

        targetTyped = PageableGUI.newGUI(GUIType.CHEST);
        populateGUI(targetTyped);
        targetTyped.setPages(3)
                .setPreviousPage(11, ItemGUIContent.newInstance("arrow"))
                .setNextPage(15, ItemGUIContent.newInstance("paper"));
        targetTyped.getPage(0).setContents(1, ItemGUIContent.newInstance("coal"))
                .setContents(5, ItemGUIContent.newInstance("iron_ingot"))
                .setContents(10, ItemGUIContent.newInstance("lapis_lazuli"));
        targetTyped.getPage(2).setContents(5, ItemGUIContent.newInstance("diamond"));
    }

    @Override
    protected void testDeserialize(final @NotNull PageableGUI gui) {
        assertEquals(target.pages(), gui.pages(), "Incorrect number of pages");
        assertEquals(3, gui.getPage(0).getContents().size(), "Page 0 should have 3 contents");
        assertEquals(0, gui.getPage(1).getContents().size(), "Page 1 should be empty");
        assertEquals(1, gui.getPage(2).getContents().size(), "Page 2 should have 1 content");
    }

    @Override
    protected void testSerialize(final @NotNull Map<?, ?> raw) {
        assertEquals(target.pages(), raw.get(PageableGUISerializer.PAGES_PATH));
        Map<?, ?> guiPages = (Map<?, ?>) raw.get(PageableGUISerializer.GUI_PAGES_PATH);
        assertEquals(target.pages(), guiPages.size(), "Should have " + target.pages() + " gui pages");
        for (Object key : guiPages.keySet()) {
            Map<?, ?> page = (Map<?, ?>) guiPages.get(key);
            assertNull(page.get(DefaultGUISerializer.SIZE_PATH), "Page " + key + " should not have size");
            assertNull(page.get(TypeGUISerializer.INVENTORY_TYPE_PATH), "Page " + key + " should not have inventoryType");
            assertNull(page.get(TypedSerializer.TYPE_PATH), "Page " + key + " should not have type");
        }
    }

    @Override
    protected void testDeserializeTyped(final @NotNull PageableGUI gui) {
        assertEquals(targetTyped.pages(), gui.pages(), "Incorrect number of pages");
        assertEquals(3, gui.getPage(0).getContents().size(), "Page 0 should have 3 contents");
        assertEquals(0, gui.getPage(1).getContents().size(), "Page 1 should be empty");
        assertEquals(1, gui.getPage(2).getContents().size(), "Page 2 should have 1 content");
    }

    @Override
    protected void testSerializeTyped(final @NotNull Map<?, ?> raw) {
        assertEquals(
                ((TypeGUI) targetTyped.templateGUI).getInventoryType().name(),
                raw.get(PageableGUISerializer.GUI_TYPE_PATH),
                "Incorrect template GUI inventory type"
        );
        assertEquals(targetTyped.pages(), raw.get(PageableGUISerializer.PAGES_PATH));
        Map<?, ?> guiPages = (Map<?, ?>) raw.get(PageableGUISerializer.GUI_PAGES_PATH);
        assertEquals(targetTyped.pages(), guiPages.size(), "Should have " + targetTyped.pages() + " gui pages");
        for (Object key : guiPages.keySet()) {
            Map<?, ?> page = (Map<?, ?>) guiPages.get(key);
            assertNull(page.get(DefaultGUISerializer.SIZE_PATH), "Page " + key + " should not have size");
            assertNull(page.get(TypeGUISerializer.INVENTORY_TYPE_PATH), "Page " + key + " should not have inventoryType");
            assertNull(page.get(TypedSerializer.TYPE_PATH), "Page " + key + " should not have type");
        }
    }

}
