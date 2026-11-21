package it.fulminazzo.yagl.gui;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Getter
class DefaultGUISerializerTest extends GUISerializerTestHelper<DefaultGUI> {
    private final @NotNull DefaultGUI target = (DefaultGUI) populateGUI(GUI.newGUI(27));

    protected DefaultGUISerializerTest() {
        super(DefaultGUI.class);
    }

    @Override
    protected void testDeserialize(final @NotNull DefaultGUI gui) {
        assertEquals(target.size(), gui.size());
    }

    @Override
    protected void testSerialize(final @NotNull Map<?, ?> raw) {
        assertEquals(target.size(), raw.get(DefaultGUISerializer.SIZE_PATH));
    }

}
