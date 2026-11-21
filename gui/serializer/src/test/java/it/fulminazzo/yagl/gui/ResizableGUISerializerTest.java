package it.fulminazzo.yagl.gui;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Getter
class ResizableGUISerializerTest extends GUISerializerTestHelper<ResizableGUI> {
    private final @NotNull ResizableGUI target = (ResizableGUI) populateGUI(GUI.newResizableGUI(27));

    protected ResizableGUISerializerTest() {
        super(ResizableGUI.class);
    }

    @Override
    protected void testDeserialize(final @NotNull ResizableGUI gui) {
        assertEquals(target.size(), gui.size());
    }

    @Override
    protected void testSerialize(final @NotNull Map<?, ?> raw) {
        assertEquals(target.size(), raw.get(DefaultGUISerializer.SIZE_PATH));
    }

}
