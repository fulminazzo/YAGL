package it.fulminazzo.yagl.guis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * A collection of utilities for this package.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class GUIUtils {

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
        return new IllegalArgumentException(
                String.format("Could not set content at index %s because contents are already full", index)
        );
    }

}
