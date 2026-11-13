package it.fulminazzo.yagl.gui;

import it.fulminazzo.yagl.reflect.Reflect;
import it.fulminazzo.yagl.util.ValidationUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

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
        final Optional<Class<?>> guiAdapter = getGUIAdapterClass();
        if (guiAdapter.isPresent())
            Reflect.on(guiAdapter.get()).invoke(functionName, parameters);
        else
            throw new IllegalStateException("Could not find GUIAdapter class. This function requires the 'gui:bukkit' module to be added");
    }

    /**
     * Checks that the given slot is between bounds.
     * Throws a {@link IllegalArgumentException} if it is not.
     *
     * @param slot the slot
     * @param size the maximum size of the GUI
     */
    public static void checkSlot(final int slot, final int size) {
        ValidationUtils.checkRange("slot", slot, 0, size - 1);
    }

    /**
     * Returns an IllegalArgumentException with a message stating that it was not possible to add
     * the content at the given index.
     *
     * @param index the index
     * @return the illegal argument exception
     */
    public static @NotNull IllegalArgumentException cannotAddContentAtIndexException(final int index) {
        IllegalArgumentException exception = new IllegalArgumentException(
                String.format("Could not set content at index %s because contents are already full", index)
        );
        StackTraceElement[] stackTrace = exception.getStackTrace();
        exception.setStackTrace(Arrays.copyOfRange(stackTrace, 1, stackTrace.length));
        return exception;
    }

    /**
     * Gets the GUIAdapter class.
     *
     * @return the GUIAdapter class
     */
    static @NotNull Optional<Class<?>> getGUIAdapterClass() {
        try {
            return Optional.of(Class.forName("it.fulminazzo.yagl.GUIAdapter"));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

}
