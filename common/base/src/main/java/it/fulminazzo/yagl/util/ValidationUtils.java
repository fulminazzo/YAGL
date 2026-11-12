package it.fulminazzo.yagl.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * A collection of utility functions for validation.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationUtils {

    /**
     * Checks whether the given number is greater than or equal to the minimum value.
     *
     * @param name   the name associated with the number
     * @param number the number to check
     * @param min    the minimum value
     */
    public static void checkMin(final @NotNull String name, final @NotNull Number number, final long min) {
        if (number.doubleValue() < min) {
            throw new IllegalArgumentException(String.format("Invalid %s: %s. Must be greater than or equal to %s", name, number, min));
        }
    }

    /**
     * Checks whether the given number is less than or equal to the maximum value.
     *
     * @param name   the name associated with the number
     * @param number the number to check
     * @param max    the maximum value
     */
    public static void checkMax(final @NotNull String name, final @NotNull Number number, final long max) {
        if (number.doubleValue() > max) {
            throw new IllegalArgumentException(String.format("Invalid %s: %s. Must be less than or equal to %s", name, number, max));
        }
    }

    /**
     * Checks whether the given number is in range [min, max].
     *
     * @param name   the name associated with the number
     * @param number the number to check
     * @param min    the minimum value
     * @param max    the maximum value
     */
    public static void checkRange(final @NotNull String name, final @NotNull Number number, final long min, final long max) {
        if (number.doubleValue() < min || number.doubleValue() > max) {
            throw new IllegalArgumentException(String.format("Invalid %s: %s. Must be in range [%s, %s]", name, number, min, max));
        }
    }

}
