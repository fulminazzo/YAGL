package it.fulminazzo.yagl.structure;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * A data class that stores two objects together.
 *
 * @param <F> the type of the first object
 * @param <S> the type of the second object
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public final class Pair<F, S> {
    private F first;
    private S second;

    /**
     * Sets the internal values.
     *
     * @param first  the first value
     * @param second the second value
     */
    public void set(final F first, final S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Checks if the internal values are not {@code null} and, if so,
     * executes the given consumer.
     *
     * @param consumer the function to execute
     */
    public void ifPresent(final @NotNull BiConsumer<? super F, ? super S> consumer) {
        if (first != null && second != null)
            consumer.accept(first, second);
    }

}
