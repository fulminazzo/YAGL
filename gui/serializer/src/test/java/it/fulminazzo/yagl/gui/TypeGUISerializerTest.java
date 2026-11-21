package it.fulminazzo.yagl.gui;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Getter
class TypeGUISerializerTest extends GUISerializerTestHelper<TypeGUI> {
    private final @NotNull TypeGUI target = (TypeGUI) populateGUI(GUI.newGUI(GUIType.CHEST));

    protected TypeGUISerializerTest() {
        super(TypeGUI.class);
    }

    @Override
    protected void testDeserialize(final @NotNull TypeGUI gui) {
        assertEquals(target.getInventoryType(), gui.getInventoryType());
    }

    @Override
    protected void testSerialize(final @NotNull Map<?, ?> raw) {
        assertEquals(target.getInventoryType().name(), raw.get(TypeGUISerializer.INVENTORY_TYPE_PATH));
    }

}
