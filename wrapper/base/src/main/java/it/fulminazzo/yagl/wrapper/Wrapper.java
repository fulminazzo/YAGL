package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.yagl.reflect.Reflect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * A general class that represents a wrapper for another object.
 */
public abstract class Wrapper {

    /**
     * Uses {@link #check(Object, Number)} to check the given value.
     *
     * @param <N>   the type of the value
     * @param value the value
     * @return the value itself
     */
    protected <N extends Number> @NotNull N check(final @NotNull N value) {
        return check(this, value);
    }

    /**
     * Allows checking the given number.
     * It uses the {@link Range} annotation.
     * If the given value is lower than {@link Range#min()} or higher than {@link Range#max()},
     * an {@link IllegalArgumentException} is thrown.
     *
     * @param <N>    the type of the value
     * @param object the object to check
     * @param value  the value
     * @return the value itself
     */
    public static <N extends Number> @NotNull N check(final @NotNull Object object, final @NotNull N value) {
        String method = StackWalker.getInstance()
                .walk(s -> s.map(StackWalker.StackFrame::getMethodName)
                        .filter(m -> !m.equals("check"))
                        .findFirst()
                ).orElseThrow();
        if (method.startsWith("set") || method.startsWith("get")) method = method.substring(3);
        String fieldName = method;
        checkField(value, Reflect.on(object).getField(f -> f.getName().equalsIgnoreCase(fieldName)));
        return value;
    }

    private static <N extends Number> void checkField(final @NotNull N value, final @NotNull Field field) {
        if (field.isAnnotationPresent(Range.class)) {
            Range range = field.getAnnotation(Range.class);
            long min = range.min();
            long max = range.max();
            if (min > max) throw new InvalidRangeException(field, min, max);
            else if (value.doubleValue() < (double) min || value.doubleValue() > (double) max)
                throw new IllegalArgumentException(getExceptionMessage(field, min, max));
        }
    }

    private static @NotNull String getExceptionMessage(final @NotNull Field field, final long min, final long max) {
        final String fieldName = field.getName();

        final String message;
        if (max != Long.MAX_VALUE && min != Long.MIN_VALUE)
            message = String.format("'%s' must be between %s and %s", fieldName, min, max);
        else if (min != Long.MIN_VALUE) message = String.format("'%s' cannot be lower than %s", fieldName, min);
        else if (max != Long.MAX_VALUE) message = String.format("'%s' cannot be higher than %s", fieldName, max);
        else message = String.format("Invalid value provided for '%s'", fieldName);

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
