package it.fulminazzo.yagl;

import org.jetbrains.annotations.NotNull;

/**
 * An exception thrown upon executing {@link SingleInstance#getInstance(Class)}
 * without having it initialized first.
 */
final class InstanceNotInitialized extends RuntimeException {

    /**
     * Instantiates a new Instance not initialized.
     *
     * @param clazz the class of the requested instance
     */
    public InstanceNotInitialized(final @NotNull Class<? extends SingleInstance> clazz) {
        super(String.format("Instance of type \"%s\" has not been initialized yet",
                clazz.getSimpleName()));
    }

}
