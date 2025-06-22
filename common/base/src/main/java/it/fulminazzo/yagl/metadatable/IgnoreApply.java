package it.fulminazzo.yagl.metadatable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An interface used by {@link Metadatable} to ignore
 * fields that should not be passed through the apply method.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreApply {
}
