package it.fulminazzo.yagl.function;

/**
 * A consumer function that accepts three parameters.
 *
 * @param <F> the type of the first parameter
 * @param <S> the type of the second parameter
 * @param <T> the type of the third parameter
 */
@FunctionalInterface
public interface TriConsumer<F, S, T> {

    /**
     * Executes the function for the given parameters.
     *
     * @param first  the first parameter
     * @param second the second parameter
     * @param third  the third parameter
     */
    void accept(final F first, final S second, final T third);

}
