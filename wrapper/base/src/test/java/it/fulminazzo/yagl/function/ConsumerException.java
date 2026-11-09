package it.fulminazzo.yagl.function;

/**
 * An implementation of {@link java.util.function.Consumer} that supports throwing an exception.
 *
 * @param <T> the type of the parameter
 * @param <X> the type of the exception
 */
@FunctionalInterface
public interface ConsumerException<T, X extends Exception> {

    /**
     * Executes the function with the given parameter.
     *
     * @param t the parameter
     * @throws X the exception thrown
     */
    void accept(final T t) throws X;

}
