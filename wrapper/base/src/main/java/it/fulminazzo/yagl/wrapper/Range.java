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
    int min() default Integer.MIN_VALUE;

    /**
     * The maximum value allowed.
     *
     * @return the value
     */
    int max() default Integer.MAX_VALUE;

}
