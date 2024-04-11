package it.angrybear.yagl.wrappers;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * An exception thrown when {@link Range} has invalid values.
 */
class InvalidRangeException extends IllegalArgumentException {

    /**
     * Instantiates a new Invalid range exception.
     *
     * @param field the field
     * @param min   the min
     * @param max   the max
     */
    public InvalidRangeException(final @NotNull Field field,
                                 final int min, final int max) {
        super(String.format("Field '%s' of type '%s' has range: min=%s > max=%s", field.getName(),
                field.getDeclaringClass().getCanonicalName(), min, max));
    }
}
