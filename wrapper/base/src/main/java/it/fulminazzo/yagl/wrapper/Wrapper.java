package it.fulminazzo.yagl.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A general class that represents a wrapper for another object.
 */
public abstract class Wrapper {

    /**
     * Allows checking the given number against the specified range.
     * If the given value is lower than {@code min}, an {@link IllegalArgumentException} is thrown.
     *
     * @param <N>        the type of the value
     * @param valueName  the name of the value (used in error messages)
     * @param value      the value to check
     * @param min        the minimum allowed value
     * @return the value itself
     */
    public static <N extends Number> @NotNull N check(final @NotNull String valueName, final @NotNull N value, final long min) {
        return check(valueName, value, min, Long.MAX_VALUE);
    }

    /**
     * Allows checking the given number against the specified range.
     * If the given value is lower than {@code min} or higher than {@code max},
     * an {@link IllegalArgumentException} is thrown.
     *
     * @param <N>        the type of the value
     * @param valueName  the name of the value (used in error messages)
     * @param value      the value to check
     * @param min        the minimum allowed value
     * @param max        the maximum allowed value
     * @return the value itself
     */
    public static <N extends Number> @NotNull N check(final @NotNull String valueName, final @NotNull N value, final long min, final long max) {
        if (min > max) throw new IllegalArgumentException(getExceptionMessage(valueName, min, max));
        else if (value.doubleValue() < (double) min || value.doubleValue() > (double) max)
            throw new IllegalArgumentException(getExceptionMessage(valueName, min, max));
        return value;
    }

    private static @NotNull String getExceptionMessage(final @NotNull String valueName, final long min, final long max) {
        final String message;
        if (max != Long.MAX_VALUE && min != Long.MIN_VALUE)
            message = String.format("'%s' must be between %s and %s", valueName, min, max);
        else if (min != Long.MIN_VALUE) message = String.format("'%s' cannot be lower than %s", valueName, min);
        else if (max != Long.MAX_VALUE) message = String.format("'%s' cannot be higher than %s", valueName, max);
        else message = String.format("Invalid value provided for '%s'", valueName);

        return message;
    }

    /**
     * Gets the name of this object.
     *
     * @return the name
     */
    public abstract @NotNull String getName();

    /**
     * Compares this {@link Wrapper} with the given one.
     *
     * @param wrapper the wrapper
     * @return true, if they have the same name
     */
    public boolean isSimilar(final @Nullable Wrapper wrapper) {
        return wrapper != null && getClass().equals(wrapper.getClass()) && getName().equalsIgnoreCase(wrapper.getName());
    }

}
