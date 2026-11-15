package it.fulminazzo.yagl.particle;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Identifies a property of an object.
 *
 * @param <T> the type of the property
 * @param <H> the type of the object that holds the property
 */
@Value
public class Property<T, H> {
    @NotNull Class<T> type;
    @Getter(AccessLevel.NONE)
    @NotNull Function<H, T> getter;

    /**
     * Gets the value of the property from the given holder.
     *
     * @param holder the holder of the property
     * @return the value
     */
    public T get(final @NotNull H holder) {
        return this.getter.apply(holder);
    }

}
