package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.serializer.DataNode;
import it.fulminazzo.yagl.serializer.SerializationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A collection of utilities for GUI serialization.
 */
final class GUISerializerUtils {

    /**
     * Checks if in the given node, under the specified path, a {@link TypeGUISerializer#INVENTORY_TYPE_PATH} field is present.
     * <br>
     * If it is, the {@code ifTypePresent} function will be called.
     * <br>
     * Otherwise, if a {@link DefaultGUISerializer#SIZE_PATH} is present, the {@code ifSizePresent} function will be called.
     * <br>
     * If neither was present, {@code null} will be returned.
     *
     * @param node          the node to check
     * @param path          the path to check
     * @param ifTypePresent the function to call if a {@link TypeGUISerializer#INVENTORY_TYPE_PATH} field is present
     * @param ifSizePresent the function to call if a {@link DefaultGUISerializer#SIZE_PATH} field is present
     * @param <T>           the type of the result
     * @return the result of the function called, or {@code null} if neither was present
     * @throws SerializationException if any error occurs
     */
    public static <T> @Nullable T callIfTypeOrSizeArePresent(
            final @NotNull DataNode node,
            final @NotNull String path,
            final @NotNull Function<GUIType, T> ifTypePresent,
            final @NotNull Function<Integer, T> ifSizePresent
    ) throws SerializationException {
        Integer size = node.getOrNull(path + "." + DefaultGUISerializer.SIZE_PATH, Integer.class);
        if (size == null)
            return node.get(path + "." + TypeGUISerializer.INVENTORY_TYPE_PATH, GUIType.class)
                    .map(ifTypePresent)
                    .orElse(null);
        else return ifSizePresent.apply(size);
    }

    /**
     * Checks if the given GUI is a {@link TypeGUI}.
     * <br>
     * If it is, it will store its type in the given {@link DataNode} under
     * {@code path.}{@link TypeGUISerializer#INVENTORY_TYPE_PATH};
     * <br>
     * Otherwise, it will store its size under
     * {@code path.}{@link DefaultGUISerializer#SIZE_PATH}.
     *
     * @param target the GUI to serialize
     * @param output the output to store to
     * @param path   the path
     * @throws SerializationException if any error occurs
     */
    public static void dumpTypeOrNormalGUI(
            final @NotNull GUI target,
            final @NotNull DataNode output,
            final @NotNull String path
    ) throws SerializationException {
        if (target instanceof TypeGUI)
            output.set(path + "." + TypeGUISerializer.INVENTORY_TYPE_PATH, ((TypeGUI) target).getInventoryType());
        else
            output.set(path + "." + DefaultGUISerializer.SIZE_PATH, target.size());
    }

}
