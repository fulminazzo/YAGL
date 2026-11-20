package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link it.fulminazzo.yagl.serializer.Serializer} for {@link ResizableGUI}.
 *
 * @see ResizableGUI
 * @see ObjectSerializer
 */
final class ResizableGUISerializer extends ObjectSerializer<ResizableGUI> {
    static final @NotNull String SIZE_PATH = DefaultGUISerializer.SIZE_PATH;

    @Override
    protected @Nullable ResizableGUI initialize(final @NotNull DataNode node) throws SerializationException {
        return node.get(SIZE_PATH, Integer.class).map(ResizableGUI::new).orElse(null);
    }

    @Override
    protected void loadData(final @NotNull ResizableGUI value, final @NotNull DataNode node) {
        // nothing else to set, already covered in initialization
    }

    @Override
    protected void dumpData(final @NotNull ResizableGUI value, final @NotNull DataNode output) throws SerializationException {
        output.set(SIZE_PATH, value.size());
    }

}
