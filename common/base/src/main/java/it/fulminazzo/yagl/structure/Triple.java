package it.fulminazzo.yagl.structure;

import it.fulminazzo.yagl.function.TriConsumer;
import it.fulminazzo.yagl.function.TriFunction;
import lombok.*;
import org.jetbrains.annotations.NotNull;

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
     * Maps the internal values to a new triple.
     * Only works if {@link #isPresent()} returns {@code true}.
     *
     * @param mapper the function to execute
     * @param <A>    the type of the first object of the new triple
     * @param <B>    the type of the second object of the new triple
     * @param <C>    the type of the third object of the new triple
     * @return the new triple
     */
    public <A, B, C> @NotNull Triple<A, B, C> map(final @NotNull TriFunction<? super F, ? super S, ? super T, Triple<A, B, C>> mapper) {
        if (isPresent())
            return mapper.apply(first, second, third);
        return new Triple<>();
    }

    /**
     * Checks if the internal values are not {@code null} and, if so,
     * executes the given consumer.
     *
     * @param consumer the function to execute
     */
    public void ifPresent(final @NotNull TriConsumer<? super F, ? super S, ? super T> consumer) {
        if (isPresent())
            consumer.accept(first, second, third);
    }

    /**
     * Checks if the internal values are not {@code null}.
     *
     * @return {@code true} if the internal values are not {@code null}
     */
    public boolean isPresent() {
        return first != null && second != null && third != null;
    }

}
