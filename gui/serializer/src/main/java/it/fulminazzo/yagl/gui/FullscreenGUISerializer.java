package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link it.fulminazzo.yagl.serializer.Serializer} for {@link FullscreenGUI}.
 *
 * @see FullscreenGUI
 * @see ObjectSerializer
 */
final class FullscreenGUISerializer extends ObjectSerializer<FullscreenGUI> {
    //TODO: extensive file configurations with repeated elements. Optimize.

    static final @NotNull String UPPER_GUI_PATH = "upperGUI";
    static final @NotNull String LOWER_GUI_PATH = "lowerGUI";

    @Override
    protected @Nullable FullscreenGUI initialize(final @NotNull DataNode node) throws SerializationException {
        Integer lowerGUISize = node.getOrNull(LOWER_GUI_PATH + "." + ResizableGUISerializer.SIZE_PATH, Integer.class);
        if (lowerGUISize == null) return null;
        Integer upperGUISize = node.getOrNull(UPPER_GUI_PATH + "." + ResizableGUISerializer.SIZE_PATH, Integer.class);
        if (upperGUISize == null)
            return node.get(UPPER_GUI_PATH + "." + TypeGUISerializer.INVENTORY_TYPE_PATH, GUIType.class)
                    .map(type -> GUI.newFullscreenGUI(type, lowerGUISize))
                    .orElse(null);
        else return GUI.newFullscreenGUI(upperGUISize, lowerGUISize);
    }

    @Override
    protected void loadData(final @NotNull FullscreenGUI value, final @NotNull DataNode node) throws SerializationException {
        node.get(UPPER_GUI_PATH, GUI.class).ifPresent(u -> value.getUpperGUI().copyFrom(u, true));
        node.get(LOWER_GUI_PATH, GUI.class).ifPresent(l -> value.getLowerGUI().copyFrom(l, true));
    }

    @Override
    protected void dumpData(final @NotNull FullscreenGUI value, final @NotNull DataNode output) throws SerializationException {
        output.set(UPPER_GUI_PATH, value.getUpperGUI());
        output.set(LOWER_GUI_PATH, value.getLowerGUI().copy());
    }

}
