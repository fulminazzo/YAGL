package it.fulminazzo.yagl.function;

/**
 * A consumer function that accepts three parameters.
 *
 * @param <F> the type of the first parameter
 * @param <S> the type of the second parameter
 * @param <T> the type of the third parameter
 * @param <R> the type of the return
 */
@FunctionalInterface
public interface TriFunction<F, S, T, R> {

    /**
     * Executes the function for the given parameters.
     *
     * @param first  the first parameter
     * @param second the second parameter
     * @param third  the third parameter
     * @return the result of the function
     */
    R apply(final F first, final S second, final T third);

}
