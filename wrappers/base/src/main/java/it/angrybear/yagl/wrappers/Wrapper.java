package it.angrybear.yagl.wrappers;

import it.fulminazzo.fulmicollection.objects.FieldEquable;
import it.fulminazzo.fulmicollection.objects.Printable;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * A general class that represents a wrapper for another object.
 */
public abstract class Wrapper extends FieldEquable {

    /**
     * Allows to check the given number.
     * It uses the {@link Range} annotation.
     * If the given value is lower than {@link Range#min()} or higher than {@link Range#max()},
     * an {@link IllegalArgumentException} is thrown.
     * @param <N> the type parameter
     * @param value   the value
     * @return the passed value
     */
    protected <N extends Number> N check(final @NotNull N value) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        // The first element should be the getStackTrace invocation, the second this method.
        // So we are looking for the third.
        String method = trace[2].getMethodName().toLowerCase();
        if (method.startsWith("set") || method.startsWith("get")) method = method.substring(3);
        Field field = ReflectionUtils.getField(this, method);
        if (field.isAnnotationPresent(Range.class)) {
            Range range = field.getAnnotation(Range.class);
            int min = range.min();
            int max = range.max();
            final Refl<Class<Math>> math = new Refl<>(Math.class);
            Number foundMin = math.invokeMethod("min", value, min);
            Number foundMax = math.invokeMethod("max", value, max);

            if ((foundMin == null || !foundMin.equals(min)) || (foundMax == null || !foundMax.equals(max))) {
                final String fieldName = field.getName();

                String message = String.format("Invalid value provided for '%s'", fieldName);
                if (max != Integer.MAX_VALUE && min != Integer.MIN_VALUE) message = String.format("'%s' must be between %s and %s", fieldName, min, max);
                else if (min != Integer.MIN_VALUE) message = String.format("'%s' cannot be lower than %s", fieldName, min);
                else if (max != Integer.MAX_VALUE) message = String.format("'%s' cannot be higher than %s", fieldName, max);

                throw new IllegalArgumentException(message);
            }
        }
        return value;
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
        if (wrapper == null) return false;
        return getClass().equals(wrapper.getClass()) && getName().equalsIgnoreCase(wrapper.getName());
    }

    @Override
    public String toString() {
        return Printable.convertToJson(this);
    }
}
