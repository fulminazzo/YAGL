package it.fulminazzo.yagl.exception;

import it.fulminazzo.yagl.SingleInstance;
import org.jetbrains.annotations.NotNull;

/**
 * An exception thrown upon executing {@link SingleInstance#initialize()} more than one time.
 */
public final class InstanceAlreadyInitializedException extends RuntimeException {
    private final @NotNull SingleInstance instance;

    /**
     * Instantiates a new Instance already initialized exception.
     *
     * @param instance the instance already initialized
     */
    public InstanceAlreadyInitializedException(final @NotNull SingleInstance instance) {
        this.instance = instance;
    }

    /**
     * Gets the instance already initialized.
     *
     * @param <T> the type of the instance
     * @return the instance
     */
    @SuppressWarnings("unchecked")
    public <T> @NotNull T getInstance() {
        return (T) instance;
    }

}
