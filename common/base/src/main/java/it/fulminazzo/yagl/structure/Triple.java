package it.fulminazzo.yagl.structure;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import it.fulminazzo.yagl.function.TriConsumer;

/**
 * A data class that stores three objects together.
 *
 * @param <F> the type of the first object
 * @param <S> the type of the second object
 * @param <T> the type of the third object
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public final class Triple<F, S, T> {
    private F first;
    private S second;
    private T third;

    /**
     * Sets the internal values.
     *
     * @param first  the first value
     * @param second the second value
     * @param third  the third value
     */
    public void set(final F first, final S second, final T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Checks if the internal values are not {@code null} and, if so,
     * executes the given consumer.
     *
     * @param consumer the function to execute
     */
    public void ifPresent(final @NotNull TriConsumer<? super F, ? super S, ? super T> consumer) {
        if (first != null && second != null && third != null)
            consumer.accept(first, second, third);
    }

}
