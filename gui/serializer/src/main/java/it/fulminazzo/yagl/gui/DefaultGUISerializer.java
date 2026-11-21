package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A serializer for {@link DefaultGUI}.
 *
 * @see DefaultGUI
 * @see ObjectSerializer
 */
final class DefaultGUISerializer extends ObjectSerializer<DefaultGUI> {
    static final @NotNull String SIZE_PATH = "size";

    @Override
    protected @Nullable DefaultGUI initialize(final @NotNull DataNode node) throws SerializationException {
        return node.get(SIZE_PATH, Integer.class).map(DefaultGUI::new).orElse(null);
    }

    @Override
    protected void loadData(final @NotNull DefaultGUI value, final @NotNull DataNode node) {
        // nothing else to set, already covered in initialization
    }

    @Override
    protected void dumpData(final @NotNull DefaultGUI value, final @NotNull DataNode output) throws SerializationException {
        output.set(SIZE_PATH, value.size());
    }

}
