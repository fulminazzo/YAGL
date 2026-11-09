package it.fulminazzo.yagl.structure;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

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
     * Maps the internal values to a new pair.
     * Only works if {@link #isPresent()} returns {@code true}.
     *
     * @param mapper the function to execute
     * @param <A>    the type of the first object of the new pair
     * @param <B>    the type of the second object of the new pair
     * @return the new pair
     */
    public <A, B> @NotNull Pair<A, B> map(final @NotNull BiFunction<? super F, ? super S, Pair<A, B>> mapper) {
        if (isPresent())
            return mapper.apply(first, second);
        return new Pair<>();
    }

    /**
     * Checks if the internal values are not {@code null} and, if so,
     * executes the given consumer.
     *
     * @param consumer the function to execute
     */
    public void ifPresent(final @NotNull BiConsumer<? super F, ? super S> consumer) {
        if (isPresent())
            consumer.accept(first, second);
    }

    /**
     * Checks if the internal values are not {@code null}.
     *
     * @return {@code true} if the internal values are not {@code null}
     */
    public boolean isPresent() {
        return first != null && second != null;
    }

}
