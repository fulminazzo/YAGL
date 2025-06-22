package it.fulminazzo.yagl.wrapper;


import java.lang.annotation.*;

/**
 * An annotation used by {@link Wrapper#check(Number)}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
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
