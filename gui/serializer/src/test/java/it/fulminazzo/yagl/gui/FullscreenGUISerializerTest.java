package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.content.ItemGUIContent;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Getter
class FullscreenGUISerializerTest extends DualGUISerializerTestHelper<FullscreenGUI> {
    private final @NotNull FullscreenGUI target = (FullscreenGUI) populateGUI(GUI.newFullscreenGUI(27, 18))
            .setContents(27, ItemGUIContent.newInstance("coal"))
            .setContents(42, ItemGUIContent.newInstance("lapis_lazuli"));

    private final @NotNull FullscreenGUI targetTyped = (FullscreenGUI) populateGUI(GUI.newFullscreenGUI(GUIType.CHEST, 18))
            .setContents(27, ItemGUIContent.newInstance("coal"))
            .setContents(42, ItemGUIContent.newInstance("lapis_lazuli"));

    protected FullscreenGUISerializerTest() {
        super(FullscreenGUI.class);
    }

    @Override
    protected void testDeserialize(final @NotNull FullscreenGUI gui) {
        assertEquals(target.getUpperGUI().size(), gui.getUpperGUI().size(), "Incorrect upper GUI size");
        assertEquals(target.getLowerGUI().size(), gui.getLowerGUI().size(), "Incorrect lower GUI size");
    }

    @Override
    protected void testSerialize(final @NotNull Map<?, ?> raw) {
        Map<?, ?> upperGUI = (Map<?, ?>) raw.get(FullscreenGUISerializer.UPPER_GUI_PATH);
        assertEquals(
                target.getUpperGUI().size(),
                upperGUI.get(DefaultGUISerializer.SIZE_PATH),
                "Incorrect upper GUI size"
        );
        Map<?, ?> lowerGUI = (Map<?, ?>) raw.get(FullscreenGUISerializer.LOWER_GUI);
        assertEquals(
                target.getLowerGUI().size(),
                lowerGUI.get(DefaultGUISerializer.SIZE_PATH),
                "Incorrect lower GUI size"
        );
    }

    @Override
    protected void testDeserializeTyped(final @NotNull FullscreenGUI gui) {
        assertEquals(
                ((TypeGUI) targetTyped.getUpperGUI()).getInventoryType(),
                ((TypeGUI) gui.getUpperGUI()).getInventoryType(),
                "Incorrect upper GUI inventory type"
        );
        assertEquals(
                targetTyped.getLowerGUI().size(),
                gui.getLowerGUI().size(),
                "Incorrect lower GUI size"
        );
    }

    @Override
    protected void testSerializeTyped(final @NotNull Map<?, ?> raw) {
        Map<?, ?> upperGUI = (Map<?, ?>) raw.get(FullscreenGUISerializer.UPPER_GUI_PATH);
        assertEquals(
                ((TypeGUI) targetTyped.getUpperGUI()).getInventoryType().name(),
                upperGUI.get(TypeGUISerializer.INVENTORY_TYPE_PATH),
                "Incorrect upper GUI inventory type"
        );
        Map<?, ?> lowerGUI = (Map<?, ?>) raw.get(FullscreenGUISerializer.LOWER_GUI);
        assertEquals(
                targetTyped.getLowerGUI().size(),
                lowerGUI.get(DefaultGUISerializer.SIZE_PATH),
                "Incorrect lower GUI size"
        );
    }

}
