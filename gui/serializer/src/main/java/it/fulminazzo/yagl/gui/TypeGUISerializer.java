package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.ObjectSerializer;
import it.fulminazzo.yagl.serializer.SerializationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A serializer for {@link TypeGUI}.
 *
 * @see TypeGUI
 * @see ObjectSerializer
 */
final class TypeGUISerializer extends ObjectSerializer<TypeGUI> {
    static final @NotNull String INVENTORY_TYPE_PATH = "inventoryType";

    @Override
    protected @Nullable TypeGUI initialize(final @NotNull DataNode node) throws SerializationException {
        return node.get(INVENTORY_TYPE_PATH, GUIType.class).map(TypeGUI::new).orElse(null);
    }

    @Override
    protected void loadData(final @NotNull TypeGUI value, final @NotNull DataNode node) {
        // nothing else to set, already covered in initialization
    }

    @Override
    protected void dumpData(final @NotNull TypeGUI value, final @NotNull DataNode output) throws SerializationException {
        output.set(INVENTORY_TYPE_PATH, value.getInventoryType());
    }

}
