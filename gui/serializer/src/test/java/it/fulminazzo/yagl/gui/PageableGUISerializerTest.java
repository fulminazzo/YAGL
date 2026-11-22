package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.content.ItemGUIContent;
import it.fulminazzo.yagl.serializer.TypedSerializer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Getter
class PageableGUISerializerTest extends QuadGUISerializerTestHelper<PageableGUI> {
    private final @NotNull PageableGUI target;
    private final @NotNull PageableGUI targetTyped;
    private final @NotNull PageableGUI targetFullscreen;
    private final @NotNull PageableGUI targetFullscreenTyped;

    protected PageableGUISerializerTest() {
        super(PageableGUI.class);

        target = populatePageableGUI(PageableGUI.newPageableGUI(27));
        targetTyped = populatePageableGUI(PageableGUI.newPageableGUI(GUIType.CHEST));
        targetFullscreen = populatePageableFullscreenGUI(PageableGUI.newFullscreenGUI(27, 18));
        targetFullscreenTyped = populatePageableFullscreenGUI(PageableGUI.newFullscreenGUI(GUIType.CHEST, 18));
    }

    @Override
    protected void testDeserialize(final @NotNull PageableGUI gui) {
        checkPageableGUIDeserializeAssertions(target, gui);
    }

    @Override
    protected void testSerialize(final @NotNull Map<?, ?> raw) {
        assertEquals(target.size(), raw.get(PageableGUISerializer.GUI_SIZE_PATH), "Incorrect GUI size");
        checkPageableGUISerializeAssertions(target, raw);
    }

    @Override
    protected void testDeserializeTyped(final @NotNull PageableGUI gui) {
        checkPageableGUIDeserializeAssertions(targetTyped, gui);
    }

    @Override
    protected void testSerializeTyped(final @NotNull Map<?, ?> raw) {
        assertEquals(
                ((TypeGUI) targetTyped.templateGUI).getInventoryType().name(),
                raw.get(PageableGUISerializer.GUI_TYPE_PATH),
                "Incorrect template GUI inventory type"
        );
        checkPageableGUISerializeAssertions(targetTyped, raw);
    }

    @Override
    protected void testDeserializeFullscreen(final @NotNull PageableGUI gui) {
        checkPageableGUIDeserializeAssertions(targetFullscreen, gui);
    }

    @Override
    protected void testSerializeFullscreen(final @NotNull Map<?, ?> raw) {
        checkPageableGUISerializeAssertions(targetFullscreen, raw);
    }

    @Override
    protected void testDeserializeFullscreenTyped(final @NotNull PageableGUI gui) {
        checkPageableGUIDeserializeAssertions(targetFullscreenTyped, gui);
    }

    @Override
    protected void testSerializeFullscreenTyped(final @NotNull Map<?, ?> raw) {
        checkPageableGUISerializeAssertions(targetFullscreenTyped, raw);
    }

    private void checkPageableGUISerializeAssertions(final @NotNull PageableGUI target, final @NotNull Map<?, ?> raw) {
        int expectedPages = target.pages();
        assertEquals(expectedPages, raw.get(PageableGUISerializer.PAGES_PATH));
        Map<?, ?> guiPages = (Map<?, ?>) raw.get(PageableGUISerializer.GUI_PAGES_PATH);
        assertNotNull(guiPages, "GUI pages should not be null");
        int expectedGUIPages = expectedPages - 1;
        assertEquals(expectedGUIPages, guiPages.size(), "Should have " + expectedGUIPages + " gui pages");
        for (Object key : guiPages.keySet()) {
            Map<?, ?> page = (Map<?, ?>) guiPages.get(key);
            assertNull(page.get(DefaultGUISerializer.SIZE_PATH), "Page " + key + " should not have size");
            assertNull(page.get(TypeGUISerializer.INVENTORY_TYPE_PATH), "Page " + key + " should not have inventoryType");
            assertNull(page.get(TypedSerializer.TYPE_PATH), "Page " + key + " should not have type");
        }
        assertFalse(guiPages.containsKey("1"), PageableGUISerializer.GUI_PAGES_PATH + " should not contain second empty page");
    }

    private void checkPageableGUIDeserializeAssertions(final @NotNull PageableGUI target, final @NotNull PageableGUI gui) {
        assertEquals(target.pages(), gui.pages(), "Incorrect number of pages");
        int firstPageContents = target.getPage(0).getContents().size();
        assertEquals(firstPageContents, gui.getPage(0).getContents().size(), "Page 0 should have " + firstPageContents + " contents");
        int secondPageContents = target.getPage(1).getContents().size();
        assertEquals(secondPageContents, gui.getPage(1).getContents().size(), "Page 1 should have " + secondPageContents + " contents");
        int thirdPageContents = target.getPage(2).getContents().size();
        assertEquals(thirdPageContents, gui.getPage(2).getContents().size(), "Page 2 should have " + thirdPageContents + " contents");
    }

    private static @NotNull PageableGUI populatePageableGUI(final @NotNull PageableGUI target) {
        populateGUI(target);
        target.setPages(3)
                .setPreviousPage(11, ItemGUIContent.newInstance("arrow"))
                .setNextPage(15, ItemGUIContent.newInstance("paper"));
        target.getPage(0).setContents(1, ItemGUIContent.newInstance("coal"))
                .setContents(5, ItemGUIContent.newInstance("iron_ingot"))
                .setContents(10, ItemGUIContent.newInstance("lapis_lazuli"));
        target.getPage(2).setContents(5, ItemGUIContent.newInstance("diamond"));
        return target;
    }

    private static @NotNull PageableGUI populatePageableFullscreenGUI(final @NotNull PageableGUI target) {
        return populatePageableGUI(target)
                .setContents(27, ItemGUIContent.newInstance("coal"))
                .setContents(42, ItemGUIContent.newInstance("lapis_lazuli"));
    }

}
