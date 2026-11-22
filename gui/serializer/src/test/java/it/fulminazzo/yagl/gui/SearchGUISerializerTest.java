package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.content.ItemGUIContent;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Getter
class SearchGUISerializerTest extends GUISerializerTestHelper<SearchGUI<?>> {
    private final @NotNull SearchGUI<?> target;

    @SuppressWarnings("unchecked")
    protected SearchGUISerializerTest() {
        super((Class<SearchGUI<?>>) (Class<?>) SearchGUI.class);
        target = SearchGUI.newSearchGUI(18);
        populateGUI(target);
        target.setPreviousPage(11, ItemGUIContent.newInstance("arrow"));
        target.setNextPage(15, ItemGUIContent.newInstance("paper"));
    }

    @Override
    protected void testSerialize(final @NotNull Map<?, ?> raw) {
        assertEquals(18, raw.get(FullscreenGUISerializer.LOWER_GUI_SIZE_PATH));
        assertFalse(raw.containsKey(FullscreenGUISerializer.UPPER_GUI_TYPE_PATH));
        assertFalse(raw.containsKey(FullscreenGUISerializer.UPPER_GUI_SIZE_PATH));
        assertFalse(raw.containsKey(PageableGUISerializer.PAGES_PATH));
        assertFalse(raw.containsKey(PageableGUISerializer.GUI_PAGES_PATH));
    }

}