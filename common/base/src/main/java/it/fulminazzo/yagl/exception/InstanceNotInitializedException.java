package it.fulminazzo.yagl.exception;

import it.fulminazzo.yagl.SingleInstance;
import org.jetbrains.annotations.NotNull;

/**
 * An exception thrown upon executing {@link SingleInstance#getInstance(Class)}
 * without having it initialized first.
 */
public final class InstanceNotInitializedException extends RuntimeException {
    private static final long serialVersionUID = -3244566145900279885L;

    /**
     * Instantiates a new Instance not initialized.
     *
     * @param clazz the class of the requested instance
     */
    public InstanceNotInitializedException(final @NotNull Class<? extends SingleInstance> clazz) {
        super(String.format("Instance of type \"%s\" has not been initialized yet",
                clazz.getSimpleName()));
    }

}
