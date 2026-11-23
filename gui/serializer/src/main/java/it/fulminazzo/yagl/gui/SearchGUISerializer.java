package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.SerializationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A serializer for {@link SearchGUI} objects.
 *
 * @see SearchGUI
 * @see DataGUISerializer
 */
final class SearchGUISerializer extends DataGUISerializer {

    @Override
    protected @Nullable SearchGUI<?> initialize(final @NotNull DataNode node) throws SerializationException {
        Integer lowerGUISize = node.getOrNull(FullscreenGUISerializer.LOWER_GUI_SIZE_PATH, Integer.class);
        return lowerGUISize == null ? null : SearchGUI.newSearchGUI(lowerGUISize);
    }

    @Override
    protected void dumpData(final @NotNull PageableGUI value, final @NotNull DataNode output) throws SerializationException {
        super.dumpData(value, output);
        output.set(FullscreenGUISerializer.UPPER_GUI_TYPE_PATH, null);
        output.set(FullscreenGUISerializer.UPPER_GUI_SIZE_PATH, null);
    }

}