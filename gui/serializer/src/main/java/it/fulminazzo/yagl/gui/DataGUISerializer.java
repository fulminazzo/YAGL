package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.SerializationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A serializer for {@link DataGUI} objects.
 *
 * @see DataGUI
 * @see PageableGUISerializer
 */
class DataGUISerializer extends PageableGUISerializer {

    @Override
    protected @Nullable DataGUI<?> initialize(final @NotNull DataNode node) throws SerializationException {
        Integer lowerGUISize = node.getOrNull(FullscreenGUISerializer.LOWER_GUI_SIZE_PATH, Integer.class);
        if (lowerGUISize == null)
            return GUISerializerUtils.callIfTypeOrSizeArePresent(node, GUI_TYPE_PATH, DataGUI::newDataGUI, GUI_SIZE_PATH, DataGUI::newDataGUI);
        else return GUISerializerUtils.callIfTypeOrSizeArePresent(
                node,
                FullscreenGUISerializer.UPPER_GUI_TYPE_PATH,
                t -> DataGUI.newFullscreenDataGUI(t, lowerGUISize),
                FullscreenGUISerializer.UPPER_GUI_SIZE_PATH,
                s -> DataGUI.newFullscreenDataGUI(s, lowerGUISize)
        );
    }

}