package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A serializer for {@link FullscreenGUI}.
 *
 * @see FullscreenGUI
 * @see ObjectSerializer
 */
final class FullscreenGUISerializer extends ObjectSerializer<FullscreenGUI> {
    static final @NotNull String UPPER_GUI_PATH = "upperGUI";
    static final @NotNull String LOWER_GUI = "lowerGUI";

    @Override
    protected @Nullable FullscreenGUI initialize(final @NotNull DataNode node) throws SerializationException {
        Integer lowerGUISize = node.getOrNull(LOWER_GUI + "." + DefaultGUISerializer.SIZE_PATH, Integer.class);
        if (lowerGUISize == null) return null;
        Integer upperGUISize = node.getOrNull(UPPER_GUI_PATH + "." + DefaultGUISerializer.SIZE_PATH, Integer.class);
        if (upperGUISize == null)
            return node.get(UPPER_GUI_PATH + "." + TypeGUISerializer.INVENTORY_TYPE_PATH, GUIType.class)
                    .map(type -> GUI.newFullscreenGUI(type, lowerGUISize))
                    .orElse(null);
        else return GUI.newFullscreenGUI(upperGUISize, lowerGUISize);
    }

    @Override
    protected void loadData(final @NotNull FullscreenGUI value, final @NotNull DataNode node) {
        // nothing else to set, already covered in initialization
    }

    @Override
    protected void dumpData(final @NotNull FullscreenGUI value, final @NotNull DataNode output) throws SerializationException {
        GUI upperGUI = value.getUpperGUI();
        if (upperGUI instanceof TypeGUI)
            output.set(UPPER_GUI_PATH + "." + TypeGUISerializer.INVENTORY_TYPE_PATH, ((TypeGUI) upperGUI).getInventoryType());
        else output.set(UPPER_GUI_PATH + "." + DefaultGUISerializer.SIZE_PATH, value.getUpperGUI().size());
        output.set(LOWER_GUI + "." + DefaultGUISerializer.SIZE_PATH, value.getLowerGUI().size());
    }

}
