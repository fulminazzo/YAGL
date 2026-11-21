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
     * Checks if in the given node a type field at {@code typePath} or a size field at {@code sizePath} is present.
     * <br>
     * If a size field is present at {@code sizePath}, the {@code ifSizePresent} function will be called.
     * <br>
     * Otherwise, if a type field is present at {@code typePath}, the {@code ifTypePresent} function will be called.
     * <br>
     * If neither was present, {@code null} will be returned.
     *
     * @param <T>           the type of the result
     * @param node          the node to check
     * @param typePath      the path where the type field is expected
     * @param ifTypePresent the function to call if a type field is present
     * @param sizePath      the path where the size field is expected
     * @param ifSizePresent the function to call if a size field is present
     * @return the result of the function called, or {@code null} if neither was present
     * @throws SerializationException if any error occurs
     */
    public static <T> @Nullable T callIfTypeOrSizeArePresent(
            final @NotNull DataNode node,
            final @NotNull String typePath,
            final @NotNull Function<GUIType, T> ifTypePresent,
            final @NotNull String sizePath,
            final @NotNull Function<Integer, T> ifSizePresent
    ) throws SerializationException {
        Integer size = node.getOrNull(sizePath, Integer.class);
        if (size == null)
            return node.get(typePath, GUIType.class)
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
        dumpTypeOrNormalGUI(
                target,
                output,
                path + "." + TypeGUISerializer.INVENTORY_TYPE_PATH,
                path + "." + DefaultGUISerializer.SIZE_PATH
        );
    }

    /**
     * Checks if the given GUI is a {@link TypeGUI}.
     * <br>
     * If it is, it will store its type in the given {@link DataNode} under {@code typePath};
     * <br>
     * Otherwise, it will store its size under {@code sizePath}.
     *
     * @param target   the GUI to serialize
     * @param output   the output to store to
     * @param typePath the path where the type field will be stored
     * @param sizePath the path where the size field will be stored
     * @throws SerializationException if any error occurs
     */
    public static void dumpTypeOrNormalGUI(
            final @NotNull GUI target,
            final @NotNull DataNode output,
            final @NotNull String typePath,
            final @NotNull String sizePath
    ) throws SerializationException {
        if (target instanceof TypeGUI) output.set(typePath, ((TypeGUI) target).getInventoryType());
        else output.set(sizePath, target.size());
    }

}
