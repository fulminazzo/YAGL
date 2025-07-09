package it.fulminazzo.yagl.wrapper;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import it.fulminazzo.fulmicollection.objects.Printable;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * A general class that represents a wrapper for another object.
 */
public abstract class Wrapper extends FieldEquable {

    /**
     * Uses {@link #check(Object, Number)} to check the given value.
     *
     * @param <N>   the type parameter
     * @param value the value
     * @return the value
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
     * @param <N>    the type parameter
     * @param object the object to check
     * @param value  the value
     * @return the passed value
     */
    public static <N extends Number> @NotNull N check(final @NotNull Object object, final @NotNull N value) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        // The first element should be the getStackTrace invocation, the second and third the 'check' methods.
        // So we are looking for the third.
        String method = trace[2].getMethodName().toLowerCase();
        if (method.equals("check")) method = trace[3].getMethodName().toLowerCase();
        if (method.startsWith("set") || method.startsWith("get")) method = method.substring(3);
        checkField(value, ReflectionUtils.getField(object, method));
        return value;
    }

    private static <N extends Number> void checkField(@NotNull N value, @NotNull Field field) {
        if (field.isAnnotationPresent(Range.class)) {
            Range range = field.getAnnotation(Range.class);
            int min = range.min();
            int max = range.max();
            if (min > max) throw new InvalidRangeException(field, min, max);
            else if (value.doubleValue() < (double) min || value.doubleValue() > (double) max)
                throw new IllegalArgumentException(getExceptionMessage(field, max, min));
        }
    }

    private static @NotNull String getExceptionMessage(final @NotNull Field field, final int max, final int min) {
        final String fieldName = field.getName();

        final String message;
        if (max != Integer.MAX_VALUE && min != Integer.MIN_VALUE) message = String.format("'%s' must be between %s and %s", fieldName, min, max);
        else if (min != Integer.MIN_VALUE) message = String.format("'%s' cannot be lower than %s", fieldName, min);
        else if (max != Integer.MAX_VALUE) message = String.format("'%s' cannot be higher than %s", fieldName, max);
        else message = String.format("Invalid value provided for '%s'", fieldName);

        return message;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public abstract String getName();

    /**
     * Compare this {@link Wrapper} with the given one
     *
     * @param wrapper the wrapper
     * @return true, if they have the same name
     */
    public boolean isSimilar(final @Nullable Wrapper wrapper) {
        return wrapper != null && getClass().equals(wrapper.getClass()) && getName().equalsIgnoreCase(wrapper.getName());
    }

    @Override
    public @NotNull String toString() {
        return Printable.convertToJson(this);
    }
}
