package it.fulminazzo.yagl.guis;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * A collection of utilities for this package.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class GUIUtils {

    /**
     * Executes the given function using the GUIAdapter.
     * Throws {@link IllegalStateException} if the bukkit module is not provided.
     *
     * @param functionName the function name
     * @param parameters   the parameters
     */
    public static void executeGUIAdapterFunction(final @NotNull String functionName, final Object @NotNull ... parameters) {
        final Class<?> guiAdapter;
        try {
            guiAdapter = ReflectionUtils.getClass("it.fulminazzo.yagl.GUIAdapter");
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Could not find GUIAdapter class. This function requires the 'gui:bukkit' module to be added");
        }
        new Refl<>(guiAdapter).invokeMethod(functionName, parameters);
    }

    /**
     * Checks that the given slot is between bounds.
     * Throws a {@link IndexOutOfBoundsException} if it is not.
     *
     * @param slot the slot
     * @param size the maximum size of the GUI
     */
    public static void checkSlot(final int slot, final int size) {
        if (slot < 0 || slot >= size)
            throw new IndexOutOfBoundsException(String.format("Slot %s out of bounds (%s, %s)", slot, 0, size));
    }

    /**
     * Returns an IllegalArgumentException with a message stating that it was not possible to add
     * the content at the given index.
     *
     * @param index the index
     * @return the illegal argument exception
     */
    public static @NotNull IllegalArgumentException cannotAddContentAtIndexException(int index) {
        IllegalArgumentException exception = new IllegalArgumentException(
                String.format("Could not set content at index %s because contents are already full", index)
        );
        StackTraceElement[] stackTrace = exception.getStackTrace();
        exception.setStackTrace(Arrays.copyOfRange(stackTrace, 1, stackTrace.length));
        return exception;
    }

}
