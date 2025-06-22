package it.fulminazzo.yagl.exceptions;

import it.fulminazzo.yagl.SingleInstance;
import org.jetbrains.annotations.NotNull;

/**
 * An exception thrown upon executing {@link SingleInstance#getInstance(Class)}
 * without having it initialized first.
 */
public final class InstanceNotInitializedException extends RuntimeException {

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
