package it.fulminazzo.yagl.wrapper;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * An exception thrown when {@link Range} has invalid values.
 */
final class InvalidRangeException extends IllegalArgumentException {
    private static final long serialVersionUID = -2375533280010305406L;

    /**
     * Instantiates a new Invalid range exception.
     *
     * @param field the field
     * @param min   the min
     * @param max   the max
     */
    public InvalidRangeException(final @NotNull Field field,
                                 final long min,
                                 final long max) {
        super(String.format("Field '%s' of type '%s' has range: min=%s > max=%s", field.getName(),
                field.getDeclaringClass().getCanonicalName(), min, max));
    }

}
