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
    static final @NotNull String UPPER_GUI_TYPE_PATH = "upperGuiType";
    static final @NotNull String UPPER_GUI_SIZE_PATH = "upperGuiSize";
    static final @NotNull String LOWER_GUI_SIZE_PATH = "lowerGuiSize";

    @Override
    protected @Nullable FullscreenGUI initialize(final @NotNull DataNode node) throws SerializationException {
        Integer lowerGUISize = node.getOrNull(LOWER_GUI_SIZE_PATH, Integer.class);
        if (lowerGUISize == null) return null;
        else return GUISerializerUtils.callIfTypeOrSizeArePresent(
                node,
                UPPER_GUI_TYPE_PATH,
                t -> GUI.newFullscreenGUI(t, lowerGUISize),
                UPPER_GUI_SIZE_PATH,
                s -> GUI.newFullscreenGUI(s, lowerGUISize)
        );
    }

    @Override
    protected void loadData(final @NotNull FullscreenGUI value, final @NotNull DataNode node) {
        // nothing else to set, already covered in initialization
    }

    @Override
    protected void dumpData(final @NotNull FullscreenGUI value, final @NotNull DataNode output) throws SerializationException {
        GUISerializerUtils.dumpTypeOrNormalGUI(value.getUpperGUI(), output, UPPER_GUI_TYPE_PATH, UPPER_GUI_SIZE_PATH);
        output.set(LOWER_GUI_SIZE_PATH, value.getLowerGUI().size());
    }

}
