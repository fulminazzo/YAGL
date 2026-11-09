package it.fulminazzo.yagl.wrapper;

import java.lang.annotation.*;

/**
 * Annotations used to annotate fields and parameters that must be between a range.
 *
 * @see Wrapper#check(Number)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface Range {

    /**
     * The minimum value allowed.
     *
     * @return the value
     */
    long min() default Long.MIN_VALUE;

    /**
     * The maximum value allowed.
     *
     * @return the value
     */
    long max() default Long.MAX_VALUE;

}
